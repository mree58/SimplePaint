package com.emrebaran.simplepaint;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by mree on 01.11.2016.
 */
public class Login  extends Activity{

    DisplayMetrics metrics;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);



        //çözünürlüðe göre popup ayarlamak için
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);


    }

//clicks

    public void loginOnClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_new :
                Intent intentPaint = new Intent(Login.this,MyPaint.class);
                startActivity(intentPaint);
                break;
            case R.id.btn_login_gallery :
                Intent intentGallery = new Intent(Login.this,MyPaintings.class);
                startActivity(intentGallery);
                break;
            case R.id.btn_login_about :
                showPopup(Login.this);
                break;
            case R.id.btn_login_rate :

                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }

                break;
            case R.id.btn_login_quit :
                //android.os.Process.killProcess(android.os.Process.myPid());
                //System.exit(1);
                finish();
                break;

        }
    }



    private PopupWindow pw;
    private void showPopup(final Activity context) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_about, (ViewGroup) findViewById(R.id.popup_1));

            float popupWidth = 350*metrics.scaledDensity;
            float popupHeight = 460*metrics.scaledDensity;

            pw = new PopupWindow(context);
            pw.setContentView(layout);
            pw.setWidth((int)popupWidth);
            pw.setHeight((int)popupHeight);
            pw.setFocusable(true);

            Point p = new Point();
            p.x = 50;
            p.y = 50;

            int OFFSET_X = -50;
            int OFFSET_Y = (int)(80*metrics.scaledDensity);


            pw.showAtLocation(layout, Gravity.TOP, p.x + OFFSET_X, p.y + OFFSET_Y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //double click to exit
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.app_exit, Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 1500);
    }


}
