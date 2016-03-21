package com.zyao.slippingtab.base;

import android.app.IntentService;

public abstract class BaseIntentService extends IntentService
{

	public BaseIntentService(String name)
	{
		super(name);
	}

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
