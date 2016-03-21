package com.zyao.slippingtab.widget.TabToosView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.widget.ZyaoCustomCircleTextView;


public class TabIndicatorNew extends RelativeLayout
{
	private int                      index;
	private String                   mTag;
	private ImageView                mIvTabIconFocused;
	private ImageView                mIvTabIconNormal;
	private TextView                 mTvTabHintFocused;
	private TextView                 mTvTabHintNormal;
	private ZyaoCustomCircleTextView mTvTabUnRead;

	//默认高度
	private int mTabToolsLayoutHight = 54; //dp

	private int focusId = -1, normalId = -1;
	private int focusColor = -1, normalColor = -1;

	private int unreadCount = 0;
	private Context      mContext;
	private LinearLayout mLinearLayoutFocused; //已选择的图片+文字
	private LinearLayout mLinearLayoutNormal;//为选择的图片+文字

	public TabIndicatorNew(Context context)
	{
		this(context, null);
	}

	public TabIndicatorNew(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public TabIndicatorNew(Context context, AttributeSet attrs, int defStyleAttr)
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

		FrameLayout frameLayout = new FrameLayout(mContext);
		int iconSize = px2Dip(34);//图片尺寸

		//选择的图片+文字
		mLinearLayoutFocused = new LinearLayout(mContext);
		LayoutParams layoutParamsFocused = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsFocused.height = px2Dip(mTabToolsLayoutHight);
		layoutParamsFocused.addRule(CENTER_IN_PARENT);
		mLinearLayoutFocused.setGravity(Gravity.CENTER);
		mLinearLayoutFocused.setOrientation(LinearLayout.VERTICAL);
		mIvTabIconFocused = new ImageView(mContext);
		mIvTabIconFocused.setLayoutParams(new ViewGroup.LayoutParams(iconSize, iconSize));
		mLinearLayoutFocused.addView(mIvTabIconFocused);
		mTvTabHintFocused = new TextView(mContext);
		mTvTabHintFocused.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		mTvTabHintFocused.setSingleLine();
		mLinearLayoutFocused.addView(mTvTabHintFocused, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		frameLayout.addView(mLinearLayoutFocused, layoutParamsFocused);

		//未选择的图片+文字
		mLinearLayoutNormal = new LinearLayout(mContext);
		LayoutParams layoutParamsNormal = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsNormal.height = px2Dip(mTabToolsLayoutHight);
		layoutParamsNormal.addRule(CENTER_IN_PARENT);
		mLinearLayoutNormal.setGravity(Gravity.CENTER);
		mLinearLayoutNormal.setOrientation(LinearLayout.VERTICAL);
		mIvTabIconNormal = new ImageView(mContext);
		mIvTabIconNormal.setLayoutParams(new ViewGroup.LayoutParams(iconSize, iconSize));
		mLinearLayoutNormal.addView(mIvTabIconNormal);
		mTvTabHintNormal = new TextView(mContext);
		mTvTabHintNormal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		mTvTabHintNormal.setSingleLine();
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLinearLayoutNormal.addView(mTvTabHintNormal, tvParams);
		frameLayout.addView(mLinearLayoutNormal, layoutParamsNormal);

		rootLayout.addView(frameLayout, layoutParamsNormal);

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

	public TabIndicatorNew setTag(String tag)
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

	public TabIndicatorNew setTextColor(int normalColor, int focusColor)
	{
		this.normalColor = normalColor;
		this.focusColor = focusColor;
		if (focusColor != -1)
		{
			mTvTabHintFocused.setTextColor(focusColor);
		}
		if (normalColor != -1)
		{
			mTvTabHintNormal.setTextColor(normalColor);
		}
		return this;
	}

	public TabIndicatorNew setTabIcon(int normalId, int focusId)
	{
		this.normalId = normalId;
		this.focusId = focusId;
		if (focusId != -1)
		{
			mIvTabIconFocused.setImageResource(focusId);
		}
		if (normalId != -1)
		{
			mIvTabIconNormal.setImageResource(normalId);
		}
		return this;
	}

	public TabIndicatorNew setTabHint(int hintId)
	{
		mTvTabHintFocused.setText(hintId);
		mTvTabHintNormal.setText(hintId);
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
			mLinearLayoutFocused.setAlpha(1);
			mLinearLayoutNormal.setAlpha(0);
		}
		else
		{
			mLinearLayoutFocused.setAlpha(0);
			mLinearLayoutNormal.setAlpha(1);
		}
	}

	public void switchCurrentFocus(float alpha)
	{
		mLinearLayoutFocused.setAlpha(alpha);
		mLinearLayoutNormal.setAlpha(1 - alpha);
	}

	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}
}
