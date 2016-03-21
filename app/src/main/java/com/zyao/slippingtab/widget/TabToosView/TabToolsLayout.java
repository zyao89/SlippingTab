package com.zyao.slippingtab.widget.TabToosView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * 初始化底部工具条
 * Created by Zyao89 on 2016/1/13.
 */
public class TabToolsLayout extends LinearLayout implements View.OnClickListener
{
	private Context mContext;
	private List<TabIndicatorNew> mTabIndicatorList = new LinkedList();
	//工具栏回调
	private CallBack              mCallBack         = null;
	//当前状态
	private TabIndicatorNew       mCurrIndicator    = null;
	//当前工具高度
	private int                   mTitleLayoutHight = 80;
	private int                   mCount            = 0;

	public TabToolsLayout(Context context)
	{
		this(context, null);
	}

	public TabToolsLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public TabToolsLayout(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context)
	{
		this.mContext = context;
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		System.out.println("wojinlaile..... + onLayout");
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		System.out.println("wojinlaile..... + onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setTabIndicatorsConfig(CallBack callBack)
	{
		this.mCallBack = callBack;
		if (callBack == null)
		{
			return;
		}
		mCount = callBack.getTabIndicatorCount();
		if (mCount > 0 && mTabIndicatorList.size() < mCount)
		{
			for (int i = 0; i < mCount; i++)
			{
				TabIndicatorNew tabIndicator = new TabIndicatorNew(mContext);
				if (!mTabIndicatorList.contains(tabIndicator))
				{
					mTabIndicatorList.add(tabIndicator);
				}
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
				tabIndicator.setIndex(i);
				callBack.initTabIndicators(tabIndicator, i);
				tabIndicator.setOnClickListener(this);
				tabIndicator.setPadding(px2Dip(10), 0, px2Dip(10), 0);
				this.addView(tabIndicator, params);
			}
			switchCurrIndicatorIndex(0);//初始化
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v instanceof TabIndicatorNew && mTabIndicatorList.contains(v))
		{
			int index = ((TabIndicatorNew) v).getIndex();
			if (mCallBack != null)
			{
				mCallBack.switchCurrFragmentIndex(index);
			}
			else
			{
				switchCurrIndicatorIndex(index);
			}
		}
	}

	public interface CallBack
	{
		int getTabIndicatorCount();

		void initTabIndicators(final TabIndicatorNew mTabIndicator, int index);

		void switchCurrFragmentIndex(int currentItem);
	}

	/**
	 * 切换页面
	 *
	 * @param index
	 */
	public void switchCurrIndicatorIndex(int index)
	{
		this.mCurrIndicator = mTabIndicatorList.get(index);
		setCurrentTabByTag(this.mCurrIndicator.getTag());
	}

	public void switchCurrIndicatorIndex(String tag)
	{
		this.mCurrIndicator = setCurrentTabByTag(tag);
	}


	public TabIndicatorNew getCurrIndicator()
	{
		return mCurrIndicator;
	}

	public int getCurrIndicatorIndex()
	{
		return mCurrIndicator.getIndex();
	}

	public void show(ViewGroup root)
	{
		MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, px2Dip(5), 0, px2Dip(5));
		root.addView(this, root.getChildCount(), params);
	}

	public void switchIndicator(int fromPosition, int toPositon, float positionOffset)
	{
		if (fromPosition >= 0 && fromPosition < mCount && toPositon >= 0 && toPositon < mCount && positionOffset != 0)
		{
			if (fromPosition > toPositon)
			{
				mTabIndicatorList.get(fromPosition).switchCurrentFocus(positionOffset);
				mTabIndicatorList.get(toPositon).switchCurrentFocus((1 - positionOffset));
			}
			else if (fromPosition < toPositon)
			{
				mTabIndicatorList.get(fromPosition).switchCurrentFocus(1 - positionOffset);
				mTabIndicatorList.get(toPositon).switchCurrentFocus(positionOffset);
			}
			else
			{

			}
		}
	}

	public TabIndicatorNew setCurrentTabByTag(String tag)
	{
		TabIndicatorNew temp = null;
		for (TabIndicatorNew i : mTabIndicatorList)
		{
			if (i.getTag().equals(tag))
			{
				i.setCurrentFocus(true);
				temp = i;
			}
			else
			{
				i.setCurrentFocus(false);
			}
		}
		return temp;
	}

	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}
}
