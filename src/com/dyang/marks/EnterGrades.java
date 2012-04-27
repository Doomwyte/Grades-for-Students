package com.dyang.marks;

import java.text.DecimalFormat;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.Obj.GradeObj;
import com.dyang.marks.adapters.CategoryAdapter;
import com.dyang.marks.adapters.CourseAdapter;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class EnterGrades extends SherlockActivity {

	private Spinner courseSpinner;
	private Spinner categorySpinner;
	private Button addMore, completeButton;
	private ScrollView gradesContentScroll;
	private LinearLayout gradesContent;
	private LinearLayout gradesLayout;
	private LinearLayout spinnerLabel;
	private LinearLayout gradesContentLabel;
	private LinearLayout gradesRow;
	private RelativeLayout enterGradesRoot;
	private DatabaseHandler db;
	private ImageView gradesRowDelete;
	private boolean init;
	private int counter;
	private int selectedCourseId;
	private int selectedCategoryId;

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
		gradesLayout = (LinearLayout) findViewById(R.id.gradesLayout);
		enterGradesRoot = (RelativeLayout) findViewById(R.id.enterGradesRoot);
		gradesContentScroll = (ScrollView) findViewById(R.id.gradesContentScroll);
		addMore = (Button) findViewById(R.id.addMore);
		completeButton = (Button) findViewById(R.id.gradesCompleteButton);
		counter = 1;
		init = false;

		enterGradesRoot.setBackgroundColor(Color.DKGRAY);

		TextView spinnerLabelText = (TextView) spinnerLabel.getChildAt(0);
		TextView gradesContentLabelText = (TextView) gradesContentLabel.getChildAt(0);

		spinnerLabelText.setText(R.string.courseInfo);
		gradesContentLabelText.setText(R.string.gradesInfo);
		spinnerLabelText.setTextColor(Color.WHITE);
		gradesContentLabelText.setTextColor(Color.WHITE);
		spinnerLabelText.setTextSize(15);
		gradesContentLabelText.setTextSize(15);

		List<CourseObj> allCourses = db.getAllCourses();

		if (allCourses.size() == 0) {
			Intent myIntent = new Intent(EnterGrades.this, CourseTabLayoutActivity.class);
			EnterGrades.this.startActivity(myIntent);
		}

		allCourses.add(0, new CourseObj(999, "Select a course", "999", "999"));

		CourseAdapter cAdapter = new CourseAdapter(this, android.R.layout.simple_spinner_item, allCourses);
		cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		courseSpinner.setAdapter(cAdapter);
		courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CourseObj selectedCourse = ((CourseObj) courseSpinner.getSelectedItem());
				if (selectedCourse.getCode().equals("999")) {
					if (gradesLayout.getVisibility() != View.INVISIBLE) {
						categorySpinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
								R.anim.fadeout));
						gradesLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
								R.anim.fadeout));
						categorySpinner.setVisibility(View.GONE);
						gradesLayout.setVisibility(View.GONE);
					}
					return;
				}
				CategoryAdapter caAdapter = new CategoryAdapter(EnterGrades.this, android.R.layout.simple_spinner_item,
						db.getAllCategories(selectedCourse.getId()));
				caAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				categorySpinner.setAdapter(caAdapter);
				categorySpinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein));
				gradesLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein));
				categorySpinner.setVisibility(View.VISIBLE);
				gradesLayout.setVisibility(View.VISIBLE);
				selectedCourseId = ((CourseObj) courseSpinner.getSelectedItem()).getId();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (init) {
					insertGrades();
				} else {
					init = true;
				}
				rowManager(true, false);
				selectedCategoryId = ((CategoryObj) categorySpinner.getSelectedItem()).getId();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		addMore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				rowManager(false, true);
			}

		});

		completeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				insertGrades();
				finish();
				Intent intent = new Intent(EnterGrades.this, MainMenuActivity.class);
				EnterGrades.this.startActivity(intent);
			}
		});
	}

	public void rowManager(boolean newCategory, boolean addMore) {

		if (newCategory) {
			counter = 1;
			gradesContent.removeViews(0, gradesContent.getChildCount() - 1);
		}

		gradesRow = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.grades_row, null);
		Button child = (Button) gradesRow.getChildAt(0);
		child.setTextColor(Color.BLACK);
		EditText childEdit = (EditText) gradesRow.getChildAt(1);
		gradesRowDelete = (ImageView) gradesRow.getChildAt(2);

		List<GradeObj> results = retrieveGrades(((CourseObj) courseSpinner.getSelectedItem()).getId(),
				((CategoryObj) categorySpinner.getSelectedItem()).getId());

		if (results.size() > 0 && !addMore) {
			gradesContent.removeViews(0, gradesContent.getChildCount() - 1);
			for (int i = 0; i < results.size(); i++) {
				gradesRow = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.grades_row, null);
				child = (Button) gradesRow.getChildAt(0);
				childEdit = (EditText) gradesRow.getChildAt(1);
				child.setText(results.get(i).getGrade_name());
				childEdit.setText(Double.toString(results.get(i).getGrade()));
				gradesRowDelete = (ImageView) gradesRow.getChildAt(2);
				gradesRowDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						final LinearLayout row = (LinearLayout) ((ImageView) arg0).getParent();

						AnimationSet set = new AnimationSet(true);

						Animation animation = new AlphaAnimation(1.0f, 0.0f);
						animation.setDuration(200);
						set.addAnimation(animation);

						animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
								Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
								Animation.RELATIVE_TO_SELF, -1f);
						animation.setDuration(200);
						set.addAnimation(animation);

						set.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationEnd(Animation arg0) {
								gradesContentScroll.post(new Runnable() {
									@Override
									public void run() {
										gradesContent.removeView(row);
									}
								});
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
							}

							@Override
							public void onAnimationStart(Animation arg0) {
							}

						});

						LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
						row.setLayoutAnimation(controller);
						row.invalidate();
					}
				});

				gradesContent.addView(gradesRow, gradesContent.getChildCount() - 1);
				counter++;
			}
		} else {
			child.setText(((CategoryObj) categorySpinner.getSelectedItem()).getCategoryName() + " " + counter++);
			gradesContent.addView(gradesRow, gradesContent.getChildCount() - 1);
		}

		child.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				editCourse(arg0);
			}
		});

		gradesRowDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final LinearLayout row = (LinearLayout) ((ImageView) arg0).getParent();

				AnimationSet set = new AnimationSet(true);

				Animation animation = new AlphaAnimation(1.0f, 0.0f);
				animation.setDuration(200);
				set.addAnimation(animation);

				animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1f);
				animation.setDuration(200);
				set.addAnimation(animation);

				set.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						gradesContentScroll.post(new Runnable() {
							@Override
							public void run() {
								gradesContent.removeView(row);
							}
						});
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationStart(Animation arg0) {
					}

				});

				LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
				row.setLayoutAnimation(controller);

				row.invalidate();
			}
		});

		gradesContentScroll.post(new Runnable() {
			public void run() {
				gradesContentScroll.fullScroll(ScrollView.FOCUS_DOWN);
				((LinearLayout) gradesContent.getChildAt(gradesContent.getChildCount() - 2)).getChildAt(1)
						.requestFocus();
			}
		});

		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(200);
		set.addAnimation(animation);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
		gradesRow.setLayoutAnimation(controller);
	}

	public void editCourse(View clicked) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.editCourse);

		LinearLayout courseNameLayout = new LinearLayout(this);
		LinearLayout courseWeightLayout = new LinearLayout(this);

		TextView editCourse = new TextView(this);
		editCourse.setText(R.string.courseName);
		editCourse.setTextColor(Color.WHITE);
		editCourse.setGravity(Gravity.LEFT);
		editCourse.setPadding(5, 0, 0, 0);

		EditText inputName = new EditText(this);
		inputName.setText(((Button) clicked).getText());

		TextView courseWeighting = new TextView(this);
		courseWeighting.setText(R.string.weighting);
		courseWeighting.setTextColor(Color.WHITE);
		courseWeighting.setGravity(Gravity.LEFT);
		courseWeighting.setPadding(5, 0, 0, 0);

		EditText inputWeighting = new EditText(this);
		double indivWeighting = 100.0 / (gradesContent.getChildCount() - 1);
		DecimalFormat sdf = new DecimalFormat();
		sdf.setMaximumFractionDigits(2);

		inputWeighting.setText(sdf.format(indivWeighting));

		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);

		linearLp.weight = 1;
		courseNameLayout.addView(editCourse, linearLp);
		courseWeightLayout.addView(courseWeighting, linearLp);

		linearLp.weight = 2;
		courseNameLayout.addView(inputName, linearLp);
		courseWeightLayout.addView(inputWeighting, linearLp);

		courseNameLayout.setPadding(5, 5, 5, 5);
		courseWeightLayout.setPadding(5, 5, 5, 5);

		LinearLayout overallLayout = new LinearLayout(this);
		overallLayout.setOrientation(LinearLayout.VERTICAL);
		overallLayout.addView(courseNameLayout);
		overallLayout.addView(courseWeightLayout);

		alert.setView(overallLayout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	public void insertGrades() {
		if (courseSpinner.getSelectedItem() == null || courseSpinner == null || categorySpinner == null
				|| categorySpinner.getSelectedItem() == null)
			return;

		LinearLayout gradeRow;
		Button grade_name;
		EditText grade;
		DatabaseHandler db = new DatabaseHandler(this);

		if (init)
			db.preAddGrade(selectedCourseId, selectedCategoryId);

		for (int i = 0; i < gradesContent.getChildCount() - 1; i++) {
			gradeRow = (LinearLayout) gradesContent.getChildAt(i);
			grade_name = (Button) gradeRow.getChildAt(0);
			grade = (EditText) gradeRow.getChildAt(1);

			if (!grade.getText().toString().trim().equals("")) {
				db.addGrade(new GradeObj(db.getNextGradeId(), grade_name.getText().toString(), Double.valueOf(grade
						.getText().toString()), selectedCourseId, selectedCategoryId));
			}
		}
		db.close();
	}

	public List<GradeObj> retrieveGrades(int course_id, int category_id) {
		DatabaseHandler db = new DatabaseHandler(this);
		List<GradeObj> gradeObj = db.getAllGrades(course_id, category_id);
		return gradeObj;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("Action Item");
		subMenu1.add(R.string.editSelectedCourse);
		subMenu1.add(R.string.deleteSelectedCourse);

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_action_overflow);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		subMenu1.getItem(0).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(EnterGrades.this, CourseTabLayoutActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("editMode", ((CourseObj) courseSpinner.getSelectedItem()).getId());
				intent.putExtras(bundle);
				EnterGrades.this.startActivity(intent);
				return false;
			}
		});

		subMenu1.getItem(1).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				final CourseObj courseToDelete = (CourseObj) courseSpinner.getSelectedItem();
				AlertDialog.Builder builder = new AlertDialog.Builder(EnterGrades.this);
				builder.setMessage(
						"Are you sure you want to delete this course along with its grades?\n\nCourse Name: "
								+ courseToDelete.getName() + "\nCourse Code: " + courseToDelete.getCode())
						.setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DatabaseHandler db = new DatabaseHandler(EnterGrades.this);
								db.deleteCourse(courseToDelete);
								db.close();
								Intent intent = getIntent();
								finish();
								startActivity(intent);
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				builder.setTitle("WARNING");
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

}