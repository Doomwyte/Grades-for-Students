package com.dyang.marks;

import java.math.BigDecimal;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EnterCategories extends SherlockFragment {

    private EditText courseCategory1Input;
    private EditText courseCategory2Input;
    private EditText courseCategory3Input;
    private EditText courseCategory4Input;
    private EditText courseCategory1Weighting;
    private EditText courseCategory2Weighting;
    private EditText courseCategory3Weighting;
    private EditText courseCategory4Weighting;
    private RelativeLayout setupContent;
    private Resources resources;
    private float dipValue;
    private int course_id;
    private CourseFragmentActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragView = inflater.inflate(R.layout.enter_courses, container,
                false);

        setupContent = (RelativeLayout) fragView
                .findViewById(R.id.SetupContent);
        resources = getResources();
        parentActivity = (CourseFragmentActivity) getActivity();
        course_id = parentActivity.getCourse_id();
        inputCategorySettings();

        return fragView;
    }

    public void inputCategorySettings() {
        setupContent.removeAllViews();
        dipValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                resources.getDisplayMetrics());

        List<CategoryObj> categoryObjList = null;
        if (course_id != 0) {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            categoryObjList = db.getAllCategories(course_id);
        }

        courseCategory1Input = new EditText(getActivity());
        courseCategory2Input = new EditText(getActivity());
        courseCategory3Input = new EditText(getActivity());
        courseCategory4Input = new EditText(getActivity());

        courseCategory1Input.setSingleLine();
        courseCategory2Input.setSingleLine();
        courseCategory3Input.setSingleLine();
        courseCategory4Input.setSingleLine();

        courseCategory1Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        courseCategory2Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        courseCategory3Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        courseCategory4Input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        courseCategory1Input.setText(R.string.assignments);
        courseCategory2Input.setText(R.string.midterms);
        courseCategory3Input.setText(R.string.finals);

        courseCategory1Weighting = new EditText(getActivity());
        courseCategory2Weighting = new EditText(getActivity());
        courseCategory3Weighting = new EditText(getActivity());
        courseCategory4Weighting = new EditText(getActivity());

        courseCategory1Weighting.setHint(R.string.thirtypercent);
        courseCategory2Weighting.setHint(R.string.thirtypercent);
        courseCategory3Weighting.setHint(R.string.thirtypercent);
        courseCategory4Weighting.setHint(R.string.thirtypercent);

        courseCategory1Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
        courseCategory2Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
        courseCategory3Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);
        courseCategory4Weighting.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (course_id != 0) {
            if (categoryObjList.size() >= 1 && categoryObjList.get(0) != null) {
                courseCategory1Input.setText(categoryObjList.get(0)
                        .getCategoryName().toString());
                courseCategory1Weighting.setText(Double
                        .toString(categoryObjList.get(0).getWeight()));
            }
            if (categoryObjList.size() >= 2 && categoryObjList.get(1) != null) {
                courseCategory2Input.setText(categoryObjList.get(1)
                        .getCategoryName().toString());
                courseCategory2Weighting.setText(Double
                        .toString(categoryObjList.get(1).getWeight()));
            }
            if (categoryObjList.size() >= 3 && categoryObjList.get(2) != null) {
                courseCategory3Input.setText(categoryObjList.get(2)
                        .getCategoryName().toString());
                courseCategory3Weighting.setText(Double
                        .toString(categoryObjList.get(2).getWeight()));
            }
            if (categoryObjList.size() >= 4 && categoryObjList.get(3) != null) {
                courseCategory4Input.setText(categoryObjList.get(3)
                        .getCategoryName().toString());
                courseCategory4Weighting.setText(Double
                        .toString(categoryObjList.get(3).getWeight()));
            }
        }

        TextView courseCategories = new TextView(getActivity());
        courseCategories.setText(R.string.courseCategories);
        courseCategories.setTextSize(dipValue);
        courseCategories.setTextColor(Color.BLACK);
        courseCategories.setGravity(Gravity.CENTER);

        TextView courseCategoriesWeighting = new TextView(getActivity());
        courseCategoriesWeighting.setText(R.string.weighting);
        courseCategoriesWeighting.setTextSize(dipValue);
        courseCategoriesWeighting.setTextColor(Color.BLACK);
        courseCategoriesWeighting.setGravity(Gravity.CENTER);

        TableLayout courseContent = new TableLayout(getActivity());
        TableRow courseCategory = new TableRow(getActivity());
        TableRow courseCategory1 = new TableRow(getActivity());
        TableRow courseCategory2 = new TableRow(getActivity());
        TableRow courseCategory3 = new TableRow(getActivity());
        TableRow courseCategory4 = new TableRow(getActivity());

        TableRow.LayoutParams rowLp = new TableRow.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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

        setupContent.addView(courseContent, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public int insertInfo() {

        DatabaseHandler db = new DatabaseHandler(getActivity());

        db.preAddCategory(course_id);
        db.preAddCourse(course_id);

        CourseObj courseObj = parentActivity.getCourseObj();
        db.addCourse(courseObj);

        // Inserting Categories
        int category_id = db.getCategoriesCount() + 1;

        try {
            String weight1 = courseCategory1Weighting.getText().toString()
                    .trim();
            String weight2 = courseCategory2Weighting.getText().toString()
                    .trim();
            String weight3 = courseCategory3Weighting.getText().toString()
                    .trim();
            String weight4 = courseCategory4Weighting.getText().toString()
                    .trim();

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
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setMessage(
                        "Weighting for all categories must sum up to 100")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                        return;
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return 1;
            }

            if (weight1D != null)
                db.addCategory(new CategoryObj(category_id++,
                        courseCategory1Input.getText().toString(), courseObj
                                .getId(), weight1D.doubleValue()));
            if (weight2D != null)
                db.addCategory(new CategoryObj(category_id++,
                        courseCategory2Input.getText().toString(), courseObj
                                .getId(), weight2D.doubleValue()));
            if (weight3D != null)
                db.addCategory(new CategoryObj(category_id++,
                        courseCategory3Input.getText().toString(), courseObj
                                .getId(), weight3D.doubleValue()));
            if (weight4D != null)
                db.addCategory(new CategoryObj(category_id++,
                        courseCategory4Input.getText().toString(), courseObj
                                .getId(), weight4D.doubleValue()));

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please enter a numeric weighting.")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
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
        Intent myIntent = new Intent(getActivity(), MainMenuActivity.class);
        EnterCategories.this.startActivity(myIntent);
    }
}
