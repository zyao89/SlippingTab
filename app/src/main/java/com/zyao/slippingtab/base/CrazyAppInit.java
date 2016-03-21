package com.zyao.slippingtab.base;

import android.content.Context;

/**
 * 应用程序初始化
 * Created by Zyao89 on 2016/1/11.
 */
public class CrazyAppInit
{
	private CrazyAppInit() {}

	private static CrazyAppInit mInstance = null;

	public static CrazyAppInit getInstance()
	{
		if (null == mInstance)
		{
			mInstance = new CrazyAppInit();
		}
		return mInstance;
	}

	/**
	 * 应用信息初始化
	 */
	public void init(Context context)
	{

	}
}
