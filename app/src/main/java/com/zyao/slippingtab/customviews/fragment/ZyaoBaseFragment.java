package com.zyao.slippingtab.customviews.fragment;

import android.support.v4.app.Fragment;

/**
 * Fragment抽象基类
 */
public abstract class ZyaoBaseFragment extends Fragment
{
	private static final String TAG = "ZyaoBaseFragment";
	protected int    mIndex;
	protected String mTag;

	public ZyaoBaseFragment(int index, String tag)
	{
		this.mIndex = index;
		this.mTag = tag;
		//bundle还可以在每个标签里传送数据
		//		Bundle bundle = new Bundle();
		//		bundle.putInt("INDEX", index);
		//		bundle.putString("TAG", tag);
		//		setArguments(bundle);
	}

	//	@Override
	//	public void onViewCreated(View view, Bundle savedInstanceState)
	//	{
	//		super.onViewCreated(view, savedInstanceState);
	//		Log.d(TAG, "ZyaoBaseFragment -----> onViewCreated");
	////		Bundle args = getArguments();
	////		mIndex = args != null ? args.getInt("INDEX", -1) : -1;
	////		mTag = args != null ? args.getString("TAG") : "";
	//	}

}
