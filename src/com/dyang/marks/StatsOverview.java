package com.dyang.marks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.GradeObj;
import com.dyang.marks.utils.DatabaseHandler;
import com.dyang.marks.utils.PieChart;
import com.dyang.marks.utils.PieItem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatsOverview extends Activity {

    private LinearLayout statsOverviewGeneralLabel;
    private LinearLayout statsOverviewGeneral;
    private LinearLayout statsOverviewBreakdownLabel;
    private LinearLayout statsOverviewBreakdown;
    private List<CategoryObj> categoryObjs;
    private BroadcastReceiver receiver;
    private GradesStats parentActivity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_overview);

        parentActivity = (GradesStats) getParent();
        statsOverviewGeneralLabel = (LinearLayout) findViewById(R.id.statsOverviewGeneralLabel);
        statsOverviewGeneral = (LinearLayout) findViewById(R.id.statsOverviewGeneral);
        statsOverviewBreakdownLabel = (LinearLayout) findViewById(R.id.statsOverviewBreakdownLabel);
        statsOverviewBreakdown = (LinearLayout) findViewById(R.id.statsOverviewBreakdown);
        statsOverviewGeneral.setGravity(Gravity.CENTER_HORIZONTAL);
        statsOverviewBreakdown.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView generalLabel = (TextView) statsOverviewGeneralLabel
                .getChildAt(0);
        generalLabel.setText("General");
        generalLabel.setTextColor(Color.WHITE);

        TextView breakdownLabel = (TextView) statsOverviewBreakdownLabel
                .getChildAt(0);
        breakdownLabel.setText("Grades Breakdown");
        breakdownLabel.setTextColor(Color.WHITE);

        populateGeneralInfo();
        generateGraph();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dyang.marks.RELOAD_TAB");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                statsOverviewBreakdown.getChildAt(0).startAnimation(
                        AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.fadeout));
                statsOverviewBreakdown.getChildAt(0).setVisibility(
                        View.INVISIBLE);
                statsOverviewBreakdown.removeAllViews();
                populateGeneralInfo();
                generateGraph();
            }
        };
        registerReceiver(receiver, filter);
    }

    public void populateGeneralInfo() {
        statsOverviewGeneral.removeAllViews();

        DatabaseHandler db = new DatabaseHandler(this);
        TableLayout tableLayout = new TableLayout(this);

        TextView courseNameLabel = new TextView(this);
        TextView courseName = new TextView(this);
        courseNameLabel.setText(R.string.courseName);
        courseName.setText(db.getCourse(parentActivity.getCourse_id())
                .getName());

        TextView courseCodeLabel = new TextView(this);
        TextView courseCode = new TextView(this);
        courseCodeLabel.setText(R.string.courseCode);
        courseCode.setText(db.getCourse(parentActivity.getCourse_id())
                .getCode());

        TextView courseAverageLabel = new TextView(this);
        TextView courseAverage = new TextView(this);
        courseAverageLabel.setText(R.string.courseAverage);

        List<CategoryObj> categoryObj = db.getAllCategories(parentActivity
                .getCourse_id());

        double cumalativeAvg = 0;
        double sectionAvg;
        List<GradeObj> gradeObj;
        for (int i = 0; i < categoryObj.size(); i++) {
            gradeObj = db.getAllGrades(parentActivity.getCourse_id(),
                    categoryObj.get(i).getId());
            sectionAvg = 0;
            for (int j = 0; j < gradeObj.size(); j++) {
                sectionAvg += (gradeObj.get(j).getGrade()
                        * categoryObj.get(i).getWeight() / 100);
            }
            if (gradeObj.size() != 0) {
                sectionAvg /= gradeObj.size();
                cumalativeAvg += sectionAvg;
            }
        }
        courseAverage.setText(cumalativeAvg + "%");

        courseNameLabel.setTextSize(16);
        courseCodeLabel.setTextSize(16);
        courseName.setTextSize(16);
        courseCode.setTextSize(16);
        courseAverageLabel.setTextSize(16);
        courseAverage.setTextSize(16);

        courseNameLabel.setTypeface(null, Typeface.BOLD);
        courseCodeLabel.setTypeface(null, Typeface.BOLD);
        courseAverageLabel.setTypeface(null, Typeface.BOLD);

        TableRow courseNameRow = new TableRow(this);
        TableRow courseCodeRow = new TableRow(this);
        TableRow courseAverageRow = new TableRow(this);

        courseNameRow.addView(courseNameLabel);
        courseNameRow.addView(courseName);
        courseCodeRow.addView(courseCodeLabel);
        courseCodeRow.addView(courseCode);
        courseAverageRow.addView(courseAverageLabel);
        courseAverageRow.addView(courseAverage);

        tableLayout.addView(courseNameRow);
        tableLayout.addView(courseCodeRow);
        tableLayout.addView(courseAverageRow);
        tableLayout.setStretchAllColumns(true);

        statsOverviewGeneral.addView(tableLayout);
        db.close();
    }

    public void generateGraph() {
        PieItem item;
        TextView tv;
        List<PieItem> pieData = new ArrayList<PieItem>();
        List<TextView> legend = new ArrayList<TextView>();

        DatabaseHandler db = new DatabaseHandler(this);
        categoryObjs = db.getAllCategories(parentActivity.getCourse_id());
        db.close();

        Random mNumGen = new Random();
        for (int i = 0; i < categoryObjs.size(); i++) {
            item = new PieItem();
            String catName = categoryObjs.get(i).getCategoryName();
            int catColor = 0xff000000 + 256 * 256 * mNumGen.nextInt(256) + 256
                    * mNumGen.nextInt(256) + mNumGen.nextInt(256);
            item.Count = categoryObjs.get(i).getWeight();
            item.Label = catName;
            item.Color = catColor;
            pieData.add(item);

            tv = new TextView(this);
            tv.setText(catName);
            tv.setTextColor(catColor);
            legend.add(tv);
        }

        int size = 300;
        int bgColor = 0xEEEEEEEE;

        PieChart pieChartView = new PieChart(this);
        pieChartView.setLayoutParams(new LayoutParams(size, size));
        pieChartView.setGeometry(size, size, 5, 5, 5, 5);
        pieChartView.setSkinParams(bgColor);
        pieChartView.setData(pieData, 100);
        pieChartView.invalidate();

        Bitmap mBackgroundImage = Bitmap.createBitmap(size, size,
                Bitmap.Config.RGB_565);
        pieChartView.draw(new Canvas(mBackgroundImage));

        ImageView mImageView = new ImageView(this);
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mImageView.setBackgroundColor(bgColor);
        mImageView.setImageBitmap(mBackgroundImage);
        mImageView.setVisibility(View.INVISIBLE);
        statsOverviewBreakdown.addView(mImageView);
        mImageView.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.fadein));
        mImageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
