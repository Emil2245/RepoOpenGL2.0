package ec.edu.uce.pa.renderes;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.geometria.Astros2;
import ec.edu.uce.pa.geometria.Estrellas2;
import ec.edu.uce.pa.utilidades.Funciones;

public class RendererPruebas implements GLSurfaceView.Renderer {
    private Astros2 astro;
    private Estrellas2 estrellas2;
    private Context context;
    private static float[] stackModelo = new float[16];
    private static float[] stackVista = new float[16];
    private static float[] stackProyeccion = new float[16];

    private static float[] matrizModelo = new float[16];
    private static float[] matrizVista = new float[16];
    private static float[] matrizProyeccion = new float[16];
    private static float[] matrizTemp = new float[16];
    private float relacionAspecto, rotacion = 0.0f;
    int[] arrayTextura = new int[10];

    public RendererPruebas(Context contexto) {
        this.context = contexto;
        astro = new Astros2(25, 25, 1.0f, 1.0f, contexto, matrizProyeccion, matrizVista, matrizModelo);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);
        estrellas2 = new Estrellas2();

        GLES20.glGenTextures(2, arrayTextura, 0);
        arrayTextura = Funciones.habilitarTexturas(new GLES20(), 10);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.sun, 0, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.mercurio, 1, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.venus, 2, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.tierra, 3, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.moon, 4, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.mars, 5, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.jupiter, 6, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.saturn, 7, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.uranus, 8, arrayTextura);
//        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.neptune, 9, arrayTextura);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int ancho, int alto) {
        gl.glViewport(0, 0, ancho, alto);
        relacionAspecto = (float) ancho / (float) alto;

        invocarFrustrum();
        invocarMatrices();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

        GLES20 gles20 = new GLES20();
        Matrix.setIdentityM(matrizModelo, 0);

        // sol
        transladar(0, 0, -10f);
        astro.dibujar(gles20, arrayTextura, 0);
        rotar(0, 1, 0, rotacion);

        pushMatrix();
        //mercurio
        escalar(0.5f, 0.5f, 0.5f);
        transladar(0, 0, -4f);
        rotar(0, 1, 0, rotacion);
        astro.dibujar(gles20, arrayTextura, 1);
        //venus
        transladar(0, 0, -1f);
        escalar(0.5f, 0.5f, 0.5f);
        astro.dibujar(gles20, arrayTextura, 2);
        popMatrix();

        transladar(0, 0, -4f);
        astro.dibujar(gles20, arrayTextura, 1);

        rotacion += 1.8f;
    }

    private void invocarFrustrum() {
        Matrix.frustumM(matrizProyeccion, 0, -relacionAspecto, relacionAspecto,
                -1, 1, 1f, 100);

        Matrix.setLookAtM(matrizVista, 0,
                0, 0, -1,
                0, 0, 0,
                0, 1, 0);
        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizVista, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    private void invocarMatrices() {
        Matrix.setIdentityM(matrizModelo, 0);
        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizModelo, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    private void resetearMatrices() {
        invocarFrustrum();
        invocarMatrices();
        Matrix.setIdentityM(matrizModelo, 0);
    }


    public void pushMatrix() {
        System.arraycopy(matrizModelo, 0, stackModelo, 0, stackModelo.length);
        System.arraycopy(matrizVista, 0, stackVista, 0, stackVista.length);
        System.arraycopy(matrizProyeccion, 0, stackProyeccion, 0, stackProyeccion.length);
    }

    public void popMatrix() {
        System.arraycopy(stackModelo, 0, matrizModelo, 0, matrizModelo.length);
        System.arraycopy(stackVista, 0, matrizVista, 0, matrizVista.length);
        System.arraycopy(stackProyeccion, 0, matrizProyeccion, 0, matrizProyeccion.length);
    }

    private void rotar(float x, float y, float z, float anguloRot) {
        Matrix.rotateM(matrizModelo, 0, anguloRot, x, y, z);
    }

    private void escalar(float x, float y, float z) {
        Matrix.scaleM(matrizModelo, 0, x, y, z);
    }

    private void transladar(float x, float y, float z) {
        Matrix.translateM(matrizModelo, 0, x, y, z);
    }

}
