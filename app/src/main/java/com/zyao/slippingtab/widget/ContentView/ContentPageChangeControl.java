package com.zyao.slippingtab.widget.ContentView;

import android.support.v4.view.ViewPager;

/**
 * TODO
 * Created by Zyao89 on 2016/1/14.
 */
public class ContentPageChangeControl implements ViewPager.OnPageChangeListener
{
	private final ContentViewPager mViewPager;
	private boolean            left               = false;
	private boolean            right              = false;
	private boolean            isScrolling        = false;
	private boolean            isSelected         = false;
	private int                lastValue          = -1;
	private ChangeViewCallback changeViewCallback = null;
	private int                fromPosition       = 0;
	private int                toPosition         = 0;
	private int                currPosition       = 0;


	public ContentPageChangeControl(ContentViewPager viewPager)
	{
		viewPager.addOnPageChangeListener(this);
		mViewPager = viewPager;
	}

	public void addOnPageChangeListener(ChangeViewCallback changeViewCallback)
	{
		this.changeViewCallback = changeViewCallback;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		if (!isScrolling && !isSelected)
		{
			System.out.println("isScrolling --> " + true);
			isScrolling = true;
		}

		if (lastValue > positionOffsetPixels)
		{
			// 递减，向右侧滑动
			right = true;
			left = false;
		}
		else if (lastValue < positionOffsetPixels)
		{
			// 递减，向右侧滑动
			right = false;
			left = true;
		}
		//			else if (lastValue == positionOffsetPixels)
		//			{
		//				right = left = false;
		//			}

		fromPosition = toPosition = position;
		if (left)
		{
			toPosition = position + 1;
		}
		else if (right)
		{
			fromPosition = position + 1;
		}
		else
		{
			toPosition = fromPosition = position;
		}

		if (Math.abs(currPosition - position) > 1 && isSelected)
		{
			if (changeViewCallback != null)
			{
				changeViewCallback.getCurrentPageIndex(currPosition);
			}
			isScrolling = false;
		}
		else
		{
			if (changeViewCallback != null && isScrolling)
			{
				changeViewCallback.onPageScrolled(fromPosition, toPosition, positionOffset, positionOffsetPixels);
			}
		}

		lastValue = positionOffsetPixels;
	}

	@Override
	public void onPageSelected(int position)
	{
		//		if (changeViewCallback != null)
		//		{
		//			changeViewCallback.getCurrentPageIndex(position);
		//		}
		System.out.println("onPageSelected --> " + position);
		//		if (!left && !right)
		//		{
		//			toPosition = fromPosition = position;
		//		}
		isSelected = true;
		currPosition = position;
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		if (state == ViewPager.SCROLL_STATE_DRAGGING)
		{
			System.out.println("isScrolling --> " + true);
			isScrolling = true;
			mViewPager.setViewPagerScrollSpeed(false);
		}
		else if (state == ViewPager.SCROLL_STATE_SETTLING)
		{
			//notify ....
			if (changeViewCallback != null)
			{
				changeViewCallback.changeView(left, right);
			}
			//			right = left = false;
			System.out.println("SCROLL_STATE_SETTLING --> ");
		}
		else if (state == ViewPager.SCROLL_STATE_IDLE)
		{
			System.out.println("isScrolling --> " + false);
			isScrolling = false;
			isSelected = false;

			right = left = false;
			if (changeViewCallback != null)
			{
				changeViewCallback.getCurrentPageIndex(currPosition);
			}
			//			toPosition = fromPosition = currPosition;
		}

	}


	/**
	 * 得到是否向右侧滑动
	 *
	 * @return true 为右滑动
	 */
	public boolean getMoveRight()
	{
		return right;
	}

	/**
	 * 得到是否向左侧滑动
	 *
	 * @return true 为左做滑动
	 */
	public boolean getMoveLeft()
	{
		return left;
	}

}
