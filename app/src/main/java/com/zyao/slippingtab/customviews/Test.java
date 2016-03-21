package com.zyao.slippingtab.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.zyao.slippingtab.R;
import com.zyao.slippingtab.customviews.fragment.TestFragment;
import com.zyao.slippingtab.customviews.fragment.ZyaoBaseFragment;
import com.zyao.slippingtab.customviews.widget.ZyaoCustomTabIndicator;

/**
 * TODO
 * Created by Zyao89 on 2016/1/15.
 */
public class Test extends ZyaoBaseViewPager
{
	private static final String TAB_CHAT     = "chat";
	private static final String TAB_CONTACT  = "contact";
	private static final String TAB_DISCOVER = "discover";
	private static final String TAB_ME       = "me";

	public Test(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public Test(Context context)
	{
		super(context);
	}

	public Test(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected int setItemAllCount()
	{
		return 4;
	}

	@Override
	protected String[] setItemTags(int allCount)
	{
		return new String[]{TAB_CHAT, TAB_CONTACT, TAB_DISCOVER, TAB_ME};
	}

	@Override
	protected void customTabIndicatorsConfig(ZyaoCustomTabIndicator tabIndicator, int index, String tag)
	{
		switch (index)
		{
			case 0:
				tabIndicator.setTabIcon(R.drawable.tab_icon_chat_normal, R.drawable.tab_icon_chat_focus).setTabHint(R.string.home_tab_chat).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
				break;
			case 1:
				tabIndicator.setTabIcon(R.drawable.tab_icon_contact_normal, R.drawable.tab_icon_contact_focus).setTabHint(R.string.home_tab_contact).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
				break;
			case 2:
				tabIndicator.setTabIcon(R.drawable.tab_icon_discover_normal, R.drawable.tab_icon_discover_focus).setTabHint(R.string.home_tab_discover).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
				break;
			case 3:
				tabIndicator.setTabIcon(R.drawable.tab_icon_me_normal, R.drawable.tab_icon_me_focus).setTabHint(R.string.home_tab_me).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
				break;
		}
	}

	@Override
	protected ZyaoBaseFragment createCustomContentFragments(int index, String tag)
	{
		switch (index)
		{
			case 0:
				TestFragment testFragment = new TestFragment(index, tag);
				return testFragment;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
		return null;
	}

}
