package com.dyang.marks;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatsAnalysis extends Activity {

    private GradesStats parentActivity;
    private LinearLayout root;
    private BroadcastReceiver receiver;

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
                parentActivity.launchReverseGradeCalc();
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
}
