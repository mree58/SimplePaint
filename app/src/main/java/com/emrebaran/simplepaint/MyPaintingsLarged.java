package com.emrebaran.simplepaint;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyPaintingsLarged extends Activity {
	
	//PhotoViewAttacher mAttacher;
	
	public ImageButton btnSil;
	public static int currentPage;
	public Activity act;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_paintings_larged);
		
		Intent i = getIntent();

	    final int position = i.getExtras().getInt("index");
		btnSil = (ImageButton) findViewById(R.id.btnSil);
		
		final List<ImageView> images = new ArrayList<ImageView>();
	
		for (int ii = 0; ii < MyPaintings.myImageAdapter.getCount(); ii++) {

			Bitmap bm = decodeSampledBitmapFromUri(MyPaintings.myImageAdapter.imgPic.get(ii), 220, 220);
			
			PaintingsSlider img = new PaintingsSlider(this);
			
			img.setImageBitmap(bm) ;
    	     
    		images.add(img);
    		
    		ImagePagerAdapter pageradapter = new ImagePagerAdapter(images);
    		ViewPager viewpager = (ViewPager) findViewById(R.id.pager);
    		viewpager.setAdapter(pageradapter);
    		
    		viewpager.setCurrentItem(position);
    		PageListener pageListener = new PageListener();
    		viewpager.setOnPageChangeListener(pageListener);

    		btnSil.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
    				final AlertDialog.Builder builder = new AlertDialog.Builder(MyPaintingsLarged.this);
    				builder.setMessage(getString(R.string.gallery_delete))
							.setCancelable(false)
		    				.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
		    					public void onClick(DialogInterface dialog, int id) {

									File file = new File(String.valueOf(MyPaintings.listOfImagesPath.get(currentPage)));
									file.delete();
									MyPaintings.listOfImagesPath.remove(currentPage);
									Toast.makeText(getApplicationContext(), getString(R.string.deleted),Toast.LENGTH_SHORT).show();
									currentPage=0;
									onStop();
									onDestroy();
		    					}
		    				})
		    				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
		    					public void onClick(DialogInterface dialog, int id) {
		    						dialog.cancel();
		    					}
		    				}); 
		    				
		    				AlertDialog alert = builder.create();
		    				alert.show();
					
					

					

				}				
    		});
		}
	}

	public Bitmap decodeFile(String path) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);
			
			// The new size we want to scale to
			final int REQUIRED_SIZE =500;
			
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o.inSampleSize = calculateInSampleSize(o, 2048, 2048);
			return BitmapFactory.decodeFile(path, o2);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
				// Raw height and width of image
				final int height = options.outHeight;
				final int width = options.outWidth;
				int inSampleSize = 1;

				if (height > reqHeight || width > reqWidth) {
					if (width > height) {
						inSampleSize = Math.round((float) height
								/ (float) reqHeight);
					} else {
						inSampleSize = Math.round((float) width / (float) reqWidth);
					}
				}

				return inSampleSize;
			}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 1024, 1024);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}
	
    @Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	private static class PageListener extends SimpleOnPageChangeListener{
            public void onPageSelected(int position) {
                   currentPage = position;
        }
    }
}
