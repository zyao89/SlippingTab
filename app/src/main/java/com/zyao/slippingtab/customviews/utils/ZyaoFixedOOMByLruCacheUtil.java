package com.zyao.slippingtab.customviews.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.InputStream;

/**
 * LruCache进行图片管理
 * Created by Zyao89 on 2016/1/16.
 */
public class ZyaoFixedOOMByLruCacheUtil
{
	public static int Random_ID = 0x0100; //随机递增id

	private static final String                     TAG     = "ZyaoFixedOOMByLruCache";
	private static       ZyaoFixedOOMByLruCacheUtil mSingle = null;
	private Context  mContext;
	/** 缓存图片信息 */
	private LruCache mBitmapCache;


	private ZyaoFixedOOMByLruCacheUtil(Context context)
	{
		this.mContext = context;
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		Log.i(TAG, "LruCache maxMemory --> " + maxMemory);
		this.mBitmapCache = new LruCache(maxMemory);
	}

	public static ZyaoFixedOOMByLruCacheUtil with(Context context)
	{
		if (null == mSingle)
		{
			mSingle = new ZyaoFixedOOMByLruCacheUtil(context);
		}
		return mSingle;
	}

	/**
	 * 从缓存列表中拿出来
	 *
	 * @param resId
	 * @return bitmap
	 */
	public Bitmap load(Integer resId)
	{
		return getBitmapLruCache(resId);
	}

	/**
	 * 从缓存列表中拿出来，如果没有自动添加
	 *
	 * @param resId
	 * @return
	 */
	public Bitmap getBitmapLruCache(Integer resId)
	{
		Log.w(TAG, "getBitmapLruCache --> " + resId);
		if (mBitmapCache.get(resId) == null)
		{
			addBitmapLruCache(resId);
		}
		return (Bitmap) mBitmapCache.get(resId);
	}

	/**
	 * 添加进入缓存列表
	 *
	 * @param resId
	 * @param value
	 */
	public void addBitmapLruCache(Integer resId, Bitmap value)
	{
		Log.w(TAG, "addBitmapLruCache --> " + resId);
		mBitmapCache.put(resId, value);
	}

	/**
	 * 加载并添加进入缓存列表
	 *
	 * @param resId
	 */
	public void addBitmapLruCache(Integer resId)
	{
		addBitmapLruCache(resId, -1);
	}

	/**
	 * 加载、压缩并添加进入缓存列表
	 *
	 * @param resId
	 * @param size
	 */
	public void addBitmapLruCache(Integer resId, int size)
	{
		Log.w(TAG, "addBitmapLruCache --> " + resId);
		Bitmap bitmap = readBitMap(mContext, resId, size);
		if (bitmap != null)
		{
			mBitmapCache.put(resId, bitmap);
		}
	}

	/**
	 * 图片处理器
	 *
	 * @param context 上下文
	 * @param resId   资源图片ID
	 * @param size    压缩目标大小
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId, int size)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		//获取资源图片
		InputStream is;
		try
		{
			is = context.getResources().openRawResource(resId);
		} catch (Exception e)
		{
			new RuntimeException(e);
			return null;
		}

		if (size != -1)
		{
			BitmapFactory.decodeStream(is, null, opt);
			//计算缩放比
			int be = opt.outHeight / size;
			if (be <= 0)
			{
				be = 1;
			}
			opt.inSampleSize = be;
		}

		opt.inJustDecodeBounds = false;//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 图片处理器（不压缩）
	 *
	 * @param context 上下文
	 * @param resId   资源ID
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId)
	{
		return readBitMap(context, resId, -1);
	}

	/**
	 * 图片加载
	 *
	 * @param url  文件路径
	 * @param size 压缩目标大小
	 * @return
	 */
	public static Bitmap readBitMap(String url, int size)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		BitmapFactory.decodeFile(url, opt); //此时返回bm为空
		opt.inJustDecodeBounds = false;//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		//计算缩放比
		int be = opt.outHeight / size;
		if (be <= 0)
		{
			be = 1;
		}
		opt.inSampleSize = be;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeFile(url, opt); //此时返回bm为空;
	}
}
