#version 330

layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec3 vNormal;
layout (location = 2) in vec3 vColor;

uniform mat4 uVP;

out float passHeight;
out vec3 passNormal;
out vec3 passColor;

void main() {
    passHeight = vPosition.y;
    passNormal = vNormal;
    passColor = vColor;
    gl_Position = uVP*vec4(vPosition.x, vPosition.y, vPosition.zw);
}
