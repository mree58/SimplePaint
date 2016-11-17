package com.emrebaran.simplepaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mree on 08.11.2016.
 */

public class MyPaint extends Activity implements ColorPickerDialog.OnColorChangedListener {

    private DrawingView drawView;

    ImageButton btnColorPalette;

    int lastColor=0xFF0000FF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.my_paint);

        drawView = (DrawingView)findViewById(R.id.drawing);

        btnColorPalette = (ImageButton)findViewById(R.id.btn_color_palette);
        btnColorPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog(MyPaint.this, MyPaint.this,lastColor).show();
            }
        });

        drawView.setBrushSize(5);

    }

    public void colorChanged(int color) {

        drawView.setNewColor(color);
        lastColor=color;
    }



    public void paintOnClick(View v) {

        switch (v.getId()){
            case R.id.btn_paint_brush :
                drawView.setErase(false);
                drawView.setNewColor(lastColor);
                break;
            case R.id.btn_paint_thickness :

                final Dialog brushDialog = new Dialog(this);
                brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                brushDialog.setContentView(R.layout.thickness_chooser);


                final TextView txtBrushTickness = (TextView) brushDialog.findViewById(R.id.txt_brush_thickness);
                txtBrushTickness.setText(String.valueOf(drawView.getLastBrushSize()));



                SeekBar sbBrushThickness = (SeekBar) brushDialog.findViewById(R.id.seekbar_brush_thickness);
                sbBrushThickness.setProgress((drawView.getLastBrushSize()));

                sbBrushThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        txtBrushTickness.setText(String.valueOf(progress));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        drawView.setBrushSize(seekBar.getProgress());
                        drawView.setLastBrushSize(seekBar.getProgress());
                        brushDialog.dismiss();
                    }
                });

                brushDialog.show();

                break;
            case R.id.btn_eraser :
                drawView.setErase(true);
                drawView.setBrushSize((drawView.getLastBrushSize()));

                break;
            case R.id.btn_new :

                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setMessage("Yeni Bir Çizime Başlamak İstiyor Musunuz ?");
                newDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        drawView.startNew();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                newDialog.show();

                break;
            case R.id.btn_save :
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
                saveDialog.setMessage("Bu Çizim Notlarınızın Arasına Kaydedilsin Mi ?");
                saveDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){

                        drawView.setDrawingCacheEnabled(true);
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SimplePaint/";

                        File folder = new File(path);
                        folder.mkdirs();

                        String dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
                        File file = new File(path, "SP_"+dateFormat+".png");

                        Bitmap bitmap = drawView.getDrawingCache();
                        try {
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                            stream.close();
                            Toast.makeText(getApplicationContext(), "Kaydedildi !", Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        drawView.setDrawingCacheEnabled(false);
                        drawView.destroyDrawingCache();
                    }
                });
                saveDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                saveDialog.show();
                break;
        }
    }






    /*
    @Override
    public void onClick(View view){

        ImageButton imgView;
        String color;

        if(view.getId()==R.id.btn_new){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setMessage("Yeni Bir Çizime Başlamak İstiyor Musunuz ?");
            newDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.btn_save){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setMessage("Bu Çizim Notlarınızın Arasına Kaydedilsin Mi ?");
            saveDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.setDrawingCacheEnabled(true);


                    String imgSaved = "null" ; /* MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing"); */

            /*        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SimplePaint/";


                    Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();


                    File folder = new File(path);
                    folder.mkdirs();

                    String dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());


                    File file = new File(path, "SP_"+dateFormat+".png");


                    Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();

                    Bitmap bitmap = drawView.getDrawingCache();
                    try {
                        FileOutputStream stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        stream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    drawView.setDrawingCacheEnabled(false);



                /*    if(imgSaved!=null){
                        Toast.makeText(getApplicationContext(), "Galeriye Kaydedildi !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Hata! Yeni Not Kaydedilemedi !", Toast.LENGTH_SHORT).show();
                    } */ /*
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.btn_normal){

            drawView.setErase(false);
            drawView.setBrushSize(smallBrush);
            drawView.setLastBrushSize(smallBrush);

            btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            btnBold.setBackgroundResource(R.drawable.ic_bold);
            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
        }
        else if(view.getId()==R.id.btn_bold){

            drawView.setErase(false);
            drawView.setBrushSize(largeBrush);
            drawView.setLastBrushSize(largeBrush);

            btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            btnNormal.setBackgroundResource(R.drawable.ic_normal);
            btnEraser.setBackgroundResource(R.drawable.ic_eraser);

        }

        else if(view.getId()==R.id.btn_eraser){

            drawView.setErase(true);
            drawView.setBrushSize(largeBrush);

            btnBold.setBackgroundResource(R.drawable.ic_bold);
            btnNormal.setBackgroundResource(R.drawable.ic_normal);
            btnEraser.setBackgroundResource(R.drawable.ic_eraser_selected);


            btnBlue.setBackgroundResource(R.drawable.ic_blue);
            btnBlack.setBackgroundResource(R.drawable.ic_black);
            btnRed.setBackgroundResource(R.drawable.ic_red);
            btnYellow.setBackgroundResource(R.drawable.ic_yellow);
            btnGreen.setBackgroundResource(R.drawable.ic_green);

        }

        else if(view.getId()==R.id.btn_blue){

            imgView = (ImageButton)view;
            color = view.getTag().toString();
            drawView.setColor(color);

            new ColorPickerDialog(this, this, color).show();




            imgView.setBackgroundResource(R.drawable.ic_blue_selected);
            btnBlack.setBackgroundResource(R.drawable.ic_black);
            btnRed.setBackgroundResource(R.drawable.ic_red);
            btnYellow.setBackgroundResource(R.drawable.ic_yellow);
            btnGreen.setBackgroundResource(R.drawable.ic_green);

            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
            if(drawView.getLastBrushSize()==largeBrush)
            {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            }
            else
            {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);

                btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            }



        }

        else if(view.getId()==R.id.btn_black){

            imgView = (ImageButton)view;
            color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setBackgroundResource(R.drawable.ic_black_selected);
            btnBlue.setBackgroundResource(R.drawable.ic_blue);
            btnRed.setBackgroundResource(R.drawable.ic_red);
            btnYellow.setBackgroundResource(R.drawable.ic_yellow);
            btnGreen.setBackgroundResource(R.drawable.ic_green);

            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
            if(drawView.getLastBrushSize()==largeBrush)
            {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            }
            else
            {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);

                btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            }


        }

        else if(view.getId()==R.id.btn_red){

            imgView = (ImageButton)view;
            color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setBackgroundResource(R.drawable.ic_red_selected);
            btnBlue.setBackgroundResource(R.drawable.ic_blue);
            btnBlack.setBackgroundResource(R.drawable.ic_black);
            btnYellow.setBackgroundResource(R.drawable.ic_yellow);
            btnGreen.setBackgroundResource(R.drawable.ic_green);

            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
            if(drawView.getLastBrushSize()==largeBrush)
            {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            }
            else
            {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);

                btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            }

        }
        else if(view.getId()==R.id.btn_yellow){

            imgView = (ImageButton)view;
            color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setBackgroundResource(R.drawable.ic_yellow_selected);
            btnBlue.setBackgroundResource(R.drawable.ic_blue);
            btnBlack.setBackgroundResource(R.drawable.ic_black);
            btnRed.setBackgroundResource(R.drawable.ic_red);
            btnGreen.setBackgroundResource(R.drawable.ic_green);

            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
            if(drawView.getLastBrushSize()==largeBrush)
            {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            }
            else
            {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);

                btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            }

        }
        else if(view.getId()==R.id.btn_green){

            imgView = (ImageButton)view;
            color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setBackgroundResource(R.drawable.ic_green_selected);
            btnBlue.setBackgroundResource(R.drawable.ic_blue);
            btnBlack.setBackgroundResource(R.drawable.ic_black);
            btnRed.setBackgroundResource(R.drawable.ic_red);
            btnYellow.setBackgroundResource(R.drawable.ic_yellow);

            btnEraser.setBackgroundResource(R.drawable.ic_eraser);
            if(drawView.getLastBrushSize()==largeBrush)
            {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                btnBold.setBackgroundResource(R.drawable.ic_bold_selected);
            }
            else
            {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);

                btnNormal.setBackgroundResource(R.drawable.ic_normal_selected);
            }
        }
    } */


}