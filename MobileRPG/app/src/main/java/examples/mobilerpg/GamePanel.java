package examples.mobilerpg;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Rohit on 10/4/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    public Player player;
    private Background bg;
    public static final int HEIGHT = 846;
    public static final int WIDTH = 400;
    public static final int MOVESPEED = -5;
    public boolean left, right;

    public GamePanel(Context context){
        super(context);

        //Add callback to surfaceholder to intercept events (fingertouch)
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        //Make gamePanel focusable to handle events
        setFocusable(true);
        left = false;
        right = false;
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
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.testbg));
      player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player),100,100,1);
        player.setPlaying(true);
      thread.setRunning(true);
        thread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    public void update() {
        bg.update();
        if(player.getPlaying()){
            player.update(left,right);
        }
        this.left = false;
        this.right = false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);
        if(canvas != null) {
            final int saveState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            canvas.restoreToCount(saveState);
        }
    }
}
