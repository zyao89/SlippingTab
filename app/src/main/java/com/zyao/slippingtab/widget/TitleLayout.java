package com.zyao.slippingtab.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.utils.ZyaoFixedOOMByLruCacheUtil;

/**
 * 自定义标题栏
 * Created by Zyao89 on 2016/1/13.
 */
public class TitleLayout extends LinearLayout implements View.OnClickListener
{
	private Context   mContext;
	private TextView  mTitleView;
	private ImageView mLeftView;
	private ImageView mRightView;
	//默认高度
	private int mTitleLayoutHight = 44; //dp
	//点击事件监听
	private OnClickListener mOnclickListener;
	
	public TitleLayout(Context context)
	{
		this(context, null);
	}
	
	public TitleLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	private void init(Context context)
	{
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.mContext = context;
		
		this.mTitleView = new TextView(context);
		this.mTitleView.setGravity(Gravity.CENTER);
		LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		this.addView(mTitleView, titleParams);
		LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		this.mLeftView = new ImageView(context);
		this.mLeftView.setPadding(px2Dip(5), 0, px2Dip(5), 0);
		this.addView(mLeftView, 0, imageParams);
		this.mRightView = new ImageView(context);
		this.mRightView.setPadding(px2Dip(5), 0, px2Dip(5), 0);
		this.addView(mRightView, imageParams);

		this.mLeftView.setOnClickListener(this);
		this.mRightView.setOnClickListener(this);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		ViewGroup.LayoutParams params = getLayoutParams();
		params.height = mTitleLayoutHight;
		setLayoutParams(params);
	}

	/** 设置setTitleLayoutHight */
	public TitleLayout setTitleLayoutHight(int hight)
	{
		mTitleLayoutHight = px2Dip(hight);
		return this;
	}

	/** 设置标题 */
	public TitleLayout setTitleText(String title)
	{
		this.mTitleView.setText(title);

		return this;
	}

	/** 设置标题字体大小 */
	public TitleLayout setTitleTextSize(int size)
	{
		this.mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

		return this;
	}

	/** 设置标题 */
	public TitleLayout setTitleText(int titleResId)
	{
		this.mTitleView.setText(titleResId);

		return this;
	}

	/** 设置左键图片 */
	public TitleLayout setLeftImage(int leftResId)
	{
		//		Picasso.with(mContext).load(leftResId).into(mLeftView);
		mLeftView.setImageBitmap(ZyaoFixedOOMByLruCacheUtil.with(mContext).load(leftResId));
		return this;
	}

	/** 设置右键图片 */
	public TitleLayout setRightImage(int rightResId)
	{
		//		Picasso.with(mContext).load(rightResId).into(mRightView);
		mRightView.setImageBitmap(ZyaoFixedOOMByLruCacheUtil.with(mContext).load(rightResId));
		return this;
	}

	public void show(ViewGroup root)
	{
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.height = mTitleLayoutHight;
		root.addView(this, 0, params);
	}

	public void setOnclickListener(OnClickListener listener)
	{
		this.mOnclickListener = listener;
	}

	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	@Override
	public void onClick(View v)
	{
		if (mOnclickListener != null)
		{
			if (v.equals(mLeftView))
			{
				mOnclickListener.OnLeftClick(mLeftView);
			}
			else if (v.equals(mRightView))
			{
				mOnclickListener.OnRightClick(mRightView);
			}
		}
	}

	public interface OnClickListener
	{
		void OnLeftClick(ImageView leftView);

		void OnRightClick(ImageView rightView);
	}

}
