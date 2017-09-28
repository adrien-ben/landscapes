#version 330

in float passHeight;
in vec3 passNormal;
in vec3 passColor;

out vec4 finalColor;

float ambient = 0.3;
vec3 lightDir = normalize(vec3(1.0, -1.0, 1.0));

void main() {
    float diffuse = dot(-lightDir, normalize(passNormal));
    finalColor = vec4(vec3(ambient*passColor + diffuse*passColor), 1.0);
}
