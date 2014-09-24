package com.example.pmalviya.customimagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import touchimageview.TouchImageView;

import com.example.pmalviya.customimagesearch.R;
import com.example.pmalviya.customimagesearch.R.id;
import com.example.pmalviya.customimagesearch.R.layout;
import com.example.pmalviya.customimagesearch.R.menu;
import com.example.pmalviya.customimagesearch.models.ImageResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class ImageDisplayActivity extends Activity {

	private ImageResult result;
	private TouchImageView ivImageResult;
	private ShareActionProvider miShareAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		// hide action bar
		// getActionBar().hide();

		result = (ImageResult) getIntent().getSerializableExtra("result");
		ivImageResult = (TouchImageView) findViewById(R.id.ivImageResult);
		// calculate the aspectRatio
		float aspectRatio = result.getWidth() / result.getHeight();
		// Get the screen width
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int calculatedHeight = (int) (screenWidth/ aspectRatio);
		ivImageResult.getLayoutParams().height = calculatedHeight;
		ivImageResult.getLayoutParams().width = screenWidth;
		//resize(screenWidth, calculatedHeight)
		
		Picasso.with(this).load(result.getFullUrl()).resize(screenWidth, calculatedHeight).into(ivImageResult, new Callback() {

			@Override
			public void onError() {
				Toast.makeText(getBaseContext(), "Could't setup sharing for the image", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess() {
				if (miShareAction == null) {
					setupShareIntent();
				}

			}

		}) ;
	}

	// Gets the image URI and setup the associated share intent to hook into the
	// provider
	public void setupShareIntent() {
		// Fetch Bitmap Uri locally
		TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivImageResult);
		Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images
													// section
		// Create share intent as described above
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		shareIntent.setType("image/*");
		// Attach share event to the menu item provider
		miShareAction.setShareIntent(shareIntent);
	}

	public Uri getLocalBitmapUri(TouchImageView imageView) {
		// Extract Bitmap from ImageView drawable
		Drawable drawable = imageView.getDrawable();
		Bitmap bmp = null;
		if (drawable instanceof BitmapDrawable) {
			bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else {
			return null;
		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}

	public void onEmailClick(MenuItem mi) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		// Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));
		Uri uri = getLocalBitmapUri(ivImageResult);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
		startActivity(Intent.createChooser(shareIntent, "Share image using"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);

		// Fetch reference to the share action provider
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.miEmail);
		miShareAction = (ShareActionProvider) item.getActionProvider();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
