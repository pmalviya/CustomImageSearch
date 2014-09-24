package com.example.pmalviya.customimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.content.ClipData.Item;
import android.os.IBinder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.example.pmalviya.customimagesearch.R;
import com.example.pmalviya.customimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
	
	public static class ViewHolder{
		DynamicHeightImageView ivImage; 
		TextView tvTitle;
	}

	public ImageResultsAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image, images);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = getItem(position);
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder =  new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
			viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.ivImage.setHeightRatio(imageInfo.getHeight()/imageInfo.getWidth());
		viewHolder.ivImage.setHeightRatio(imageInfo.getHeight()/imageInfo.getWidth());
		
		viewHolder.ivImage.setImageResource(0);
		viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
		Picasso.with(getContext()).load(imageInfo.getTbUrl()).fit().into(viewHolder.ivImage);
		return convertView;
	}

}
