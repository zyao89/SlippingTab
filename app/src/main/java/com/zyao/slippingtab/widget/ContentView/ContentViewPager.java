package com.zyao.slippingtab.widget.ContentView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zyao.slippingtab.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 中间内容部分
 * Created by Zyao89 on 2016/1/13.
 */
public class ContentViewPager extends ViewPager
{
	private Context mContext;
	private CallBack            mCallBack            = null;
	private ArrayList<Fragment> mContentFragmentList = new ArrayList<>();
	private FragmentManager mFragmentManager;
	private int mCurrFragmentIndex = 0;
	private ContentPageChangeControl mContentPageChangeControl;

	public ContentViewPager(Context context)
	{
		this(context, null);
	}

	public ContentViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		init(context);
	}

	private void init(Context context)
	{
		this.mContext = context;
		setId(0x9000000);//必须要设置一个ID
		if (context instanceof FragmentActivity)
		{
			mFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		setLayoutParams(params);
		mContentPageChangeControl = new ContentPageChangeControl(this);
	}

	public void setContentFragmentViewsConfig(final CallBack callBack)
	{
		this.mCallBack = callBack;
		if (callBack == null)
		{
			return;
		}
		int count = callBack.getContentFragmentViewCount();
		if (count > 0 && mContentFragmentList.size() < count)
		{
			this.setOffscreenPageLimit(count);
			for (int i = 0; i < count; i++)
			{
				String tag = callBack.getFragmentTag(i);
				Test instantiate = new Test(i, tag);
				mContentFragmentList.add(instantiate);
				callBack.initContentFragmentViews(instantiate, i);
			}
			//给ViewPager设置适配器
			this.setAdapter(new FragmentStatePagerAdapter(mFragmentManager)
			{
				@Override
				public Fragment getItem(int position)
				{
					return mContentFragmentList.get(position);
				}

				@Override
				public int getCount()
				{
					return mContentFragmentList.size();
				}

				//				@Override
				//				public void finishUpdate(ViewGroup container)
				//				{
				//					super.finishUpdate(container);
				//					//获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
				//					int currentItem = getCurrentItem();
				//					if (currentItem == mCurrFragmentIndex)
				//					{
				//						return;
				//					}
				//
				//					callBack.onSelectedCurrPageIndex(currentItem);
				//				}
			});

			switchCurrFragmentIndex(0);//初始化
		}
	}

	public void switchCurrFragmentIndex(int index)
	{
		mCurrFragmentIndex = index;
		setCurrentItem(index);
	}

	public void show(ViewGroup root)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		root.addView(this, params);
	}

	public void addOnPageChangeListener(ChangeViewCallback changeViewCallback)
	{
		mContentPageChangeControl.addOnPageChangeListener(changeViewCallback);
	}


	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	public interface CallBack
	{
		int getContentFragmentViewCount();

		String getFragmentTag(int index);

		void initContentFragmentViews(final Fragment mFragment, int index);

		void switchCurrFragmentIndex(int currentItem);
	}

	public void setViewPagerScrollSpeed(boolean isScroll)
	{
		try
		{
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, isScroll);
			mScroller.set(this, scroller);
		} catch (NoSuchFieldException e)

		{

		} catch (IllegalArgumentException e)

		{

		} catch (IllegalAccessException e)

		{

		}
	}

}
