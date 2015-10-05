package examples.mobilerpg;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Rohit on 10/4/2015.
 */
public class MainThread extends Thread {
    private int FPS = 30;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }
    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {}
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                this.sleep(waitTime);
            } catch(Exception e) {}
            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS) {
                avgFPS = 1000/((totalTime/frameCount)/10000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(avgFPS);
            }
        }
    }
    public void setRunning(boolean b){
        running = b;
    }
}
