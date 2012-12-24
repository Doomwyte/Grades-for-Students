package com.dyang.marks;

import com.dyang.marks.utils.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class MainMenuActivity extends Activity {

	private ImageView mainMenuButton1;
	private ImageView mainMenuButton2;
	private ImageView setupButton;
	private ImageView mainMenuButton5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		mainMenuButton1 = (ImageView) findViewById(R.id.MainMenuButton1);
		mainMenuButton2 = (ImageView) findViewById(R.id.MainMenuButton2);
		mainMenuButton5 = (ImageView) findViewById(R.id.MainMenuButton5);
		setupButton = (ImageView) findViewById(R.id.setupButton);

		mainMenuButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			    finish();
				Intent myIntent = new Intent(MainMenuActivity.this, EnterGrades.class);
				MainMenuActivity.this.startActivity(myIntent);
			}
		});
		
		mainMenuButton2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
			    finish();
				Intent myIntent = new Intent(MainMenuActivity.this, GradesStats.class);
				MainMenuActivity.this.startActivity(myIntent);				
			}
		});
		
        mainMenuButton5.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                finish();
                Intent myIntent = new Intent(MainMenuActivity.this, CourseFragmentActivity.class);
                MainMenuActivity.this.startActivity(myIntent);        
            }
        });

		setupButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
				builder.setMessage("This will wipe all data, including all courses, categories and grades.")
						.setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MainMenuActivity.this.finish();
								DatabaseHandler db = new DatabaseHandler(MainMenuActivity.this);
								db.deleteAllCourses();
								db.close();
								finish();
								Intent myIntent = new Intent(MainMenuActivity.this, CourseFragmentActivity.class);
								MainMenuActivity.this.startActivity(myIntent);
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				builder.setTitle("WARNING");
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
}
