package com.zyao.slippingtab.widget.ContentView;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * TODO
 * Created by Zyao89 on 2016/1/15.
 */
public class FixedSpeedScroller extends Scroller
{
	private int     mDuration          = 0;
	private boolean isNoScrollDuration = false;

	public FixedSpeedScroller(Context context, boolean isNoScrollDuration)
	{
		super(context);
		this.isNoScrollDuration = isNoScrollDuration;
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator)
	{
		super(context, interpolator);
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel)
	{
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy)
	{
		if (isNoScrollDuration)
		{
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
		else
		{
			super.startScroll(startX, startY, dx, dy);
		}
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration)
	{
		if (isNoScrollDuration)
		{
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
		else
		{
			super.startScroll(startX, startY, dx, dy, duration);
		}
	}
}
