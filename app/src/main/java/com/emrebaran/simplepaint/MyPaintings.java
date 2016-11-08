package com.emrebaran.simplepaint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MyPaintings extends Activity {
	
	private static final String PHOTO_DIRECTORY_NAME = "/SimplePaint/";
	public static String AdsPhotos_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + PHOTO_DIRECTORY_NAME;
	static List<String> listOfImagesPath;
	
	ImageButton btnTamam;
	GridView grdFotograflar;
	DisplayMetrics metrics;
	TextView txtCount;
	public static ImageAdapter myImageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_paintings);
		
		Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		

		btnTamam = (ImageButton)findViewById(R.id.btnTamam);
		btnTamam.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {			
			finish();				
			}
		});

		grdFotograflar = (GridView) findViewById(R.id.grdFotograflar);

		
		listOfImagesPath = RetriveCapturedImagePath();
		if(listOfImagesPath!=null){
			 myImageAdapter = new ImageAdapter(this,listOfImagesPath);
			 grdFotograflar.setAdapter(myImageAdapter);
		}



		

		grdFotograflar.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          /*  yenile();
            if (position<listOfImagesPath.size()){ */
 			Intent i = new Intent(getApplicationContext(), MyPaintingsLarged.class);
 			i.putExtra("id", String.valueOf(listOfImagesPath.get(position)));
 			i.putExtra("index", position);
 			startActivity(i);
 			yenile();}
            
        });


	}
		
	public void yenile(){
		
		listOfImagesPath = RetriveCapturedImagePath();
		myImageAdapter = new ImageAdapter(this,listOfImagesPath);
		grdFotograflar.setAdapter(myImageAdapter);

		
	}
	
	
	private static List<String> RetriveCapturedImagePath() {
		List<String> tFileList = new ArrayList<String>();

		File f = new File(AdsPhotos_ImagePath);

		if (f.exists()) {
			File[] files=f.listFiles();
			Arrays.sort(files);
 
			for(int i=0; i<files.length; i++){
				File file = files[i];
				if(file.isDirectory())
					continue;
				tFileList.add(file.getPath());
			}
		}
		return tFileList;
	}
	

public class ImageAdapter extends BaseAdapter
	{
		private Context context;
		List<String> imgPic;
		public ImageAdapter(Context c, List<String> thePic)
		{
			context = c;
			imgPic = thePic;
		}
		public int getCount() {
			if(imgPic != null)
				return imgPic.size();
			else
				return 0;
		}
 
		public Object getItem(int position) {
			return position;
		}
 
		public long getItemId(int position) {
			return position;
		}

   public View getView(int position, View convertView, ViewGroup parent)
   {   
	ImageView imageView;
	BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	bfOptions.inJustDecodeBounds = false;
	bfOptions.inSampleSize=4;
	bfOptions.inDither=false;
	bfOptions.inPurgeable=true;
	bfOptions.inInputShareable=true;
	bfOptions.inTempStorage=new byte[32 * 1024]; //32*1024
	if (convertView == null) {
		imageView = new ImageView(context);
		imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		imageView.setPadding(0, 0, 0, 0);
	} else {
		imageView = (ImageView) convertView;
	}
	FileInputStream fs = null;
	Bitmap bm;
	try {
		fs = new FileInputStream(new File(imgPic.get(position).toString()));
 
		if(fs!=null) {
			bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
			imageView.setImageBitmap(bm);
			imageView.setId(position);
			imageView.setBackgroundResource(R.drawable.design_image_rectangle);
			imageView.setLayoutParams(new GridView.LayoutParams((int)(200*(metrics.scaledDensity/1.3)), (int) (200*metrics.scaledDensity)));}
	} catch (IOException e) {
		e.printStackTrace();
	} finally{
		if(fs!=null) {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	return imageView;
	}
   }
	
	
	
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		yenile();
		}

}
