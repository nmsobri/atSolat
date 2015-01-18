package com.slier.atsolat.activity;

import com.slier.atsolat.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends ActionBarActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread splashThread = new Thread(this);
        splashThread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
            this.startActivity(new Intent("com.slier.atsolat.MAIN"));
            this.finish();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
