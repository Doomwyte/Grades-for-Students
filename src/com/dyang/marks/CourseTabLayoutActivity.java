package com.dyang.marks;

import com.dyang.marks.courseObj.CourseObj;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CourseTabLayoutActivity extends TabActivity {

	public TabHost tabHost;
	private boolean inserted;
	public CourseObj courseObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_tabhost);

		tabHost = getTabHost();
		setInserted(false);

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
	
	public void setCourseObj(CourseObj courseObj){
		this.courseObj = courseObj;
	}
	
	public CourseObj getCourseObj(){
		return courseObj;
	}

	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}

	public boolean isInserted() {
		return inserted;
	}

}
