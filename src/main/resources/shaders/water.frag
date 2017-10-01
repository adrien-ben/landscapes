#version 330

struct Light {
    vec4 color;
    float intensity;
};

struct DirectionalLight {
    Light base;
    vec3 direction;
};

in vec3 passPosition;

out vec4 finalColor;

uniform Light uAmbient;
uniform DirectionalLight uSun;
uniform vec3 eye;

vec3 up = vec3(0.0, 1.0, 0.0);
vec3 waterColor = vec3(55.0/255, 204.0/255, 194.0/255);

void main() {
    vec3 ambient = uAmbient.intensity*uAmbient.color.rgb*waterColor;

    vec3 toSun = -normalize(uSun.direction);
    vec3 diffuse = dot(toSun, up)*uSun.base.intensity*uSun.base.color.rgb*waterColor;

    vec3 toEye = normalize(eye - passPosition);
    vec3 halfVector = normalize(toEye + toSun);
    vec3 specular = pow(max(0.0, dot(halfVector, up)), 32)*uSun.base.intensity*uSun.base.color.rgb;

    finalColor = vec4(ambient + diffuse + specular, 0.8);
}