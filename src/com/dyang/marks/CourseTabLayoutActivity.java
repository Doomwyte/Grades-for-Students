package com.dyang.marks;

import com.dyang.marks.courseObj.CourseObj;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CourseTabLayoutActivity extends TabActivity {

	public TabHost tabHost;
	public CourseObj courseObj;
	public static Activity parentActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_tabhost);
		parentActivity = this;

		tabHost = getTabHost();

		// Tab for Photos
		TabSpec coursespec = tabHost.newTabSpec("General");
		// setting Title and Icon for the Tab
		coursespec.setIndicator("General");
		Intent courseIntent = new Intent(this, EnterCourses.class);
		coursespec.setContent(courseIntent);

		// Tab for Songs
		TabSpec categoryspec = tabHost.newTabSpec("Breakdown");
		categoryspec.setIndicator("Breakdown");
		Intent categoryIntent = new Intent(this, EnterCategories.class);
		categoryspec.setContent(categoryIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(coursespec); // Adding course tab
		tabHost.addTab(categoryspec); // Adding category tab

	}

	public void switchTab(int tab) {
		tabHost.setCurrentTab(tab);
	}

	public void setCourseObj(CourseObj courseObj) {
		this.courseObj = courseObj;
	}

	public CourseObj getCourseObj() {
		return courseObj;
	}

	public void reload() {
		startActivity(getIntent());
		finish();
	}

}
