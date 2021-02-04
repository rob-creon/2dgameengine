package com.creon.engine.test.render.shader;

import org.joml.Matrix4f;

public class BasicShader extends Shader {

	private ShaderAttrib posAttrib, colAttrib, texAttrib;

	private ShaderUniform<Integer> texUni;
	private ShaderUniform<Matrix4f> modelUni, viewUni, projUni;

	public BasicShader() {
		super("vertexShader.glsl", "fragmentShader.glsl", "fragColor");
	}

	protected void declareAttribs() {
		posAttrib = super.declareAttrib("position", 2);
		colAttrib = super.declareAttrib("color", 3);
		texAttrib = super.declareAttrib("texcoord", 2);
	}

	protected void declareUniforms() {

		texUni = super.declareUniform("texImage", Integer.class);
		modelUni = super.declareUniform("model", Matrix4f.class);
		viewUni = super.declareUniform("view", Matrix4f.class);
		projUni = super.declareUniform("projection", Matrix4f.class);

		float screenSize = 100.0f;
		Matrix4f model = new Matrix4f();
		Matrix4f view = new Matrix4f();
		Matrix4f projection = new Matrix4f().ortho(-screenSize, screenSize, -screenSize, screenSize, -screenSize,
				screenSize);

		texUni.sendValue(0);
		modelUni.sendValue(model);
		viewUni.sendValue(view);
		projUni.sendValue(projection);
	}

	public void setTexture(int texture) {
		texUni.sendValue(texture);
	}

	public void setModelMatrix(Matrix4f mat) {
		modelUni.sendValue(mat);
	}

	public void setViewMatrix(Matrix4f mat) {
		viewUni.sendValue(mat);
	}

	public void setProjMatrix(Matrix4f mat) {
		projUni.sendValue(mat);
	}
}
