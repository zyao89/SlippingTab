package com.zyao.slippingtab.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyao.slippingtab.R;
import com.zyao.slippingtab.base.BaseActivity;
import com.zyao.slippingtab.customviews.ZyaoBaseViewPager;
import com.zyao.slippingtab.customviews.fragment.ZyaoBaseFragment;
import com.zyao.slippingtab.customviews.widget.ZyaoCustomTabIndicator;
import com.zyao.slippingtab.widget.TitleLayout;

/**
 * TODO
 * Created by Zyao89 on 2016/1/11.
 */
public class LoadingActivity extends BaseActivity
{
    private static final String TAB_CHAT     = "chat";
    private static final String TAB_CONTACT  = "contact";
    private static final String TAB_DISCOVER = "discover";
    private static final String TAB_ME       = "me";
    private LinearLayout mRootView;
    private TitleLayout  mTitleLayout;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mRootView = new LinearLayout(this);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        setContentView(mRootView);

        initTitleView();

        ZyaoBaseViewPager customViewPager = new ZyaoBaseViewPager(this)
        {
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
            protected void customTabIndicatorsConfig(ZyaoCustomTabIndicator tabIndicator, int index, String mTag)
            {
                switch (index)
                {
                    case 0:
                        tabIndicator.setTag(TAB_CHAT).setTabIcon(R.drawable.tab_icon_chat_normal, R.drawable.tab_icon_chat_focus).setTabHint(R.string.home_tab_chat).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
                        break;
                    case 1:
                        tabIndicator.setTag(TAB_CONTACT).setTabIcon(R.drawable.tab_icon_contact_normal, R.drawable.tab_icon_contact_focus).setTabHint(R.string.home_tab_contact).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
                        break;
                    case 2:
                        tabIndicator.setTag(TAB_DISCOVER).setTabIcon(R.drawable.tab_icon_discover_normal, R.drawable.tab_icon_discover_focus).setTabHint(R.string.home_tab_discover).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
                        break;
                    case 3:
                        tabIndicator.setTag(TAB_ME).setTabIcon(R.drawable.tab_icon_me_normal, R.drawable.tab_icon_me_focus).setTabHint(R.string.home_tab_me).setTextColor(Color.GRAY, Color.parseColor("#45C01A"));
                        break;
                }
            }

            @Override
            protected ZyaoBaseFragment createCustomContentFragments(int index, String tag)
            {
                return null;
            }


        };

        mRootView.addView(customViewPager);
    }

    private void initTitleView()
    {
        mTitleLayout = new TitleLayout(this);
        mTitleLayout.setTitleLayoutHight(60).setLeftImage(R.drawable.action_unread_icon).setRightImage(R.drawable.action_unread_icon);
        mTitleLayout.setBackgroundColor(Color.WHITE);
        mTitleLayout.setTitleText("CrazyMsg").setTitleTextSize(30).show(mRootView);
        mTitleLayout.setOnclickListener(new TitleLayout.OnClickListener()
        {
            @Override
            public void OnLeftClick(ImageView leftView)
            {
                Toast.makeText(LoadingActivity.this, "OnLeftClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnRightClick(ImageView rightView)
            {
                Toast.makeText(LoadingActivity.this, "OnRightClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
