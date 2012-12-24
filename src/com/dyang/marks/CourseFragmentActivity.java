package com.dyang.marks;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.dyang.marks.Obj.CourseObj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class CourseFragmentActivity extends SherlockFragmentActivity {

    // public TabHost tabHost;
    public CourseObj courseObj;
    public static Activity parentActivity;
    public int course_id;
    public ActionBar bar;
    private EnterCourses courseFrag;
    private EnterCategories catFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_tabhost);
        parentActivity = this;
        bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setCourse_id(0);

        courseFrag = new EnterCourses();
        catFrag = new EnterCategories();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setCourse_id(bundle.getInt("editMode"));
            resetTab();
        }

        setTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Back")
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        bar.setSelectedNavigationItem(0);
                        return false;
                    }
                }).setTitle(R.string.back)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add("Next")
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(bar.getSelectedNavigationIndex()==0)
                            bar.setSelectedNavigationItem(1);
                        else{
                            catFrag.insertInfo();
                            catFrag.complete();
                        }
                        return false;
                    }
                }).setTitle(R.string.next)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private class MyTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (tab.getPosition() == 0) {
                ft.replace(android.R.id.content, courseFrag);
                
            } else {
                ft.replace(android.R.id.content, catFrag);
                setCourse_id(courseFrag.getEditCourseId());
                courseFrag.setCourseInfo(getCourse_id());
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
        }
    }

    public void switchTab(int tab) {
        bar.setSelectedNavigationItem(tab);
    }

    public void setCourseObj(CourseObj courseObj) {
        this.courseObj = courseObj;
    }

    public CourseObj getCourseObj() {
        return courseObj;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void reload() {
        startActivity(getIntent());
        finish();
    }

    public void resetTab() {
        bar.removeAllTabs();
    }

    public void setTab() {
        ActionBar.Tab tab1 = bar.newTab();
        ActionBar.Tab tab2 = bar.newTab();
        tab1.setText("General");
        tab2.setText("Categories");
        tab1.setTabListener(new MyTabListener());
        tab2.setTabListener(new MyTabListener());
        bar.addTab(tab1);
        bar.addTab(tab2);
    }

}
