package com.zyao.slippingtab.widget.TabToosView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.widget.ZyaoCustomCircleTextView;


public class TabIndicator extends RelativeLayout
{
	private int                      index;
	private String                   mTag;
	private ImageView                mIvTabIcon;
	private TextView                 mTvTabHint;
	private ZyaoCustomCircleTextView mTvTabUnRead;

	//默认高度
	private int mTabToolsLayoutHight = 54; //dp

	private int focusId = -1, normalId = -1;

	private int unreadCount = 0;
	private Context mContext;

	public TabIndicator(Context context)
	{
		this(context, null);
	}

	public TabIndicator(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context)
	{
		this.setGravity(Gravity.CENTER);
		this.mContext = context;
		initViews();
	}

	private void initViews()
	{
		//根节点
		RelativeLayout rootLayout = new RelativeLayout(mContext);
		int allSize = px2Dip(55);//单个尺寸宽度
		LayoutParams rootParams = new LayoutParams(allSize, ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout linearLayout = new LinearLayout(mContext);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.height = px2Dip(mTabToolsLayoutHight);
		layoutParams.addRule(CENTER_IN_PARENT);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		mIvTabIcon = new ImageView(mContext);
		int iconSize = px2Dip(34);//图片尺寸
		mIvTabIcon.setLayoutParams(new ViewGroup.LayoutParams(iconSize, iconSize));
		linearLayout.addView(mIvTabIcon);
		mTvTabHint = new TextView(mContext);
		mTvTabHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		mTvTabHint.setSingleLine();
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		linearLayout.addView(mTvTabHint, tvParams);
		rootLayout.addView(linearLayout, layoutParams);

		mTvTabUnRead = new ZyaoCustomCircleTextView(mContext);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_TOP);
		params.addRule(ALIGN_PARENT_RIGHT);
		params.setMargins(0, px2Dip(3), 0, 0);
		mTvTabUnRead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		mTvTabUnRead.setTextColor(Color.WHITE);
		rootLayout.addView(mTvTabUnRead, params);

		this.addView(rootLayout, rootParams);

		setUnread(100);
	}


	public String getTag()
	{
		return mTag;
	}

	public TabIndicator setTag(String tag)
	{
		this.mTag = tag;
		return this;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}

	public TabIndicator setTabIcon(int normalId, int focusId)
	{
		this.normalId = normalId;
		this.focusId = focusId;
		return this;
	}

	public TabIndicator setTabHint(int hintId)
	{
		mTvTabHint.setText(hintId);
		return this;
	}

	public void setUnread(int unreadCount)
	{
		this.unreadCount = unreadCount;

		if (unreadCount <= 0)
		{
			mTvTabUnRead.setVisibility(View.GONE);
		}
		else
		{
			if (unreadCount >= 100)
			{
				mTvTabUnRead.setText("99+");
			}
			else
			{
				mTvTabUnRead.setText("" + unreadCount);
			}
			mTvTabUnRead.setVisibility(View.VISIBLE);
		}
	}

	public int getUnreadCount()
	{
		return this.unreadCount;
	}

	public void setCurrentFocus(boolean current)
	{
		if (current)
		{
			if (focusId != -1)
			{
				mIvTabIcon.setImageResource(focusId);
			}
		}
		else
		{
			if (normalId != -1)
			{
				mIvTabIcon.setImageResource(normalId);
			}
		}
	}

	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}
}
