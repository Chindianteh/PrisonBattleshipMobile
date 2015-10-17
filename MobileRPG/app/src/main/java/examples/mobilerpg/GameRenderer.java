package examples.mobilerpg;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Arjun on 10/14/2015.
 */
public class GameRenderer implements GLSurfaceView.Renderer {

    private Triangle mT;

    private float[] mRotationMatrix = new float[16];

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        //Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //initialize a triangle
        mT = new Triangle();
    }

    public void onDrawFrame(GL10 unused){
        float[] model_view_matrix = new float[16];
        float[] scratch = new float[16];
        float[] scratch2 = new float[16];
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //Set the camera position
        Matrix.setLookAtM(mViewMatrix, 0, 0, -2, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //Calculate the projection and view transformation
        //the result is stored in mMVPMatrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.20f * ((int) time);
        float translateX = (float) Math.sin((double) time/400) * 0.5f;
        float translateY = (float) Math.cos((double) time/400) * 0.5f;

        mT.set_position(translateX, translateY, 0);
        mT.set_rotation(angle, 0.0f, 0.0f, -1.0f);

//            Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
//            Matrix.setIdentityM(scratch, 0);
//            Matrix.translateM(scratch, 0, 0, translateY, translateX);

//            Matrix.multiplyMM(scratch2, 0, mMVPMatrix, 0, scratch, 0);
//            //combine the rotation matrix with the projection and camera view
//            //Note that the mMVPMatrix factor *must be first* in order
//            //for the matrix multiplication product to be correct.
//            Matrix.multiplyMM(model_view_matrix, 0, scratch2, 0, mRotationMatrix, 0);

        //Draw Triangle
        mT.draw(mMVPMatrix);
    }

    //mMVPMatrix is an abbreviation for "Model View Projection Matrix"

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height){
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        //this projection matrix is applied to object coordinates
        //in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){
        //create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        //or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        //add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

}
