package com.dyang.marks;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.utils.DatabaseHandler;

public class GradesStats extends SherlockFragmentActivity {

    private List<CourseObj> courseList;
    private int course_id;
    private StatsOverview overFrag;
    private StatsGrades gradesFrag;
    private StatsAnalysis analysisFrag;
    private RelativeLayout gradesStatsRoot;
    private LinearLayout gradesStatsCategoryLabel;
    private ActionBar bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_stats);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DatabaseHandler db = new DatabaseHandler(this);
        courseList = db.getAllCourses();
        db.close();

        if (courseList.size() > 0)
            setCourse_id(courseList.get(0).getId());

        overFrag = new StatsOverview();
        gradesFrag = new StatsGrades();
        analysisFrag = new StatsAnalysis();

        bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setTab();
        
        gradesStatsRoot = (RelativeLayout) findViewById(R.id.gradesStatsRoot);
        gradesStatsRoot.setBackgroundColor(Color.DKGRAY);
        gradesStatsCategoryLabel = (LinearLayout) findViewById(R.id.gradesStatsCategoryLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu topDropdown = menu.addSubMenu("Action Item");
        for (int i = 0; i < courseList.size(); i++) {
            final int temp = i;
            topDropdown.add(
                    courseList.get(i).getName() + " ("
                            + courseList.get(i).getCode() + ")")
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            setCourse_id(courseList.get(temp).getId());
                            refreshOverview();
                            return true;
                        }

                    });
        }

        MenuItem topDropdownItem = topDropdown.getItem();
        topDropdownItem.setTitle(R.string.selectACourse);
        topDropdownItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }
    
    public void setTab() {
        ActionBar.Tab tab1 = bar.newTab();
        ActionBar.Tab tab2 = bar.newTab();
        ActionBar.Tab tab3 = bar.newTab();
        tab1.setText(R.string.overview);
        tab2.setText(R.string.grades);
        tab3.setText(R.string.analysis);
        tab1.setTabListener(new MyTabListener());
        tab2.setTabListener(new MyTabListener());
        tab3.setTabListener(new MyTabListener());
        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);
    }

    private void refreshOverview() {
        Intent i = new Intent();
        i.setAction("com.dyang.marks.RELOAD_TAB");
        sendBroadcast(i);
    }
    
    private class MyTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (tab.getPosition() == 0) {
                ft.replace(android.R.id.content, overFrag);
            } else if (tab.getPosition() == 1) {
                ft.replace(android.R.id.content, gradesFrag);
            } else {
                ft.replace(android.R.id.content, analysisFrag);
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
