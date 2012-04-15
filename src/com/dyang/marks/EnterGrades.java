package com.dyang.marks;

import com.dyang.marks.adapters.CategoryAdapter;
import com.dyang.marks.adapters.CourseAdapter;
import com.dyang.marks.courseObj.CategoryObj;
import com.dyang.marks.courseObj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EnterGrades extends Activity {

	private Spinner courseSpinner;
	private Spinner categorySpinner;
	private Button addMore;
	private LinearLayout gradesContent;
	private LinearLayout enterGradeSpinners;
	private LinearLayout spinnerLabel;
	private LinearLayout gradesContentLabel;
	private DatabaseHandler db;
	private int counter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_grade);

		db = new DatabaseHandler(this);

		courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		spinnerLabel = (LinearLayout) findViewById(R.id.spinnerLabel);
		gradesContentLabel = (LinearLayout) findViewById(R.id.gradesContentLabel);
		gradesContent = (LinearLayout) findViewById(R.id.gradesContent);
		enterGradeSpinners = (LinearLayout) findViewById(R.id.enterGradeSpinners);
		addMore = (Button) findViewById(R.id.addMore);
		counter = 1;

		TextView spinnerLabelText = (TextView) spinnerLabel.getChildAt(0);
		TextView gradesContentLabelText = (TextView) gradesContentLabel.getChildAt(0);

		spinnerLabelText.setText(R.string.courseInfo);
		gradesContentLabelText.setText(R.string.gradesInfo);
		spinnerLabelText.setTextColor(Color.WHITE);
		gradesContentLabelText.setTextColor(Color.WHITE);
		spinnerLabelText.setTextSize(15);
		gradesContentLabelText.setTextSize(15);

		CourseAdapter cAdapter = new CourseAdapter(this, android.R.layout.simple_spinner_item, db.getAllCourses());
		cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		courseSpinner.setAdapter(cAdapter);
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CourseObj selectedCourse = ((CourseObj) courseSpinner.getSelectedItem());
				CategoryAdapter caAdapter = new CategoryAdapter(EnterGrades.this, android.R.layout.simple_spinner_item,
						db.getAllCategories(selectedCourse.getId()));
				caAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				categorySpinner.setAdapter(caAdapter);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				rowManager(true);
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		addMore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				rowManager(false);
			}

		});

	}

	public void rowManager(boolean newCategory) {
		if (newCategory) {
			counter = 1;
			gradesContent.removeViews(0, gradesContent.getChildCount() - 1);
		}
		LinearLayout gradesRow = (LinearLayout) LayoutInflater.from(getBaseContext())
				.inflate(R.layout.grades_row, null);
		TextView child = (TextView) gradesRow.getChildAt(0);
		child.setText(((CategoryObj) categorySpinner.getSelectedItem()).getCategoryName() + " " + counter++);
		child.setTextColor(Color.BLACK);
		gradesContent.addView(gradesRow, gradesContent.getChildCount() - 1);
	}
}
