package com.zyao.slippingtab.widget.ContentView;

/**
 * TODO
 * Created by Zyao89 on 2016/1/14.
 */
public interface ChangeViewCallback
{
	void changeView(boolean left, boolean right);

	void getCurrentPageIndex(int index);

	void onPageScrolled(int fromPosition, int toPositon, float positionOffset, int positionOffsetPixels);

}
