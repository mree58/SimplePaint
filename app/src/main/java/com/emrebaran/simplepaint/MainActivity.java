package com.emrebaran.simplepaint;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void colorChanged(int color) {
        //mPaint.setColor(color);
    }


}