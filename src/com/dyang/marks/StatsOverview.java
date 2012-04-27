package com.dyang.marks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.CourseObj;
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
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatsOverview extends Activity {

	private LinearLayout statsOverviewLayout;
	private LinearLayout statsOverviewLabel;
	private LinearLayout statsOverview;
	private List<CategoryObj> categoryObjs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_overview);

		GradesStats parentActivity = (GradesStats) getParent();

		statsOverviewLabel = (LinearLayout) findViewById(R.id.statsOverviewLabel);
		statsOverview = (LinearLayout) findViewById(R.id.statsOverview);
		statsOverview.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView label = (TextView) statsOverviewLabel.getChildAt(0);
		label.setText("Grades Breakdown");
		label.setTextColor(Color.WHITE);

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
			int catColor = 0xff000000 + 256 * 256 * mNumGen.nextInt(256) + 256 * mNumGen.nextInt(256)
					+ mNumGen.nextInt(256);
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

		Bitmap mBackgroundImage = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
		pieChartView.draw(new Canvas(mBackgroundImage));

		ImageView mImageView = new ImageView(this);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mImageView.setBackgroundColor(bgColor);
		mImageView.setImageBitmap(mBackgroundImage);

		statsOverview.addView(mImageView);
	}
}
