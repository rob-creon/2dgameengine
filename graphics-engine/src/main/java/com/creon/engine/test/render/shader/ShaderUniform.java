package com.creon.engine.test.render.shader;

import org.joml.Matrix4f;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class ShaderUniform<T> {

	private String name;
	private Class<T> type;

	private int uniform = -1;

	public ShaderUniform(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	public void init(int shaderProgram) {
		uniform = glGetUniformLocation(shaderProgram, name);
	}

	public void sendValue(T val) {
		if (uniform == -1) {
			//throw new RuntimeException("Uniform set before init!");
			return;
		}

		switch (type.getSimpleName()) {
		case "Matrix4f":
			glUniformMatrix4fv(uniform, false, ((Matrix4f) val).get(BufferUtils.createFloatBuffer(16)));
			break;
		case "Integer":
			glUniform1i(uniform, (Integer) val);
			break;
		default:
			throw new RuntimeException("Unhandled uniform type: " + type.getSimpleName());
		}
	}
}
