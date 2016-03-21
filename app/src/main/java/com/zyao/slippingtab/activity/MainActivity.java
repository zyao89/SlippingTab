package com.zyao.slippingtab.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyao.slippingtab.R;
import com.zyao.slippingtab.customviews.Test;
import com.zyao.slippingtab.customviews.ZyaoBaseViewPager;
import com.zyao.slippingtab.customviews.widget.ZyaoCustomTabIndicator;
import com.zyao.slippingtab.widget.TitleLayout;

/**
 * Description TODO
 * Author Zyao89
 * Created at 2016/1/16 01:30
 */
public class MainActivity extends FragmentActivity implements ZyaoBaseViewPager.OnCustomViewPagerListener
{
    private LinearLayout mRootView;
    private Test         custom;
    private static int i = 0;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mRootView = new LinearLayout(this);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        setContentView(mRootView);

        custom = new Test(this);
        custom.setOnCustomViewPagerListener(this);
        custom.show(mRootView);

        TitleLayout mTitleLayout = new TitleLayout(this);
        mTitleLayout.setTitleLayoutHight(44).setLeftImage(R.drawable.action_unread_icon).setRightImage(R.drawable.action_unread_icon);
        mTitleLayout.setBackgroundColor(Color.WHITE);
        mTitleLayout.setTitleText("CrazyMsg").setTitleTextSize(30).show(mRootView);
        mTitleLayout.setOnclickListener(new TitleLayout.OnClickListener()
        {
            @Override
            public void OnLeftClick(ImageView leftView)
            {
                Toast.makeText(MainActivity.this, "OnLeftClick", Toast.LENGTH_SHORT).show();
            }
        
            @Override
            public void OnRightClick(ImageView rightView)
            {
                Toast.makeText(MainActivity.this, "OnRightClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSelectedCurrPageIndex(int index)
    {
        if (index != 0)//test
        {
            i++;
        }
        else
        {
            i = 0;
        }
        custom.getTabIndicatorList().get(0).setUnread(i);
    }

    @Override
    public void onPageScrollOrientation(int orientation)
    {

    }

    @Override
    public void onCustomTabIndicatorItemClick(ZyaoCustomTabIndicator v)
    {

    }
}
