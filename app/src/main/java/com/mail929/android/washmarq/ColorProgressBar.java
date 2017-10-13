package com.mail929.android.washmarq;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liam on 10/13/17.
 */

public class ColorProgressBar extends View
{
	//dimensions
	int height;
	int width;

	int count = 0;
	int good = 0;
	int moderate = 0;
	int bad = 0;

	public ColorProgressBar(Context context)
	{
		this(context, null);
	}

	public ColorProgressBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		this.good = 5;
		this.moderate = 2;
		this.bad = 3;

		count = good + moderate + bad;
	}

	public void setProgress(int good, int moderate, int bad)
	{
		this.good = good;
		this.moderate = moderate;
		this.bad = bad;

		count = good + moderate + bad;

		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//setup paint to draw background (white)
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);

		double goodPerc = (double) good / count;
		double modPerc = (double) moderate / count;
		double badPerc = (double) bad / count;

		int goodWidth = (int) (goodPerc * width);
		int modWidth = (int) (modPerc * width);
		int badWidth = (int) (badPerc * width);

		System.out.println("Widths: G-" + goodWidth + " M-" + modWidth + " B-" + badWidth);

		paint.setColor(Color.parseColor("#4CAF50"));
		canvas.drawRect(0, 0, goodWidth, height, paint);
		paint.setColor(Color.parseColor("#FFEB3B"));
		canvas.drawRect(goodWidth, 0, goodWidth + modWidth, height, paint);
		paint.setColor(Color.parseColor("#F44336"));
		canvas.drawRect(goodWidth + modWidth, 0, goodWidth + modWidth + badWidth, height, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		//update to new view size
		height = h;
		width = w;
	}
}
