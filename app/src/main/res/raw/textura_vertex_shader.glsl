attribute vec4 posVertexShader;
attribute vec2 texturaVertex;
varying vec2 fragTexturaVertex;
uniform mat4 matrizProyeccion;

void main() {
    gl_Position = matrizProyeccion*posVertexShader; // multipliecamos por la matriz de proyeccion
    gl_PointSize = 80.0;
    fragTexturaVertex = texturaVertex;
}
