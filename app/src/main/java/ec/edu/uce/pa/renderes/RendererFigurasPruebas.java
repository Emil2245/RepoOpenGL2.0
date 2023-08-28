package ec.edu.uce.pa.renderes;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ec.edu.uce.pa.geometria.Cilindro;
import ec.edu.uce.pa.geometria.CilindroTextura;
import ec.edu.uce.pa.geometria.Cono;
import ec.edu.uce.pa.geometria.ConoTextura;
import ec.edu.uce.pa.geometria.Cuadrado;
import ec.edu.uce.pa.geometria.Cubo;
import ec.edu.uce.pa.geometria.EsferaColor;
import ec.edu.uce.pa.geometria.Piramide;
import ec.edu.uce.pa.geometria.PrismaCuadrangular;
import ec.edu.uce.pa.geometria.PrismaTriangular;

public class RendererFigurasPruebas implements GLSurfaceView.Renderer {
    private Cilindro cilindro;
    private CilindroTextura cilindroTextura;
    private Cono cono;
    private ConoTextura conoTextura;
    private Cubo cubo;
    private Cuadrado cuadrado;
    private EsferaColor elipsoide, esfera;
    private Piramide piramide;
    private PrismaCuadrangular prismaCuadrangular;
    private PrismaTriangular prismaTriangular;
    private Context context;
    private int []arrayTextura = new int[1];
    private float[] matrizProyeccion = new float[16], matrizModelo = new float[16],
            matrizVista = new float[16], matrizTemp = new float[16];
    private float relacionAspecto, rotacion = 0.0f;

    public RendererFigurasPruebas(Context contexto) {
        this.context = contexto;
        cilindro= new Cilindro(20, 20, 1, 2, contexto, matrizProyeccion,
                matrizVista,matrizModelo);
        cilindroTextura= new CilindroTextura(20, 20, 1, 2, contexto,
                matrizProyeccion, matrizVista,matrizModelo);
        cono=new Cono(20,1,2,contexto,matrizProyeccion,matrizVista,matrizModelo);
        conoTextura=new ConoTextura(20,1,2,contexto,matrizProyeccion,matrizVista,
                matrizModelo);
        piramide= new Piramide(context,matrizProyeccion,matrizVista,matrizModelo);
        elipsoide=new EsferaColor(20, 20, 1, 2.5f, contexto,
                matrizProyeccion, matrizVista, matrizModelo);
        esfera=new EsferaColor(20, 20, 1, 1, contexto,
                matrizProyeccion, matrizVista, matrizModelo);
        cubo=new Cubo(context, matrizProyeccion, matrizVista, matrizModelo);
        cuadrado = new Cuadrado(contexto,matrizProyeccion,matrizVista,matrizModelo);
        prismaCuadrangular= new PrismaCuadrangular(context,matrizProyeccion,matrizVista,matrizModelo);
        prismaTriangular=new PrismaTriangular(context,matrizProyeccion,matrizVista,matrizModelo);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        gl.glEnable(gl.GL_DEPTH_TEST);
        /*arrayTextura = Funciones.habilitarTexturas(new GLES20(),1);
        Funciones.cargarImagenesTextura(new GLES20(),context, 0,R.drawable.galaxia2,
                arrayTextura);*/

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int ancho, int alto) {
        gl.glViewport(0, 0, ancho, alto);
        relacionAspecto = (float) ancho / (float) alto;
        Matrix.frustumM(matrizProyeccion, 0, -relacionAspecto, relacionAspecto,
                -1, 1, 1, 30);

        Matrix.setLookAtM(matrizVista, 0, 0, 0, 2,
                0, 0, 0,
                0, 1, 0);

        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizVista, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        rotacion += 0.5f;

        posicionarObjeto();
        translate(0,0,-2);
        rotate(1,1,1,rotacion);
        //cilindro.dibujar(new GLES20());
        //cono.dibujar(new GLES20());
        //piramide.dibujar(new GLES20());
        //cilindroTextura.dibujar(new GLES20());
        //conoTextura.dibujar(new GLES20());
        //elipsoide.dibujar(new GLES20());
        //esfera.dibujar(new GLES20());
        //cubo.dibujar(new GLES20());
        //cuadrado.dibujar(new GLES20());
        //prismaTriangular.dibujar(new GLES20());
        prismaCuadrangular.dibujar(new GLES20());
    }
    private void posicionarObjeto() {
        Matrix.setIdentityM(matrizModelo, 0);
        Matrix.multiplyMM(matrizTemp, 0, matrizProyeccion, 0, matrizModelo, 0);
        System.arraycopy(matrizTemp, 0, matrizProyeccion, 0, matrizTemp.length);
    }

    private void rotate(float x, float y, float z, float anguloRot){
        Matrix.rotateM(matrizModelo, 0, anguloRot, x, y, z);
    }

    private void translate(float x, float y, float z){
        Matrix.translateM(matrizModelo, 0, x, y, z);
    }

    private void scalef(float x, float y, float z){
        Matrix.scaleM(matrizModelo,0,x,y,z);
    }
}

