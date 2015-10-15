package examples.mobilerpg;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rohit on 10/4/2015.
 */
public class GamePanel extends GLSurfaceView{

    private final GameRenderer mRenderer;

    public GamePanel(Context context){
        super(context);

        //Create an OpenGL ES 2.0 Context
        setEGLContextClientVersion(2);

        mRenderer = new GameRenderer();

        //Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
