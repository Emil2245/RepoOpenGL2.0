package ec.edu.uce.pa.renderes;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ec.edu.uce.pa.R;
import ec.edu.uce.pa.geometria.Astros;
import ec.edu.uce.pa.geometria.Estrellas;
import ec.edu.uce.pa.utilidades.Funciones;

public class RendererSistemaSolar implements GLSurfaceView.Renderer {
    private Astros astro;
    private Estrellas estrellas;
    private Context context;

    private static float[] matrizProyeccion = new float[16];
    private static float[] matrizModelo = new float[16];
    private static float[] matrizVista = new float[16];
    private static float[] matrizTemp = new float[16];
    private float relacionAspecto, rotacion = 0.0f;
    int[] arrayTextura = new int[10];

    public RendererSistemaSolar(Context contexto) {
        this.context = contexto;
        astro = new Astros(50, 50, 1.0f, 1.0f, contexto, matrizProyeccion, matrizVista, matrizModelo);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);
        estrellas = new Estrellas();

        GLES20.glGenTextures(2, arrayTextura, 0);
        arrayTextura = Funciones.habilitarTexturas(new GLES20(), 10);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.sun, 0, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.mercurio, 1, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.venus, 2, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.tierra, 3, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.moon, 4, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.mars, 5, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.jupiter, 6, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.saturn, 7, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.uranus, 8, arrayTextura);
        Funciones.cargarImagenesTexturas(new GLES20(), context, R.drawable.neptune, 9, arrayTextura);

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
        GLES20 gles20= new GLES20();
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        Matrix.setIdentityM(matrizModelo, 0);
        // sol
        transladar(0, 10, -80f);
        rotar(0, 1, 0, rotacion);
        escalar(9f, 9f, 9f);
        astro.dibujar(gles20, arrayTextura, 0);

        //mercurio
        transladar(0, 0, -1.3f);
        escalar(0.1f,0.1f,0.1f);
        astro.dibujar(gles20, arrayTextura, 1);
        //venus
        transladar(0, 0, -3f);
        escalar(1.3f, 1.3f, 1.3f);
        astro.dibujar(gles20, arrayTextura, 2);
        //tierra
        transladar(0, 0, -5f);
        escalar(1.5f, 1.5f, 1.5f);
        astro.dibujar(gles20, arrayTextura, 3);
        //marte
        transladar(0, 0, -3);
        escalar(1.45f, 1.45f, 1.45f);
        astro.dibujar(gles20, arrayTextura, 5);
        //jupiter
        transladar(0, 0, -3);
        escalar(1.6f, 1.6f, 1.6f);
        astro.dibujar(gles20, arrayTextura, 6);
        //saturno
        transladar(0, 0, -1.5f);
        escalar(0.6f, 0.6f, 0.6f);
        astro.dibujar(gles20, arrayTextura, 7);
        //urano
        transladar(0, 0, -1);
        escalar(0.8f, 0.8f, 0.8f);
        astro.dibujar(gles20, arrayTextura, 8);
        //neptuno
        transladar(0, 0, -1);
        escalar(0.6f, 0.6f, 0.6f);
        astro.dibujar(gles20, arrayTextura, 9);

        rotacion += 0.8f;
    }

    private void invocarFrustrum() {
        Matrix.frustumM(matrizProyeccion, 0, -relacionAspecto, relacionAspecto,
                -1, 1, 1, 800);

        Matrix.setLookAtM(matrizVista, 0, 0, 30, -1,
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
