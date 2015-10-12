package com.sky.adapter;

import java.util.List;

import com.sky.R;
import com.sky.adapter.base.BaseListAdapter;
import com.sky.adapter.base.ViewHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GridViewAdapter extends BaseListAdapter<Bitmap> {

	private ImageView item_img;

	public GridViewAdapter(Context context, List<Bitmap> list) {
		super(context, list);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.grid_img_items, parent, false);
		if (convertView != null) {
			item_img = ViewHolder.get(convertView, R.id.item_img);
			item_img.setImageBitmap(list.get(position));
		}
		return convertView;
	}

}
