#version 150 core

in vec3 vertexColor;
in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D texImage;

void main() {
	vec4 textureColor = texture(texImage, textureCoord);
	vec4 test = textureColor * 2.0;
	
    fragColor = textureColor;//vec4(textureColor, 1.0);
}