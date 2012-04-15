package com.dyang.marks.adapters;

import java.util.List;

import com.dyang.marks.Obj.CourseObj;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<CourseObj> {

	private List<CourseObj> courseItems;

	public CourseAdapter(Context context, int textViewResourceId, List<CourseObj> courses) {
		super(context, textViewResourceId, courses);
		courseItems = courses;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		CourseObj item = courseItems.get(position);
		if (!item.getCode().equals("999"))
			((TextView) v).setText(item.getName() + " (" + item.getCode() + ")");
		else
			((TextView) v).setText(item.getName());
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = super.getDropDownView(position, convertView, parent);
		CourseObj item = courseItems.get(position);
		TextView tv = (TextView) v.findViewById(android.R.id.text1);
		if (!item.getCode().equals("999"))
			tv.setText(item.getName() + " (" + item.getCode() + ")");
		else
			tv.setText(item.getName());
		return v;
	}

}
