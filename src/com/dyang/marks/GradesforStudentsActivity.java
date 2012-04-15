package com.dyang.marks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GradesforStudentsActivity extends Activity {

	private Button clickToContinue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		clickToContinue = (Button) findViewById(R.id.button1);
		clickToContinue.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
				Intent myIntent = new Intent(GradesforStudentsActivity.this, MainMenuActivity.class);
				GradesforStudentsActivity.this.startActivity(myIntent);
			}
		});
	}

}