package com.dyang.marks;

import com.dyang.marks.courseObj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EnterCourses extends Activity {

	private EditText courseNameInput;
	private EditText courseCodeInput;
	private CheckBox countGPABox;
	private RelativeLayout setupContent;
	private Resources resources;
	private float dipValue;
	private Button next;
	private CourseTabLayoutActivity parentActivity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_courses);
		setupContent = (RelativeLayout) findViewById(R.id.SetupContent);
		next = (Button) findViewById(R.id.next);
		resources = getResources();
		inputCourseSettings();
	}

	public void inputCourseSettings() {
		setupContent.removeAllViews();

		parentActivity = (CourseTabLayoutActivity) getParent();

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics());

		TextView courseName = new TextView(this);
		courseName.setText(R.string.courseName);
		courseName.setTextSize(dipValue);
		courseName.setTextColor(Color.BLACK);
		courseNameInput = new EditText(this);
		courseNameInput.setSingleLine();
		courseNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		TextView courseCode = new TextView(this);
		courseCode.setText(R.string.courseCode);
		courseCode.setTextSize(dipValue);
		courseCode.setTextColor(Color.BLACK);
		courseCodeInput = new EditText(this);
		courseCodeInput.setSingleLine();
		courseCodeInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		TextView countGPA = new TextView(this);
		countGPA.setText(R.string.countGPA);
		countGPA.setTextSize(dipValue);
		countGPA.setTextColor(Color.BLACK);
		countGPABox = new CheckBox(this);

		TableLayout courseContent = new TableLayout(this);
		TableRow courseNameRow = new TableRow(this);
		TableRow courseCodeRow = new TableRow(this);
		TableRow gpaRow = new TableRow(this);

		TableRow.LayoutParams rowLp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rowLp.topMargin = 20;

		courseNameRow.addView(courseName);
		courseNameRow.addView(courseNameInput);

		courseCodeRow.addView(courseCode);
		courseCodeRow.addView(courseCodeInput);

		gpaRow.addView(countGPA);
		gpaRow.addView(countGPABox);

		courseContent.setStretchAllColumns(true);
		courseContent.addView(courseNameRow);
		courseContent.addView(courseCodeRow);
		courseContent.addView(gpaRow);

		setupContent.addView(courseContent, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, resources.getDisplayMetrics());

		next.setTextSize(dipValue);

		RelativeLayout.LayoutParams addMoreCoursesLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addMoreCoursesLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, resources.getDisplayMetrics());

		addMoreCoursesLp.topMargin = (int) dipValue;
		next.setText(R.string.next);
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				parentActivity.switchTab(1);
			}

		});

		parentActivity.tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String arg0) {
				if (parentActivity.getTabHost().getCurrentTab() == 1)
					setCourseInfo(0);
			}

		});

	}

	public void setCourseInfo(int id) {
		DatabaseHandler db = new DatabaseHandler(this);

		// If id==0, means adding NEW course
		// Else, means updating EXISTING course
		if (id == 0)
			id = db.getCoursesCount() + 1;

		// Sending course object to tab host
		CourseObj courseObj = new CourseObj(id, courseNameInput.getText().toString(), courseCodeInput
				.getText().toString().toUpperCase(), new Boolean(countGPABox.isSelected()).toString());
		parentActivity.setCourseObj(courseObj);

		db.close();
	}

	public void reload() {
		startActivity(getIntent());
		finish();
	}

}
