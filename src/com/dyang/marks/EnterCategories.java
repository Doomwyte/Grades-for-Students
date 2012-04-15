package com.dyang.marks;

import java.math.BigDecimal;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EnterCategories extends Activity {

	private EditText courseCategory1Input;
	private EditText courseCategory2Input;
	private EditText courseCategory3Input;
	private EditText courseCategory4Input;
	private EditText courseCategory1Weighting;
	private EditText courseCategory2Weighting;
	private EditText courseCategory3Weighting;
	private EditText courseCategory4Weighting;
	private RelativeLayout setupContent;
	private RelativeLayout setupFooter;
	private Resources resources;
	private float dipValue;
	private Button next;
	private CourseTabLayoutActivity parentActivity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_courses);
		setupContent = (RelativeLayout) findViewById(R.id.SetupContent);
		setupFooter = (RelativeLayout) findViewById(R.id.SetupFooter);
		next = (Button) findViewById(R.id.next);
		resources = getResources();
		parentActivity = (CourseTabLayoutActivity) this.getParent();
		inputCategorySettings();
	}

	public void inputCategorySettings() {
		setupContent.removeAllViews();

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics());

		courseCategory1Input = new EditText(this);
		courseCategory2Input = new EditText(this);
		courseCategory3Input = new EditText(this);
		courseCategory4Input = new EditText(this);

		courseCategory1Input.setSingleLine();
		courseCategory2Input.setSingleLine();
		courseCategory3Input.setSingleLine();
		courseCategory4Input.setSingleLine();

		courseCategory1Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		courseCategory2Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		courseCategory3Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		courseCategory4Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		courseCategory1Weighting = new EditText(this);
		courseCategory2Weighting = new EditText(this);
		courseCategory3Weighting = new EditText(this);
		courseCategory4Weighting = new EditText(this);

		courseCategory1Input.setText(R.string.assignments);
		courseCategory2Input.setText(R.string.midterms);
		courseCategory3Input.setText(R.string.finals);

		courseCategory1Weighting.setHint(R.string.thirtypercent);
		courseCategory2Weighting.setHint(R.string.thirtypercent);
		courseCategory3Weighting.setHint(R.string.thirtypercent);
		courseCategory4Weighting.setHint(R.string.thirtypercent);

		courseCategory1Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
		courseCategory2Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
		courseCategory3Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
		courseCategory4Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);

		TextView courseCategories = new TextView(this);
		courseCategories.setText(R.string.courseCategories);
		courseCategories.setTextSize(dipValue);
		courseCategories.setTextColor(Color.BLACK);
		courseCategories.setGravity(Gravity.CENTER);

		TextView courseCategoriesWeighting = new TextView(this);
		courseCategoriesWeighting.setText(R.string.weighting);
		courseCategoriesWeighting.setTextSize(dipValue);
		courseCategoriesWeighting.setTextColor(Color.BLACK);
		courseCategoriesWeighting.setGravity(Gravity.CENTER);

		TableLayout courseContent = new TableLayout(this);
		TableRow courseCategory = new TableRow(this);
		TableRow courseCategory1 = new TableRow(this);
		TableRow courseCategory2 = new TableRow(this);
		TableRow courseCategory3 = new TableRow(this);
		TableRow courseCategory4 = new TableRow(this);

		TableRow.LayoutParams rowLp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rowLp.topMargin = 20;

		courseCategory.addView(courseCategories, rowLp);
		courseCategory.addView(courseCategoriesWeighting, rowLp);
		courseCategory1.addView(courseCategory1Input);
		courseCategory1.addView(courseCategory1Weighting);
		courseCategory2.addView(courseCategory2Input);
		courseCategory2.addView(courseCategory2Weighting);
		courseCategory3.addView(courseCategory3Input);
		courseCategory3.addView(courseCategory3Weighting);
		courseCategory4.addView(courseCategory4Input);
		courseCategory4.addView(courseCategory4Weighting);

		courseContent.setStretchAllColumns(true);
		courseContent.addView(courseCategory);
		courseContent.addView(courseCategory1);
		courseContent.addView(courseCategory2);
		courseContent.addView(courseCategory3);
		courseContent.addView(courseCategory4);

		setupContent.addView(courseContent, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, resources.getDisplayMetrics());

		Button addMoreCourses = new Button(this);
		addMoreCourses.setText(R.string.addMoreCourses);
		addMoreCourses.setTextSize(dipValue);
		next.setTextSize(dipValue);

		RelativeLayout.LayoutParams addMoreCoursesLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addMoreCoursesLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, resources.getDisplayMetrics());

		addMoreCoursesLp.topMargin = (int) dipValue;
		next.setText(R.string.complete);

		setupFooter.addView(addMoreCourses, addMoreCoursesLp);

		addMoreCourses.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				insertInfo();
				parentActivity.reload();
			}

		});

		next.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (insertInfo() == 0)
					complete();
			}

		});

	}

	public int insertInfo() {

		DatabaseHandler db = new DatabaseHandler(this);

		CourseObj courseObj = parentActivity.getCourseObj();
		db.addCourse(courseObj);

		// Inserting Categories
		int category_id = db.getCategoriesCount() + 1;

		try {
			String weight1 = courseCategory1Weighting.getText().toString().trim();
			String weight2 = courseCategory2Weighting.getText().toString().trim();
			String weight3 = courseCategory3Weighting.getText().toString().trim();
			String weight4 = courseCategory4Weighting.getText().toString().trim();

			if (weight1.contains("%"))
				weight1 = weight1.substring(0, weight1.indexOf("%"));
			if (weight2.contains("%"))
				weight2 = weight2.substring(0, weight2.indexOf("%"));
			if (weight3.contains("%"))
				weight3 = weight3.substring(0, weight3.indexOf("%"));
			if (weight4.contains("%"))
				weight4 = weight4.substring(0, weight4.indexOf("%"));

			BigDecimal weightTotal = new BigDecimal(0.0);

			BigDecimal weight1D = null;
			BigDecimal weight2D = null;
			BigDecimal weight3D = null;
			BigDecimal weight4D = null;

			if (!courseCategory1Input.getText().toString().equals("")) {
				weight1D = new BigDecimal(weight1);
				weightTotal = weightTotal.add(weight1D);
			}
			if (!courseCategory2Input.getText().toString().equals("")) {
				weight2D = new BigDecimal(weight2);
				weightTotal = weightTotal.add(weight2D);
			}
			if (!courseCategory3Input.getText().toString().equals("")) {
				weight3D = new BigDecimal(weight3);
				weightTotal = weightTotal.add(weight3D);
			}
			if (!courseCategory4Input.getText().toString().equals("")) {
				weight4D = new BigDecimal(weight4);
				weightTotal = weightTotal.add(weight4D);
			}

			if (weightTotal.doubleValue() != 100.0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Weighting for all categories must sum up to 100").setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								return;
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				return 1;
			}

			if (weight1D != null)
				db.addCategory(new CategoryObj(category_id++, courseCategory1Input.getText().toString(), courseObj
						.getId(), weight1D.doubleValue()));
			if (weight2D != null)
				db.addCategory(new CategoryObj(category_id++, courseCategory2Input.getText().toString(), courseObj
						.getId(), weight2D.doubleValue()));
			if (weight3D != null)
				db.addCategory(new CategoryObj(category_id++, courseCategory3Input.getText().toString(), courseObj
						.getId(), weight3D.doubleValue()));
			if (weight4D != null)
				db.addCategory(new CategoryObj(category_id++, courseCategory4Input.getText().toString(), courseObj
						.getId(), weight4D.doubleValue()));

		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please enter a numeric weighting.").setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							return;
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			return 1;
		}

		return 0;
	}

	public void complete() {
		parentActivity.finish();
		Intent myIntent = new Intent(EnterCategories.this, MainMenuActivity.class);
		EnterCategories.this.startActivity(myIntent);
	}
}
