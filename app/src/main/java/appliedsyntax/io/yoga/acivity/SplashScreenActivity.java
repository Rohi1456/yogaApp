package appliedsyntax.io.yoga.acivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import appliedsyntax.io.yoga.R;


/**
 * Created by HARI @IPC
 */
public class SplashScreenActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);

        startAnim();
    }

    private void startAnim() {
        splashTread = new Thread() {

            @Override
            public void run() {
                try {
                    int waited = 0;
                    // LanguageSelectActivity screen pause time
                    while (waited < 800) {
                        sleep(60);
                        waited += 60;
                    }
                    Intent i1 = new Intent(SplashScreenActivity.this,ActivityHome.class);
                    startActivity(i1);
                    finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreenActivity.this.finish();
                }
            }
        };
        splashTread.start();
    }
}