package com.dyang.marks.utils;

import com.dyang.marks.utils.PieItem;

import java.text.DecimalFormat;
import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {
	private static final int WAIT = 0;
	private static final int IS_READY_TO_DRAW = 1;
	private static final int IS_DRAW = 2;
	private static final float START_INC = 30;
	private Paint mBgPaints = new Paint();
	private Paint mLinePaints = new Paint();
	private Paint mTextPaints = new Paint();
	private int mWidth;
	private int mHeight;
	private int mGapLeft;
	private int mGapRight;
	private int mGapTop;
	private int mGapBottom;
	private int mBgColor;
	private int mState = WAIT;
	private float mStart;
	private float mSweep;
	private int mMaxConnection;
	private List<PieItem> mDataArray;

	// --------------------------------------------------------------------------------------
	public PieChart(Context context) {
		super(context);
	}

	// --------------------------------------------------------------------------------------
	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// --------------------------------------------------------------------------------------
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ------------------------------------------------------
		if (mState != IS_READY_TO_DRAW)
			return;
		canvas.drawColor(mBgColor);
		// ------------------------------------------------------
		mBgPaints.setAntiAlias(true);
		mBgPaints.setStyle(Paint.Style.FILL);
		mBgPaints.setColor(0x88FF0000);
		mBgPaints.setStrokeWidth(0.5f);
		// ------------------------------------------------------
		mLinePaints.setAntiAlias(true);
		mLinePaints.setStyle(Paint.Style.STROKE);
		mLinePaints.setColor(0xff000000);
		mLinePaints.setStrokeWidth(0.5f);
		// ------------------------------------------------------
		mTextPaints.setAntiAlias(true);
		mTextPaints.setStyle(Paint.Style.FILL);
		mTextPaints.setColor(0xff000000);
		mTextPaints.setTextSize(20);
		mTextPaints.setStrokeWidth(0.5f);
		// ------------------------------------------------------
		RectF mOvals = new RectF(mGapLeft, mGapTop, mWidth - mGapRight, mHeight - mGapBottom);
		// ------------------------------------------------------
		mStart = START_INC;
		PieItem Item;
		for (int i = 0; i < mDataArray.size(); i++) {
			Item = (PieItem) mDataArray.get(i);
			mBgPaints.setColor(Item.Color);
			mSweep = (float) 360 * ((float) Item.Count / (float) mMaxConnection);
			canvas.drawArc(mOvals, mStart, mSweep, true, mBgPaints);
			canvas.drawArc(mOvals, mStart, mSweep, true, mLinePaints);
			mStart += mSweep;
		}

		// ------------------------------------------------------------
		// Draw Legend on Pie
		// ------------------------------------------------------------
		mStart = START_INC;
		float lblX;
		float lblY;
		String LblPercent;
		float Percent;
		DecimalFormat FloatFormatter = new DecimalFormat("0.## %");
		float CenterOffset = (mWidth / 2);
		float Conv = (float) (2 * Math.PI / 360);
		float Radius = 2 * (mWidth / 2) / 3.5f;
		Rect bounds = new Rect();
		for (int i = 0; i < mDataArray.size(); i++) {
			Item = (PieItem) mDataArray.get(i);
			Percent = (float) Item.Count / (float) mMaxConnection;
			mSweep = (float) 360 * Percent;
			// Format Label
			LblPercent = FloatFormatter.format(Percent);
			// Get Label width and height in pixels
			mLinePaints.getTextBounds(LblPercent, 0, LblPercent.length(), bounds);
			// Claculate final coords for Label
			lblX = (float) ((float) CenterOffset + Radius * Math.cos(Conv * (mStart + mSweep / 2))) - bounds.width()
					/ 2;
			lblY = (float) ((float) CenterOffset + Radius * Math.sin(Conv * (mStart + mSweep / 2))) + bounds.height()
					/ 2;
			// Dwraw Label on Canvas
			canvas.drawText(LblPercent, lblX, lblY, mTextPaints);
			mStart += mSweep;
		}

		// ------------------------------------------------------
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		// ------------------------------------------------------
		mState = IS_DRAW;
	}

	// --------------------------------------------------------------------------------------
	public void setGeometry(int width, int height, int GapLeft, int GapRight, int GapTop, int GapBottom) {
		mWidth = width;
		mHeight = height;
		mGapLeft = GapLeft;
		mGapRight = GapRight;
		mGapTop = GapTop;
		mGapBottom = GapBottom;
	}

	// --------------------------------------------------------------------------------------
	public void setSkinParams(int bgColor) {
		mBgColor = bgColor;
	}

	// --------------------------------------------------------------------------------------
	public void setData(List<PieItem> data, int MaxConnection) {
		mDataArray = data;
		mMaxConnection = MaxConnection;
		mState = IS_READY_TO_DRAW;
	}

	// --------------------------------------------------------------------------------------
	public void setState(int State) {
		mState = State;
	}

	// --------------------------------------------------------------------------------------
	public int getColorValue(int Index) {
		if (mDataArray == null)
			return 0;
		if (Index < 0) {
			return ((PieItem) mDataArray.get(0)).Color;
		} else if (Index >= mDataArray.size()) {
			return ((PieItem) mDataArray.get(mDataArray.size() - 1)).Color;
		} else {
			return ((PieItem) mDataArray.get(mDataArray.size() - 1)).Color;
		}
	}
}
