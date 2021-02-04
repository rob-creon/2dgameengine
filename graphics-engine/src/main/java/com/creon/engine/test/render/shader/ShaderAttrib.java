package com.creon.engine.test.render.shader;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class ShaderAttrib {

	private static final int GL_FLOAT_SIZE = 4; // size of a float in bytes

	private int attrib;

	private String name;
	private int size, type;
	private boolean normalized;

	public ShaderAttrib(int shaderHandle, String name, int size, int type, boolean normalized) {

		this.name = name;
		this.size = size;
		this.type = type;
		this.normalized = normalized;

		attrib = glGetAttribLocation(shaderHandle, name);
	}

	public void init(int stride, int offset) {
		glEnableVertexAttribArray(attrib);
		glVertexAttribPointer(attrib, size, // size per vertex
				type, // type
				normalized, // normalize?
				stride, // stride
				offset // offset
		);
	}

	public int getSizeInBytes() {
		switch (type) {
		case GL_FLOAT:
			return size * GL_FLOAT_SIZE;
		default:
			return 0;
		}
	}
}
