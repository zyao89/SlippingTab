package com.zyao.slippingtab.base;

import android.app.Service;

public abstract class BaseService extends Service
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		
		((CrazyApplication) getApplication()).addService(this);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		((CrazyApplication) getApplication()).removeService(this);
	}
}
