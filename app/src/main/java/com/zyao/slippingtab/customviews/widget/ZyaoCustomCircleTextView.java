package com.zyao.slippingtab.customviews.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.utils.ZyaoFixedOOMByLruCacheUtil;

/**
 * 消息提示圆
 * Created by Zyao89 on 2016/1/14.
 */
public class ZyaoCustomCircleTextView extends TextView
{
	private Context mContext;
	private static int CustomCircle_ID = ZyaoFixedOOMByLruCacheUtil.Random_ID++;

	private int mSize  = 5;
	private int mColor = Color.RED;

	public ZyaoCustomCircleTextView(Context context)
	{
		this(context, null);
	}

	public ZyaoCustomCircleTextView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ZyaoCustomCircleTextView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context)
	{
		this.mContext = context;
		this.setGravity(Gravity.CENTER);
	}

	@Override
	public void setTextSize(int unit, float size)
	{
		super.setTextSize(unit, size);
		mSize = px2Sp((int) size) * 2;
		drawCircle();
	}

	public void setCircleSize(int size)
	{
		mSize = px2Dip(size);
	}

	public void setCircleColor(int color)
	{
		mColor = color;
	}

	private void drawCircle()
	{
		Bitmap bitmap = ZyaoFixedOOMByLruCacheUtil.with(mContext).getBitmapLruCache(CustomCircle_ID);//读取图片缓存
		if (bitmap == null)
		{
			bitmap = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
			//        // 软引用的Bitmap对象
			//        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);

			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(mColor);
			canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, paint);

			ZyaoFixedOOMByLruCacheUtil.with(mContext).addBitmapLruCache(CustomCircle_ID, bitmap);//添加进入图片缓存
		}

		Log.e("", "CustomCircle_ID --> " + CustomCircle_ID);

		Drawable drawable = new BitmapDrawable(getResources(), bitmap);
		setBackgroundDrawable(drawable);
	}

	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	private int px2Sp(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getResources().getDisplayMetrics());
	}

}
