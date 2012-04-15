package com.dyang.marks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainMenuActivity extends Activity {

	private ImageView mainMenuButton1;
	private ImageView mainMenuButton2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		mainMenuButton1 = (ImageView) findViewById(R.id.MainMenuButton1);
		mainMenuButton2 = (ImageView) findViewById(R.id.MainMenuButton2);

		mainMenuButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(MainMenuActivity.this, EnterGrades.class);
				MainMenuActivity.this.startActivity(myIntent);
			}
		});

		mainMenuButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(MainMenuActivity.this, CourseTabLayoutActivity.class);
				MainMenuActivity.this.startActivity(myIntent);
			}
		});
	}
}
