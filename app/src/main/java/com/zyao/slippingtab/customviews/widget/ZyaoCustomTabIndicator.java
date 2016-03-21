package com.zyao.slippingtab.customviews.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyao.slippingtab.R;
import com.zyao.slippingtab.customviews.utils.ZyaoFixedOOMByLruCacheUtil;


public class ZyaoCustomTabIndicator extends RelativeLayout
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

	private int mUnreadCount = 0;
	private Context      mContext;
	private LinearLayout mLinearLayoutFocused; //已选择的图片+文字
	private LinearLayout mLinearLayoutNormal;//为选择的图片+文字
	private ImageView    exploredImageView;//爆炸图

	public ZyaoCustomTabIndicator(Context context)
	{
		this(context, null);
	}

	public ZyaoCustomTabIndicator(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ZyaoCustomTabIndicator(Context context, AttributeSet attrs, int defStyleAttr)
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
		params.setMargins(0, px2Dip(3), px2Dip(5), 0);
		mTvTabUnRead.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		mTvTabUnRead.setTextColor(Color.WHITE);
		mTvTabUnRead.setVisibility(View.INVISIBLE);
		rootLayout.addView(mTvTabUnRead, params);

		/** 爆炸动画 */
		exploredImageView = new ImageView(mContext);
		params.addRule(ALIGN_PARENT_TOP);
		params.addRule(ALIGN_PARENT_RIGHT);
		params.setMargins(0, px2Dip(3), px2Dip(5), 0);
		exploredImageView.setImageResource(R.drawable.tip_anim);
		exploredImageView.setVisibility(View.INVISIBLE);
		rootLayout.addView(exploredImageView, params);
		/** 爆炸动画 */

		this.addView(rootLayout, rootParams);

		setUnread(0);//初始化
	}


	public String getTag()
	{
		return mTag;
	}

	public ZyaoCustomTabIndicator setTag(String tag)
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

	public ZyaoCustomTabIndicator setTextColor(int normalColor, int focusColor)
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

	public ZyaoCustomTabIndicator setTabIcon(int normalId, int focusId)
	{
		this.normalId = normalId;
		this.focusId = focusId;
		if (focusId != -1)
		{
			mIvTabIconFocused.setImageBitmap(ZyaoFixedOOMByLruCacheUtil.with(mContext).load(focusId));
			//            mIvTabIconFocused.setImageResource(focusId);
		}
		if (normalId != -1)
		{
			mIvTabIconNormal.setImageBitmap(ZyaoFixedOOMByLruCacheUtil.with(mContext).load(normalId));
			//            mIvTabIconNormal.setImageResource(normalId);
		}
		return this;
	}

	public ZyaoCustomTabIndicator setTabHint(int hintId)
	{
		mTvTabHintFocused.setText(hintId);
		mTvTabHintNormal.setText(hintId);
		return this;
	}

	public void setUnread(int unreadCount)
	{
		this.mUnreadCount = unreadCount;

		if (unreadCount <= 0)
		{
			mTvTabUnRead.clearAnimation();
			mTvTabUnRead.setVisibility(View.INVISIBLE);
			setMissAlphaAnimation();//逐渐消失
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
			setAlphaScaleAnimation();//放大+透明度（循环）
		}
	}

	public int getmUnreadCount()
	{
		return this.mUnreadCount;
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

	/**
	 * 放大+透明度（循环）
	 */
	private void setAlphaScaleAnimation()
	{
		AlphaAnimation animationAlpha = new AlphaAnimation(0.6f, 1.0f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animationAlpha.setRepeatMode(Animation.REVERSE);
		animationAlpha.setRepeatCount(5);
		scaleAnimation.setRepeatMode(Animation.REVERSE);
		scaleAnimation.setRepeatCount(5);
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(animationAlpha);
		animationSet.addAnimation(scaleAnimation);
		//            animationSet.addAnimation(translateAnimation);
		animationSet.setDuration(1000);
		animationSet.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
				exploredImageView.setVisibility(View.VISIBLE);
				exploredImageView.setImageResource(0);
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}
		});
		mTvTabUnRead.startAnimation(animationSet);

	}

	/**
	 * 逐渐消失
	 */
	private void setMissAlphaAnimation()
	{
		exploredImageView.setImageResource(R.drawable.tip_anim);
		((AnimationDrawable) exploredImageView.getDrawable()).stop();
		((AnimationDrawable) exploredImageView.getDrawable()).start();
	}
}
