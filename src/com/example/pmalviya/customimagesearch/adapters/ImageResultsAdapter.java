package com.example.pmalviya.customimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pmalviya.customimagesearch.R;
import com.example.pmalviya.customimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image, images);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
		}
		ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		
		ivImage.setImageResource(0);
		tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
		Picasso.with(getContext()).load(imageInfo.getTbUrl()).into(ivImage);
		return convertView;
	}

}
