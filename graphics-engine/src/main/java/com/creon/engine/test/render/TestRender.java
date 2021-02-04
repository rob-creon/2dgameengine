package com.creon.engine.test.render;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;

import com.creon.engine.test.game.Game;
import com.creon.engine.test.render.shader.BasicShader;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestRender {

	public TestRender() {
		init();
	}

	int vao;
	int vbo;
	int ebo;
	int texture;

	BasicShader shader;

	private void init() {

		ByteBuffer image;
		int imageWidth, imageHeight;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			STBImage.stbi_set_flip_vertically_on_load(true);
			String absolutePath = TestRender.class.getClassLoader().getResource("smile.png").getPath().substring(1);
			if (!System.getProperty("os.name").contains("Windows")) {
				absolutePath = File.separator + absolutePath;
			}
			image = STBImage.stbi_load(absolutePath, w, h, comp, 4);
			if (image == null)
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());

			imageWidth = w.get();
			imageHeight = h.get();
		}

		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, imageWidth, imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		float x1 = (1f - imageWidth) / 2f;
		float y1 = (1f - imageHeight) / 2f;
		float x2 = x1 + imageWidth;
		float y2 = y1 + imageHeight;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(4 * 7);
			vertices.put(x1).put(y1).put(1f).put(1f).put(1f).put(0f).put(0f);
			vertices.put(x2).put(y1).put(1f).put(1f).put(1f).put(1f).put(0f);
			vertices.put(x2).put(y2).put(1f).put(1f).put(1f).put(1f).put(1f);
			vertices.put(x1).put(y2).put(1f).put(1f).put(1f).put(0f).put(1f);
			vertices.flip();

			vbo = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

			IntBuffer elements = stack.mallocInt(2 * 3);
			elements.put(0).put(1).put(2);
			elements.put(2).put(3).put(0);
			elements.flip();

			ebo = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_STATIC_DRAW);

		}

		shader = new BasicShader();

	}

	float angle = 0.0f;

	public void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		angle += Game.getInstance().getDelta() * 90f;
		Matrix4f model = new Matrix4f().rotate((float) Math.toRadians(angle), 0f, 0f, 1f);

		glBindVertexArray(vao);

		glBindTexture(GL_TEXTURE_2D, texture);
		shader.useProgram();

		shader.setModelMatrix(model);

		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

	}

	public void destroy() {
		shader.destroy();
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
	}
}
