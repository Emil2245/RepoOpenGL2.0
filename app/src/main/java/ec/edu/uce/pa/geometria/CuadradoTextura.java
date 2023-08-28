package ec.edu.uce.pa.geometria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ec.edu.uce.pa.utilidades.Funciones;
import ec.edu.uce.pa.R;

public class CuadradoTextura {

    private FloatBuffer bufferVertices;
    private ByteBuffer bufferIndice;
    private FloatBuffer bufferTexturas;
    private static final int byteFlotante = 4;
    private static final int compPorVertice = 2;
    private static final int compPorText = 2;
    private final static int STRIDE = (compPorVertice + compPorText) * byteFlotante;
    private int[] arrayTexturas;

    private float[] matrizProyeccion,matrizVista, matrizModelo;

    private Context contexto;

    public CuadradoTextura(Context contexto, float[] matrizProyeccion, float[] matrizVista,
                           float[] matrizModelo) {
        this.matrizProyeccion = matrizProyeccion;
        this.matrizVista = matrizVista;
        this.matrizModelo = matrizModelo;
        this.contexto = contexto;

        float[] vertices = {
                -1, -1,
                1, -1,
                -1, 1,
                1, 1
        };

        float[] texturas = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f
        };

        byte[] indices = {
                0, 2, 3,
                0, 3, 1
        };

        bufferVertices = Funciones.generarFloatBuffer(vertices);
        bufferTexturas = Funciones.generarFloatBuffer(texturas);
        bufferIndice = Funciones.generarByteBuffer(indices);
    }

    public void dibujar(GLES20 gl, int indiceTextura) {
        int vertexShader = 0;
        int fragmentShader = 0;

        String sourceVS = Funciones.leerArchivo(R.raw.mvp_textura_vertex_shader, contexto);
        vertexShader = Funciones.crearShader(gl.GL_VERTEX_SHADER, sourceVS, gl);

        String sourceFS = Funciones.leerArchivo(R.raw.textura_fragment_shader, contexto);
        fragmentShader = Funciones.crearShader(gl.GL_FRAGMENT_SHADER, sourceFS, gl);

        int programa = Funciones.crearPrograma(vertexShader, fragmentShader, gl);
        gl.glUseProgram(programa);

        bufferVertices.position(0);
        int idVertexShader = gl.glGetAttribLocation(programa, "posVertexShader");
        gl.glVertexAttribPointer(idVertexShader,
                compPorVertice,
                gl.GL_FLOAT,
                false,
                0,
                bufferVertices);
        gl.glEnableVertexAttribArray(idVertexShader);

        bufferTexturas.position(0);
        int idFragmentShader = gl.glGetAttribLocation(programa, "texturaVertex");
        gl.glVertexAttribPointer(idFragmentShader,
                compPorText,
                gl.GL_FLOAT,
                false,
                0,
                bufferTexturas);
        gl.glEnableVertexAttribArray(idFragmentShader);

        int idPosMatrizProy = gl.glGetUniformLocation(programa, "matrizProjection");
        gl.glUniformMatrix4fv(idPosMatrizProy, 1, false, matrizProyeccion, 0);

        int idPosMatrizView = gl.glGetUniformLocation(programa, "matrizView");
        gl.glUniformMatrix4fv(idPosMatrizView, 1, false, matrizVista, 0);

        int idPosMatrizModel = gl.glGetUniformLocation(programa, "matrizModel");
        gl.glUniformMatrix4fv(idPosMatrizModel, 1, false, matrizModelo, 0);

        gl.glFrontFace(gl.GL_CW);

        gl.glBindTexture(gl.GL_TEXTURE_2D,arrayTexturas[indiceTextura]);
        gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, bufferIndice);


        gl.glFrontFace(gl.GL_CCW);

        gl.glDisableVertexAttribArray(idVertexShader);
        gl.glDisableVertexAttribArray(idFragmentShader);

        Funciones.liberarShader(programa, vertexShader, fragmentShader);
    }
    public void habilitarTexturasFondo(GLES20 gl,int indice){
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_TEXTURE_2D);
        arrayTexturas = new int[indice];
        gl.glGenTextures(indice, arrayTexturas, 0);
    }

    public void cargarImagenesTexturaFondo(GLES20 gl, Context contexto, int idImagen, int indice){
        Bitmap bitmap = BitmapFactory.decodeResource(contexto.getResources(), idImagen);
        gl.glBindTexture(gl.GL_TEXTURE_2D, arrayTexturas[indice]);
        GLUtils.texImage2D(gl.GL_TEXTURE_2D, 0, bitmap, 0);
        gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
        gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR);
        bitmap.recycle();
    }
}

