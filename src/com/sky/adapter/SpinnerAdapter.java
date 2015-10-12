package com.sky.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sky.R;
import com.sky.adapter.base.BaseListAdapter;
import com.sky.adapter.base.ViewHolder;
import com.sky.bean.Tags;

public class SpinnerAdapter extends BaseListAdapter<Tags> {

	private TextView textView;

	public SpinnerAdapter(Context context, List<Tags> list) {
		super(context, list);
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.tag_spinner_item, parent,
				false);
		textView = ViewHolder.get(convertView, R.id.tag_string);
		textView.setText(list.get(position).getValue());
		return convertView;
	}
}
