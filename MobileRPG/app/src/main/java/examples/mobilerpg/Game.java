package examples.mobilerpg;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class Game extends Activity implements View.OnClickListener {
    public GamePanel gamePanel;
    public FrameLayout game;
    public RelativeLayout mainButtons;
    public Button leftButton;
    public Button rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Turn Title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamePanel = new GamePanel(this);
       game = new FrameLayout(this);
        mainButtons = new RelativeLayout(this);
        leftButton = new Button(this);
        rightButton = new Button(this);
        leftButton.setBackgroundColor(0);
        leftButton.setText("left");
        leftButton.setId(R.id.button);
        rightButton.setBackgroundColor(0);
        rightButton.setText("right");
        rightButton.setId(R.id.button2);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        RelativeLayout.LayoutParams b1 = new RelativeLayout.LayoutParams(550, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams b2 = new RelativeLayout.LayoutParams(550, ViewGroup.LayoutParams.MATCH_PARENT);
        b2.setMarginStart(550);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mainButtons.setLayoutParams(params);
        mainButtons.addView(leftButton);
        mainButtons.addView(rightButton);
        leftButton.setLayoutParams(b1);
        rightButton.setLayoutParams(b2);
        game.addView(gamePanel);
        game.addView(mainButtons);
        setContentView(game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        if(v.getId()== R.id.button){
            System.out.println("pressed left");
            gamePanel.left=true;
        }
        if(v.getId()==R.id.button2){
            System.out.println("pressed right");
            gamePanel.right=true;
        }
    }
}
