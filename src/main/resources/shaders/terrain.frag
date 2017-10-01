#version 330

struct Light {
    vec4 color;
    float intensity;
};

struct DirectionalLight {
    Light base;
    vec3 direction;
};

in float passHeight;
in vec3 passNormal;
in vec3 passColor;

out vec4 finalColor;

uniform Light uAmbient;
uniform DirectionalLight uSun;

void main() {
    float diffuse = dot(-normalize(uSun.direction), normalize(passNormal));
    vec3 ambient = uAmbient.intensity*uAmbient.color.rgb;
    vec3 sun = diffuse*uSun.base.intensity*uSun.base.color.rgb;
    finalColor = vec4(vec3(ambient*passColor + sun*passColor), 1.0);
}
