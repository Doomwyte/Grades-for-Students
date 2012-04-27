package com.dyang.marks.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CourseListAdapter extends ArrayAdapter<String> {

	public CourseListAdapter(Context context, int textViewResourceId, List<String> courses) {
		super(context, textViewResourceId, courses);
	}

}
