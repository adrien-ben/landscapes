#version 330

in float passHeight;

out vec4 finalColor;

void main() {
    vec3 color;
    if (passHeight < 0.25) {
        color = vec3(0.0, 0.0, 1.0);
    } else if (passHeight < 0.35) {
        color = vec3(0.0, 0.5, 1.0);
    }else if (passHeight < 0.55) {
        color = vec3(0.0, 0.8, 0.0);
    } else if (passHeight < 0.8) {
        color = vec3(0.8, 0.5, 0.5);
    } else {
        color = vec3(1.0, 1.0, 1.0);
    }
    finalColor = vec4(vec3(color), 1.0);
}
