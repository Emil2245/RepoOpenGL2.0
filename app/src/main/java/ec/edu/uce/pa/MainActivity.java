package ec.edu.uce.pa;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.uce.pa.renderes.RenderHexagonoProyFP;
import ec.edu.uce.pa.renderes.RenderHexagonoProyO;
import ec.edu.uce.pa.renderes.RenderHexagonoStride;
import ec.edu.uce.pa.renderes.RenderHexagonoTextura;
import ec.edu.uce.pa.renderes.RendererEsfera;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView view;
    private GLSurfaceView.Renderer render = null;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
//        render = new RenderHexagonoProyFP(this);
//        render = new RenderHexagonoTextura(this);
        render = new RendererEsfera(this);
        //render = new RenderHexagonoStride(this);
//        render = new RenderHexagonoProyO(this);
        //render = new RenderHexagonoColor(this);
        //render = new RenderHexagono(this);
       // render = new RenderPunto();

        view.setRenderer(render);
        setContentView(view);
    }
}