package com.dyang.marks;

import java.util.List;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.adapters.CategoryAdapter;
import com.dyang.marks.adapters.CourseAdapter;
import com.dyang.marks.utils.DatabaseHandler;

public class GradesStats extends SherlockActivity implements
        ActionBar.OnNavigationListener {

    private List<CourseObj> courseList;
    private RelativeLayout gradesStatsRoot;
    private TabHost tabHost;
    private int course_id;
    private boolean initialized = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_stats);

        DatabaseHandler db = new DatabaseHandler(this);
        courseList = db.getAllCourses();
        db.close();

        CourseAdapter list = new CourseAdapter(this,
                R.layout.sherlock_spinner_item, courseList);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);

        if (courseList.size() > 0)
            setCourse_id(courseList.get(
                    getSupportActionBar().getSelectedNavigationIndex()).getId());

        gradesStatsRoot = (RelativeLayout) findViewById(R.id.gradesStatsRoot);
        gradesStatsRoot.setBackgroundColor(Color.DKGRAY);

        tabHost = (TabHost) LayoutInflater.from(getBaseContext()).inflate(
                R.layout.course_tabhost, null);
        LocalActivityManager mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);
        tabHost.setup(mlam);

        TabSpec overviewSpec = tabHost.newTabSpec("Overview");
        overviewSpec.setIndicator(getResources().getString(R.string.overview));
        Intent overviewIntent = new Intent(this, StatsOverview.class);
        overviewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overviewSpec.setContent(overviewIntent);
        tabHost.addTab(overviewSpec);

        TabSpec gradesSpec = tabHost.newTabSpec("Grades");
        gradesSpec.setIndicator(getResources().getString(R.string.grades));
        Intent gradesIntent = new Intent(this, StatsGrades.class);
        gradesSpec.setContent(gradesIntent);
        tabHost.addTab(gradesSpec);

        TabSpec analysisSpec = tabHost.newTabSpec("Analysis");
        analysisSpec.setIndicator(getResources().getString(R.string.tool));
        Intent analysisIntent = new Intent(this, StatsAnalysis.class);
        analysisSpec.setContent(analysisIntent);
        tabHost.addTab(analysisSpec);

        TextView tv;
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tv = (TextView) tabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);
            tv.setTextColor(this.getResources().getColorStateList(
                    R.color.tab_text));
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 3, 0, 0);
        gradesStatsRoot.addView(tabHost, lp);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        setCourse_id(courseList.get(itemPosition).getId());
        if (initialized)
            refreshOverview();
        else
            initialized = true;
        return true;
    }

    public void launchReverseGradeCalc() {
        gradesStatsRoot.removeAllViews();

        LinearLayout reverseGradeLayout;
        TextView label;
        LinearLayout content;
        
        reverseGradeLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        label = (TextView) ((LinearLayout) reverseGradeLayout
                .getChildAt(0)).getChildAt(0);
        content = (LinearLayout) reverseGradeLayout.getChildAt(1);

        DatabaseHandler db = new DatabaseHandler(this);
        Spinner categorySpinner = new Spinner(this);
        CategoryAdapter caAdapter = new CategoryAdapter(GradesStats.this,
                android.R.layout.simple_spinner_item,
                db.getAllCategories(getCourse_id()));
        caAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(caAdapter);

        label.setText(R.string.unknownMark);
        label.setTextColor(Color.WHITE);
        content.addView(categorySpinner);

        gradesStatsRoot.addView(reverseGradeLayout,
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

        reverseGradeLayout = (LinearLayout) LayoutInflater.from(
                getBaseContext()).inflate(R.layout.category_box, null);
        label = (TextView) ((LinearLayout) reverseGradeLayout
                .getChildAt(0)).getChildAt(0);
        content = (LinearLayout) reverseGradeLayout.getChildAt(1);
        
        db.close();
    }

    private void refreshOverview() {
        Intent i = new Intent();
        i.setAction("com.dyang.marks.RELOAD_TAB");
        sendBroadcast(i);
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
