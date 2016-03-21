package com.zyao.slippingtab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.fragment.ZyaoBaseFragment;

/**
 * TODO
 * Created by Zyao89 on 2016/1/11.
 */
public class Test extends ZyaoBaseFragment
{
	private static final String TAG = "TestFragment";

	public Test(int index, String tag)
	{
		super(index, tag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.guide_2, container, false);
		TextView viewhello = (TextView) view.findViewById(R.id.tv);
		viewhello.setText(mTag);
		return view;
	}

}
