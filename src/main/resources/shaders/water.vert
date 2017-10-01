#version 330

layout (location = 0) in ivec2 vPosition;

out vec3 passPosition;

uniform mat4 uVP;
uniform float width;
uniform float depth;
uniform float height;

void main() {
    passPosition = vec3(vPosition.x*width, height, vPosition.y*depth);
    gl_Position = uVP*vec4(passPosition, 1.0);
}