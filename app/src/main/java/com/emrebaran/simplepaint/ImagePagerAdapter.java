package com.emrebaran.simplepaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
	Context context;

	List<String> itemList=new ArrayList<String>();
	private List<ImageView> images;
	 
    public ImagePagerAdapter(List<ImageView> images) {
        this.images = images;
    }
    
	public ImagePagerAdapter(Context context,List<String>  adapterList)
	{
	    this.context = context;
	    this.itemList = adapterList;
	}
	
	
	
	ImagePagerAdapter(Context context){
	this.context=context;
	}
	
	@Override
	public int getCount() {
		return images.size();
	}
	 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		 return view == object;
	}
	 
	
	public Object instantiateItem(ViewGroup container, int position) {		
		 ImageView imageView = images.get(position);
	     container.addView(imageView);	     
	     return imageView;
	}
	
	
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
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
	
	 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		 container.removeView(images.get(position));
	}

}
