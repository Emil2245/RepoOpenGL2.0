attribute vec4 posVertexShader;
attribute vec2 texturaVertex;
varying vec2 fragTexturaVertex;
uniform mat4 matrizModel;
uniform mat4 matrizView;
uniform mat4 matrizProjection;

void main(){
    mat4 mvp = matrizProjection * matrizView * matrizModel;
    gl_Position = mvp * posVertexShader;

    gl_PointSize = 80.0;
    fragTexturaVertex = texturaVertex;
}
