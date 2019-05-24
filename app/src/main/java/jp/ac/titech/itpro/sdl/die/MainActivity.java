package jp.ac.titech.itpro.sdl.die;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, Runnable {
    private final static String TAG = MainActivity.class.getSimpleName();

    private GLSurfaceView glView;
    private SimpleRenderer renderer;

    private Cube cube;
    private Pyramid pyramid;
    private Prism prism;

    // for timer: automatic change of seek value, and thus rotation of object.
    private final static long SPIN_REFRESH_PERIOD_MS = 33; //assuming 30fps
    private final Timer timer = new Timer();
    private Handler handler = new Handler();

    SeekBar seekBarX, seekBarY, seekBarZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        glView = findViewById(R.id.gl_view);
        seekBarX = findViewById(R.id.seekbar_x);
        seekBarY = findViewById(R.id.seekbar_y);
        seekBarZ = findViewById(R.id.seekbar_z);
        seekBarX.setMax(360);
        seekBarY.setMax(360);
        seekBarZ.setMax(360);
        seekBarX.setOnSeekBarChangeListener(this);
        seekBarY.setOnSeekBarChangeListener(this);
        seekBarZ.setOnSeekBarChangeListener(this);

        renderer = new SimpleRenderer();
        cube = new Cube();
        pyramid = new Pyramid();
        prism = new Prism();
        renderer.setObj(cube);
        glView.setRenderer(renderer);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(MainActivity.this);
            }
        }, 500, SPIN_REFRESH_PERIOD_MS);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        glView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        glView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.menu_cube:
                renderer.setObj(cube);
                break;
            case R.id.menu_pyramid:
                renderer.setObj(pyramid);
                break;
            case R.id.menu_prism:
                renderer.setObj(prism);
                break;
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.seekbar_x:
            renderer.rotateObjX(progress);
            break;
        case R.id.seekbar_y:
            renderer.rotateObjY(progress);
            break;
        case R.id.seekbar_z:
            renderer.rotateObjZ(progress);
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override // called by handler
    public void run() {
        int x = seekBarX.getProgress();
        int y = seekBarY.getProgress();
        int z = seekBarZ.getProgress();
        seekBarX.setProgress((x + 2) % 360,true);
        seekBarY.setProgress((y + 2) % 360,true);
        seekBarZ.setProgress((z + 2) % 360,true);

    }
}
