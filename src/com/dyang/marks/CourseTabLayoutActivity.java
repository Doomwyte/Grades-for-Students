package com.dyang.marks;

import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

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
	public int course_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_tabhost);
		parentActivity = this;
		tabHost = getTabHost();
		setCourse_id(0);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			setCourse_id(bundle.getInt("editMode"));
			resetTab();
		}

		setTab();

		//DatabaseHandler dh = new DatabaseHandler(this);
		//dh.deleteAllCourses();
		//dh.close();
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

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void reload() {
		startActivity(getIntent());
		finish();
	}

	public void resetTab() {
		tabHost.getTabWidget().removeAllViews();
	}

	public void setTab() {
		TabSpec coursespec = tabHost.newTabSpec("General");
		coursespec.setIndicator("General");
		Intent courseIntent = new Intent(this, EnterCourses.class);
		coursespec.setContent(courseIntent);
		tabHost.addTab(coursespec);

		TabSpec categoryspec = tabHost.newTabSpec("Breakdown");
		categoryspec.setIndicator("Breakdown");
		Intent categoryIntent = new Intent(this, EnterCategories.class);
		categoryspec.setContent(categoryIntent);
		tabHost.addTab(categoryspec);
	}

}
