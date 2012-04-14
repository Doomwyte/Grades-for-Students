package com.dyang.marks;

import com.dyang.marks.adapters.CategoryAdapter;
import com.dyang.marks.adapters.CourseAdapter;
import com.dyang.marks.courseObj.CategoryObj;
import com.dyang.marks.courseObj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EnterGrades extends Activity {

	private Spinner courseSpinner;
	private Spinner categorySpinner;
	private TextView gradesLabel;
	private Button addMore;
	private LinearLayout enterGradesRoot;
	private DatabaseHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_grade);

		db = new DatabaseHandler(this);

		courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		gradesLabel = (TextView) findViewById(R.id.gradesLabel);
		enterGradesRoot = (LinearLayout) findViewById(R.id.enterGradesRoot);
		addMore = (Button) findViewById(R.id.addMore);

		courseSpinner.setAdapter(new CourseAdapter(this, R.layout.my_simple_spinner_dropdown_item, db.getAllCourses()));
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CourseObj selectedCourse = ((CourseObj) courseSpinner.getSelectedItem());
				categorySpinner.setAdapter(new CategoryAdapter(EnterGrades.this,
						R.layout.my_simple_spinner_dropdown_item, db.getAllCategories(selectedCourse.getId())));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int counter = 1;
				gradesLabel.setText(((CategoryObj) categorySpinner.getSelectedItem()).getCategoryName() + " "
						+ counter++);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		addMore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LinearLayout gradesRow = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(
						R.layout.grades_row, null);
				enterGradesRoot.addView(gradesRow);
			}

		});

	}
}
