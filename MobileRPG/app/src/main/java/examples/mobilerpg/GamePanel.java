package examples.mobilerpg;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rohit on 10/4/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    public GamePanel(Context context){
        super(context);

        //Add callback to surfaceholder to intercept events (fingertouch)
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        //Make gamePanel focusable to handle events
        setFocusable(true);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
    boolean retry = true;
        while(retry) {
            try{
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
                retry = false;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holer){
    thread.setRunning(true);
        thread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }
    public void update() {

    }
}
