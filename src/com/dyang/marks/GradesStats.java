package com.dyang.marks;

import java.util.List;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.adapters.CourseAdapter;
import com.dyang.marks.utils.DatabaseHandler;

public class GradesStats extends SherlockActivity implements ActionBar.OnNavigationListener {

	private List<CourseObj> courseList;
	private RelativeLayout gradesStatsRoot;
	private TabHost tabHost;
	private int course_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades_stats);

		DatabaseHandler db = new DatabaseHandler(this);
		courseList = db.getAllCourses();
		db.close();

		CourseAdapter list = new CourseAdapter(this, R.layout.sherlock_spinner_item, courseList);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);

		if (courseList.size() > 0)
			setCourse_id(courseList.get(getSupportActionBar().getSelectedNavigationIndex()).getId());

		gradesStatsRoot = (RelativeLayout) findViewById(R.id.gradesStatsRoot);
		gradesStatsRoot.setBackgroundColor(Color.DKGRAY);

		tabHost = (TabHost) LayoutInflater.from(getBaseContext()).inflate(R.layout.course_tabhost, null);
		LocalActivityManager mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);
		tabHost.setup(mlam);

		TabSpec overviewSpec = tabHost.newTabSpec("Overview");
		overviewSpec.setIndicator("Overview");
		Intent overviewIntent = new Intent(this, StatsOverview.class);
		overviewSpec.setContent(overviewIntent);
		tabHost.addTab(overviewSpec);

		TabSpec gradesSpec = tabHost.newTabSpec("Grades");
		gradesSpec.setIndicator("Grades");
		Intent gradesIntent = new Intent(this, StatsGrades.class);
		gradesSpec.setContent(gradesIntent);
		tabHost.addTab(gradesSpec);

		TabSpec analysisSpec = tabHost.newTabSpec("Analysis");
		analysisSpec.setIndicator("Analysis");
		Intent analysisIntent = new Intent(this, StatsAnalysis.class);
		analysisSpec.setContent(analysisIntent);
		tabHost.addTab(analysisSpec);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 3, 0, 0);
		gradesStatsRoot.addView(tabHost, lp);
		
		Button button = new Button(this);
		button.setText("test");
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setAction("com.dyang.marks.NIF");
				sendBroadcast(i);
			}
			
		});
		gradesStatsRoot.addView(button);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		setCourse_id(courseList.get(itemPosition).getId());
		//refreshOverview();
		return true;
	}

	private void refreshOverview() {
		Intent i = new Intent();
		i.setAction("test.intent.action.LOC.TEST");
		sendBroadcast(i);
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
}
