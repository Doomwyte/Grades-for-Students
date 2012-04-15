package com.dyang.marks.adapters;

import java.util.List;

import com.dyang.marks.Obj.CategoryObj;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<CategoryObj> {

	private List<CategoryObj> categoryItems;

	public CategoryAdapter(Context context, int textViewResourceId, List<CategoryObj> categories) {
		super(context, textViewResourceId, categories);
		categoryItems = categories;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		CategoryObj item = categoryItems.get(position);
		((TextView) v).setText(item.getCategoryName() + " (" + item.getWeight() + "%)");
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = super.getDropDownView(position, convertView, parent);
		CategoryObj item = categoryItems.get(position);
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		tv.setText(item.getCategoryName() + " (" + item.getWeight() + "%)");
		v.bringToFront();
		return v;
	}

}
