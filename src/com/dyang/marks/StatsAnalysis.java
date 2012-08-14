package com.dyang.marks;

import java.util.ArrayList;
import java.util.List;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.GradeObj;
import com.dyang.marks.adapters.CategoryAdapter;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class StatsAnalysis extends Activity {

    private GradesStats parentActivity;
    private LinearLayout root;
    private BroadcastReceiver receiver;
    private DatabaseHandler db;
    private Spinner categorySpinner;
    private TextView passingGradeDisplay;
    private SeekBar passingGradeBar;
    private TextView resultText;
    private CategoryObj selectedCat;
    private double result;
    private double avg;
    private int course_id;

    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_analysis);

        parentActivity = (GradesStats) getParent();
        root = (LinearLayout) findViewById(R.id.statsAnalysisLayout);

        // Generate Content
        populateInfo();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dyang.marks.RELOAD_TAB");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                populateInfo();
            }
        };
        registerReceiver(receiver, filter);
    }

    public final void populateInfo() {
        // MARK NEEDED BOX
        LinearLayout markNeededLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        TextView label = (TextView) ((LinearLayout) markNeededLayout
                .getChildAt(0)).getChildAt(0);
        LinearLayout content = (LinearLayout) markNeededLayout.getChildAt(1);

        label.setText(R.string.markNeeded);
        label.setTextColor(Color.WHITE);

        TextView contentText = new TextView(this);
        contentText.setText(R.string.markNeededContent);
        content.addView(contentText);

        Button button = new Button(this, null, android.R.attr.buttonStyleSmall);
        button.setText(R.string.launch);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchReverseGradeCalc();
            }
        });

        content.addView(button);
        root.addView(markNeededLayout);
    }

    @Override
    protected final void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void launchReverseGradeCalc() {

        root.removeAllViews();

        LinearLayout reverseGradeLayout;
        TextView label;
        LinearLayout content;

        passingGradeBar = new SeekBar(this);
        passingGradeBar.setProgress(50);
        result = 0;

        reverseGradeLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        label = (TextView) ((LinearLayout) reverseGradeLayout.getChildAt(0))
                .getChildAt(0);
        content = (LinearLayout) reverseGradeLayout.getChildAt(1);

        course_id = parentActivity.getCourse_id();

        db = new DatabaseHandler(this);
        categorySpinner = new Spinner(this);
        CategoryAdapter caAdapter = new CategoryAdapter(this,
                android.R.layout.simple_spinner_item,
                db.getAllCategories(course_id));
        caAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(caAdapter);

        categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                selectedCat = (CategoryObj) categorySpinner.getSelectedItem();
                List<CategoryObj> catArray = db.getAllCategories(course_id);
                List<GradeObj> gradeArray;
                avg = 0;
                result = 0;
                for (int i = 0; i < catArray.size(); i++) {
                    if (catArray.get(i).getId() != selectedCat.getId()) {
                        gradeArray = db.getAllGrades(course_id, catArray.get(i)
                                .getId());
                        double sum = 0;
                        for (int j = 0; j < gradeArray.size(); j++) {
                            double getGrade = gradeArray.get(j).getGrade();
                            sum += getGrade;
                        }
                        avg = avg
                                + ((sum / gradeArray.size())
                                        * catArray.get(i).getWeight() / 100);
                    }
                }
                result = (passingGradeBar.getProgress() - avg)
                        / (selectedCat.getWeight() / 100);
                if (!Double.isNaN(result))
                    if (result > 100)
                        resultText.setText(R.string.impossible);
                    else if (result > 0)
                        resultText.setText(getString(R.string.markRequired)
                                + " " + Double.toString(result) + "%");
                    else
                        resultText.setText(R.string.alreadyPassed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        label.setText(R.string.unknownMark);
        label.setTextColor(Color.WHITE);
        content.addView(categorySpinner);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        root.addView(reverseGradeLayout, lp);

        reverseGradeLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        label = (TextView) ((LinearLayout) reverseGradeLayout.getChildAt(0))
                .getChildAt(0);
        content = (LinearLayout) reverseGradeLayout.getChildAt(1);

        passingGradeDisplay = new TextView(this);
        passingGradeDisplay.setText(getString(R.string.passingMark) + ": "
                + passingGradeBar.getProgress() + "%");
        passingGradeBar
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        passingGradeDisplay
                                .setText(getString(R.string.passingMark) + ": "
                                        + progress + "%");
                        result = (passingGradeBar.getProgress() - avg)
                                / (selectedCat.getWeight() / 100);
                        if (!Double.isNaN(result))
                            if (result > 100)
                                resultText.setText(R.string.impossible);
                            else if (result > 0)
                                resultText
                                        .setText(getString(R.string.markRequired)
                                                + " "
                                                + Double.toString(result)
                                                + "%");
                            else
                                resultText.setText(R.string.alreadyPassed);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });

        label.setText(R.string.passingMark);
        label.setTextColor(Color.WHITE);
        content.addView(passingGradeBar, lp);
        content.addView(passingGradeDisplay, lp);

        root.addView(reverseGradeLayout, lp);

        reverseGradeLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        label = (TextView) ((LinearLayout) reverseGradeLayout.getChildAt(0))
                .getChildAt(0);
        content = (LinearLayout) reverseGradeLayout.getChildAt(1);

        label.setText(R.string.result);
        label.setTextColor(Color.WHITE);

        resultText = new TextView(this);
        content.addView(resultText);

        root.addView(reverseGradeLayout, lp);

        db.close();
    }
}
