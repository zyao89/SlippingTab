package com.zyao.slippingtab.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyao.slippingtab.R;
import com.zyao.slippingtab.base.BaseActivity;
import com.zyao.slippingtab.widget.ContentView.ChangeViewCallback;
import com.zyao.slippingtab.widget.ContentView.ContentViewPager;
import com.zyao.slippingtab.widget.TabToosView.TabIndicatorNew;
import com.zyao.slippingtab.widget.TabToosView.TabToolsLayout;
import com.zyao.slippingtab.widget.TitleLayout;

/**
 * 主界面
 * Created by Zyao89 on 2016/1/11.
 */
public class HomeActivity extends BaseActivity
{
	private static final String TAB_CHAT     = "chat";
	private static final String TAB_CONTACT  = "contact";
	private static final String TAB_DISCOVER = "discover";
	private static final String TAB_ME       = "me";

	private LinearLayout     mRootView;
	private TitleLayout      mTitleLayout;
	private TabToolsLayout   mTabToolsLayout;
	private ContentViewPager mContentFragmentView;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		mRootView = new LinearLayout(this);
		mRootView.setOrientation(LinearLayout.VERTICAL);
		setContentView(mRootView);

		initTitleView();

		initContentView();

		initTabToolsView();
	}

	private void initContentView()
	{
		mContentFragmentView = new ContentViewPager(this);
		mContentFragmentView.setContentFragmentViewsConfig(new ContentViewPager.CallBack()
		{
			@Override
			public int getContentFragmentViewCount()
			{
				return 4;
			}

			@Override
			public String getFragmentTag(int index)
			{
				switch (index)
				{
					case 0:
						return TAB_CHAT;
					case 1:
						return TAB_CONTACT;
					case 2:
						return TAB_DISCOVER;
					case 3:
						return TAB_ME;
				}
				return null;
			}

			@Override
			public void initContentFragmentViews(Fragment mFragment, int index)
			{
				switch (index)
				{
					case 0:
						break;
					case 1:
						break;

					case 2:
						break;

					case 3:
						break;

				}
			}

			@Override
			public void switchCurrFragmentIndex(int currentItem)
			{
				switchCurrPageByIndex(currentItem);
			}
		});
		mContentFragmentView.addOnPageChangeListener(new ChangeViewCallback()
		{
			@Override
			public void changeView(boolean left, boolean right)
			{
				System.out.println("left --> " + left + ": right --> " + right);
			}

			@Override
			public void getCurrentPageIndex(int index)
			{
				switchCurrPageByIndex(index);
				System.out.println("getCurrentPageIndex --> " + index);
			}

			@Override
			public void onPageScrolled(int fromPosition, int toPositon, float positionOffset, int positionOffsetPixels)
			{
				System.out.println("fromPosition --> " + fromPosition + ": toPositon --> " + toPositon + ": positionOffset --> " + positionOffset + ": positionOffsetPixels --> " + positionOffsetPixels);
				mTabToolsLayout.switchIndicator(fromPosition, toPositon, positionOffset);
			}
		});
		mContentFragmentView.show(mRootView);
	}

	private void initTabToolsView()
	{
		mTabToolsLayout = new TabToolsLayout(this);
		mTabToolsLayout.setTabIndicatorsConfig(new TabToolsLayout.CallBack()
		{
			@Override
			public int getTabIndicatorCount()
			{
				return 4;
			}

			@Override
			public void initTabIndicators(final TabIndicatorNew mTabIndicator, int index)
			{
				switch (index)
				{
					case 0:
						mTabIndicator.setTag(TAB_CHAT).setTabIcon(R.drawable.tab_icon_chat_normal, R.drawable.tab_icon_chat_focus).setTabHint(R.string.home_tab_chat).setTextColor(Color.BLACK,Color.parseColor("#45C01A"));
						break;
					case 1:
						mTabIndicator.setTag(TAB_CONTACT).setTabIcon(R.drawable.tab_icon_contact_normal, R.drawable.tab_icon_contact_focus).setTabHint(R.string.home_tab_contact).setTextColor(Color.BLACK, Color.parseColor("#45C01A"));
						break;
					case 2:
						mTabIndicator.setTag(TAB_DISCOVER).setTabIcon(R.drawable.tab_icon_discover_normal, R.drawable.tab_icon_discover_focus).setTabHint(R.string.home_tab_discover).setTextColor(Color.BLACK, Color.parseColor("#45C01A"));
						break;
					case 3:
						mTabIndicator.setTag(TAB_ME).setTabIcon(R.drawable.tab_icon_me_normal, R.drawable.tab_icon_me_focus).setTabHint(R.string.home_tab_me).setTextColor(Color.BLACK, Color.parseColor("#45C01A"));
						break;
				}
			}

			@Override
			public void switchCurrFragmentIndex(int currentItem)
			{
				mContentFragmentView.setViewPagerScrollSpeed(true);
				switchCurrPageByIndex(currentItem);
			}

		});
		mTabToolsLayout.show(mRootView);
	}

	private void initTitleView()
	{
		mTitleLayout = new TitleLayout(this);
		mTitleLayout.setTitleLayoutHight(44).setLeftImage(R.drawable.action_unread_icon).setRightImage(R.drawable.action_unread_icon);
		mTitleLayout.setBackgroundColor(Color.MAGENTA);
		mTitleLayout.setTitleText("CrazyMsg").show(mRootView);
		mTitleLayout.setOnclickListener(new TitleLayout.OnClickListener()
		{
			@Override
			public void OnLeftClick(ImageView leftView)
			{
				Toast.makeText(HomeActivity.this, "OnLeftClick", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnRightClick(ImageView rightView)
			{
				Toast.makeText(HomeActivity.this, "OnRightClick", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void switchCurrPageByIndex(int index)
	{
		mContentFragmentView.switchCurrFragmentIndex(index);
		mTabToolsLayout.switchCurrIndicatorIndex(index);
	}
}
