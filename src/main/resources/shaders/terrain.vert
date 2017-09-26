#version 330

layout (location = 0) in vec4 vPosition;

uniform mat4 uVP;

out float passHeight;

void main() {
    passHeight = vPosition.y;
    gl_Position = uVP*vec4(vPosition.x, vPosition.y*10, vPosition.zw);
}
