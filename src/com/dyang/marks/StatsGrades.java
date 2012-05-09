package com.dyang.marks;

import java.util.List;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.GradeObj;
import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatsGrades extends Activity {

    private GradesStats parentActivity;
    private LinearLayout root;
    private BroadcastReceiver receiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_grades);

        parentActivity = (GradesStats) getParent();
        root = (LinearLayout) findViewById(R.id.statsGradesLayout);

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

    public void populateInfo() {
        root.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.fadeout));
        root.setVisibility(View.INVISIBLE);
        root.removeAllViews();

        int course_id = parentActivity.getCourse_id();

        DatabaseHandler db = new DatabaseHandler(this);
        List<CategoryObj> categoryObj = db.getAllCategories(course_id);

        LinearLayout categoryBox;
        TextView label;
        LinearLayout content;
        TableLayout tl;

        for (int i = 0; i < categoryObj.size(); i++) {
            categoryBox = (LinearLayout) LayoutInflater.from(getBaseContext())
                    .inflate(R.layout.category_box, null);
            label = (TextView) ((LinearLayout) categoryBox.getChildAt(0))
                    .getChildAt(0);
            content = (LinearLayout) categoryBox.getChildAt(1);

            label.setText(categoryObj.get(i).getCategoryName());
            label.setTextColor(Color.WHITE);

            List<GradeObj> gradeObj = db.getAllGrades(course_id, categoryObj
                    .get(i).getId());

            tl = new TableLayout(this);
            TableRow tr;
            TextView gradesLabel;
            TextView grades;

            for (int j = 0; j < gradeObj.size(); j++) {
                gradesLabel = new TextView(this);
                grades = new TextView(this);
                tr = new TableRow(this);

                gradesLabel.setText(gradeObj.get(j).getGrade_name());
                gradesLabel.setTypeface(null, Typeface.BOLD);
                gradesLabel.setTextSize(16);
                grades.setText(Double.toString(gradeObj.get(j).getGrade())
                        + "%");
                grades.setTextSize(16);
                grades.setGravity(Gravity.RIGHT);

                tr.addView(gradesLabel);
                tr.addView(grades);
                tl.addView(tr);
            }

            tl.setStretchAllColumns(true);
            content.addView(tl);

            root.addView(categoryBox);
            root.startAnimation(AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.fadein));
            root.setVisibility(View.VISIBLE);

            db.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
