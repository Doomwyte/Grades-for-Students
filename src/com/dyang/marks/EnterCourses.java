package com.dyang.marks;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EnterCourses extends SherlockFragment {

	private EditText courseNameInput;
	private EditText courseCodeInput;
	private CheckBox countGPABox;
	private RelativeLayout setupContent;
	private Resources resources;
	private float dipValue;
	private CourseFragmentActivity parentActivity;
	private int editCourseId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    
	    View fragView = inflater.inflate(R.layout.enter_courses, container, false);
	    
        setupContent = (RelativeLayout) fragView.findViewById(R.id.SetupContent);
        resources = getResources();
        parentActivity = (CourseFragmentActivity) getActivity();
        editCourseId = parentActivity.getCourse_id();
        if (editCourseId == 0) {
            inputCourseSettings(false);
        } else {
            inputCourseSettings(true);
        }

	    return fragView;
	}
	
	/*public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_courses);

	}*/

	public void inputCourseSettings(final boolean edit) {

		CourseObj course = null;
		if (edit) {
			DatabaseHandler db = new DatabaseHandler(getActivity());
			course = db.getCourse(editCourseId);
			db.close();
		}

		setupContent.removeAllViews();

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics());

		TextView courseName = new TextView(getActivity());
		courseName.setText(R.string.courseName);
		courseName.setTextSize(dipValue);
		courseName.setTextColor(Color.BLACK);
		courseNameInput = new EditText(getActivity());
		courseNameInput.setSingleLine();
		courseNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		TextView courseCode = new TextView(getActivity());
		courseCode.setText(R.string.courseCode);
		courseCode.setTextSize(dipValue);
		courseCode.setTextColor(Color.BLACK);
		courseCodeInput = new EditText(getActivity());
		courseCodeInput.setSingleLine();
		courseCodeInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		TextView countGPA = new TextView(getActivity());
		countGPA.setText(R.string.countGPA);
		countGPA.setTextSize(dipValue);
		countGPA.setTextColor(Color.BLACK);
		countGPABox = new CheckBox(getActivity());

		if (edit) {
			courseNameInput.setText(course.getName());
			courseCodeInput.setText(course.getCode());
			countGPABox.setChecked(Boolean.valueOf(course.getCountAsGpa()));
		}

		TableLayout courseContent = new TableLayout(getActivity());
		TableRow courseNameRow = new TableRow(getActivity());
		TableRow courseCodeRow = new TableRow(getActivity());
		TableRow gpaRow = new TableRow(getActivity());

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

		RelativeLayout.LayoutParams addMoreCoursesLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addMoreCoursesLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, resources.getDisplayMetrics());

		addMoreCoursesLp.topMargin = (int) dipValue;

	}

	public void setCourseInfo(int id) {
		DatabaseHandler db = new DatabaseHandler(getActivity());
		// If id==0, means adding NEW course
		// Else, means updating EXISTING course
		if (id == 0)
			id = db.getCoursesCount() + 1;
		
		db.close();

		// Sending course object to tab host
		CourseObj courseObj = new CourseObj(id, courseNameInput.getText().toString(), courseCodeInput.getText()
				.toString().toUpperCase(), new Boolean(countGPABox.isChecked()).toString());
		parentActivity.setCourseObj(courseObj);
	}
	
	public void setEditCourseId(int editCourseId){
	    this.editCourseId = editCourseId;
	}
	
	public int getEditCourseId(){
	    return editCourseId;
	}

}
