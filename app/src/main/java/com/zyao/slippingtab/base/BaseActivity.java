package com.zyao.slippingtab.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		((CrazyApplication) getApplication()).addActivity(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		((CrazyApplication) getApplication()).removeActivity(this);
	}
}
