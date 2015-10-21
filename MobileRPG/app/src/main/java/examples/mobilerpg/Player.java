package examples.mobilerpg;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by Rohit on 10/12/2015.
 */
public class Player extends GameObject {
    private Bitmap sprite;
    private int money;
    private int morale;
    private double dya;
    private boolean dead;
    private boolean down;
    private boolean playing;
    private Animation anim = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int frames) {
        sprite = res;
        x = 250;
        y = 250;
        dy = 0;
        money = 1000;
        morale = 10;
        height = h;
        width = w;
        Bitmap[] image = new Bitmap[frames];
        for(int i = 0;i<image.length;i++) {
            image[i] = Bitmap.createBitmap(sprite, i*width, 0, width, height);
        }
         anim.setFrames(image);
        anim.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b) {
        down = b;
    }

    public void update(boolean left, boolean right) {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            money++;
            startTime = System.nanoTime();
        }
        if (left) {
            x-=10;
        }
        if(right){
            x+=10;
        }

    }

    public void draw(Canvas canvas) {
            canvas.drawBitmap(anim.getImage(),x,y,null);
    }

    public int getMoney() {
        return money;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean p) {
        playing = p;
    }

    public void resetDYA() {
        dya = 0;
    }

    public void resetMoney() {
        money = 0;
    }


}
