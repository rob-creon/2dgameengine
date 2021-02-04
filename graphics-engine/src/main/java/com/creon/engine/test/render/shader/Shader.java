package com.creon.engine.test.render.shader;

import java.util.ArrayList;

import com.creon.engine.test.util.IO;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public abstract class Shader {

	private String vertexShaderPath, fragmentShaderPath, fragDataLocationName;

	protected int vertexShader, fragmentShader, shaderProgram;
	private ArrayList<ShaderAttrib> attribs;
	private ArrayList<ShaderUniform> uniforms;

	public Shader(String vertexShaderPath, String fragmentShaderPath, String fragDataLocationName) {
		this.vertexShaderPath = vertexShaderPath;
		this.fragmentShaderPath = fragmentShaderPath;
		this.fragDataLocationName = fragDataLocationName;

		attribs = new ArrayList<ShaderAttrib>();
		uniforms = new ArrayList<ShaderUniform>();

		init();
	}

	protected abstract void declareAttribs();

	protected abstract void declareUniforms();

	private void init() {
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, IO.loadFile(vertexShaderPath));
		glCompileShader(vertexShader);

		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, IO.loadFile(fragmentShaderPath));
		glCompileShader(fragmentShader);

		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(vertexShader));
		}

		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
		}

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);

		glBindFragDataLocation(shaderProgram, 0, fragDataLocationName);
		glLinkProgram(shaderProgram);
		
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			throw new RuntimeException("Error Linking Shader: " + glGetProgramInfoLog(shaderProgram));
		}

		this.useProgram();

		this.declareAttribs();
		
		initAttribs();

		this.declareUniforms();
	}

	private void initAttribs() {
		int stride = 0;
		for (ShaderAttrib attrib : attribs) {
			stride += attrib.getSizeInBytes();
		}

		int offset = 0;
		for (ShaderAttrib attrib : attribs) {
			attrib.init(stride, offset);
			offset += attrib.getSizeInBytes();
		}
	}

	protected ShaderAttrib declareAttrib(String name, int size) {
		ShaderAttrib attrib = new ShaderAttrib(shaderProgram, name, size, GL_FLOAT, false);
		attribs.add(attrib);
		return attrib;
	}

	protected <T> ShaderUniform<T> declareUniform(String name, Class<T> type) {
		ShaderUniform<T> uniform = new ShaderUniform<T>(name, type);
		uniform.init(shaderProgram);
		uniforms.add(uniform);
		return uniform;
	}

	public void destroy() {
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(shaderProgram);
	}

	public void useProgram() {
		glUseProgram(shaderProgram);
	}

	protected ArrayList<ShaderAttrib> getAttribs() {
		return attribs;
	}

	protected ArrayList<ShaderUniform> getUniforms() {
		return uniforms;
	}
}
