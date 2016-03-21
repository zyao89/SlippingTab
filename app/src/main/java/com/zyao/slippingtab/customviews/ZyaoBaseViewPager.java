package com.zyao.slippingtab.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.zyao.slippingtab.customviews.fragment.ZyaoBaseFragment;
import com.zyao.slippingtab.customviews.utils.ZyaoFixedOOMByLruCacheUtil;
import com.zyao.slippingtab.customviews.widget.ZyaoCustomTabIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 自定义带工具条的ViewPager
 * Created by Zyao89 on 2016/1/15.
 */
public abstract class ZyaoBaseViewPager extends RelativeLayout implements View.OnClickListener
{
	/** 上下文（FragmentActivity） */
	private Context mContext;
	/** 总共item数量 */
	private int mAllCount      = 0;
	/** 当前页索引 */
	private int mCurrPageIndex = -1;
	/** 工具条指示器IndicatorList集合 */
	private ArrayList<ZyaoCustomTabIndicator> mTabIndicatorList;
	/** FragmentList集合 */
	private ArrayList<Fragment>               mContentFragmentList;
	/** 中间内容容器ViewPager */
	private ViewPager                         mCenterContentViewPager;
	private int mCenterContentViewPager_ID = ZyaoFixedOOMByLruCacheUtil.Random_ID++;
	/** 工具条LinearLayout */
	private LinearLayout mTabToolsLinearLayout;
	private int mTabToolsLinearLayout_ID = ZyaoFixedOOMByLruCacheUtil.Random_ID++;
	/** Fragment管理 */
	private FragmentManager           mFragmentManager;
	/** Tag标签数组 */
	private String[]                  mTags;
	/** 监听事件回调 */
	private OnCustomViewPagerListener mListener;
	/** 分割背景线ID */
	private static int CustomSplitLine_ID = ZyaoFixedOOMByLruCacheUtil.Random_ID++;

	public ZyaoBaseViewPager(Context context)
	{
		this(context, null);
	}

	public ZyaoBaseViewPager(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ZyaoBaseViewPager(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context)
	{
		this.mContext = context;
		//		this.setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		initCustomViewPagerConfig();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		//绘制工具条分割线(灰色)
		createTabToolsTopLine(Color.GRAY, mTabToolsLinearLayout.getMeasuredHeight());
	}

	private void initCustomViewPagerConfig()
	{
		this.mAllCount = setItemAllCount();
		if (mAllCount > 0)
		{
			mTags = setItemTags(mAllCount);
			if (mTags == null || mTags.length != mAllCount)
			{
				mTags = new String[mAllCount];
			}

			createCenterContentViewPager();//创建中间内容
			createTabToolsLayout();//创建工具条

			setCurrentPageByIndex(0);//初始化
		}
	}

	/**
	 * 创建中间内容ViewPager
	 */
	private void createCenterContentViewPager()
	{
		mCenterContentViewPager = new ViewPager(mContext);
		mCenterContentViewPager.setId(mCenterContentViewPager_ID);//必须要设置一个ID
		if (mContext instanceof FragmentActivity)
		{
			mFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		}
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(ABOVE, mTabToolsLinearLayout_ID);
		this.addView(mCenterContentViewPager, params);

		createCenterContentFragments();//初始化Fragment
	}

	/**
	 * 创建中间Fragment
	 */
	private void createCenterContentFragments()
	{
		if (mCenterContentViewPager == null)
		{
			return;
		}
		mContentFragmentList = new ArrayList<>();
		mCenterContentViewPager.setOffscreenPageLimit(mAllCount);//设置缓存页数
		for (int i = 0; i < mAllCount; i++)
		{
			ZyaoBaseFragment instantiate = createCustomContentFragments(i, mTags[i]);
			if (instantiate == null)
			{
				instantiate = new ZyaoCustomFragment(i, mTags[i]);
			}
			mContentFragmentList.add(instantiate);
		}
		//给ViewPager设置适配器
		mCenterContentViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager)
		{
			@Override
			public Fragment getItem(int position)
			{
				return mContentFragmentList.get(position);
			}

			@Override
			public int getCount()
			{
				return mContentFragmentList.size();
			}

		});
		//给ViewPager设置监听
		mCenterContentViewPager.addOnPageChangeListener(new CustomPageChangeListener());
	}


	/**
	 * 创建工具条
	 */

	private void createTabToolsLayout()
	{
		this.mTabToolsLinearLayout = new LinearLayout(mContext);
		mTabToolsLinearLayout.setId(mTabToolsLinearLayout_ID);
		mTabToolsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabToolsLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//        params.setMargins(0, px2Dip(5), 0, px2Dip(5));
		mTabToolsLinearLayout.setPadding(0, px2Dip(3), 0, px2Dip(3));
		params.addRule(ALIGN_PARENT_BOTTOM);
		this.addView(mTabToolsLinearLayout, getChildCount(), params);

		createTabIndicators();//初始化工具条Item
	}

	/**
	 * 创建背景分割线
	 */
	private void createTabToolsTopLine(int color, int width)
	{
		Bitmap bitmap = ZyaoFixedOOMByLruCacheUtil.with(mContext).getBitmapLruCache(CustomSplitLine_ID);
		if (bitmap == null)
		{
			bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels, width, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(color);
			canvas.drawRect(0, 0, bitmap.getWidth(), px2Dip(1), paint);

			ZyaoFixedOOMByLruCacheUtil.with(mContext).addBitmapLruCache(CustomSplitLine_ID, bitmap);
		}
		//        canvas.drawLine(0, 0, bitmap.getWidth(), 0, paint);
		Drawable drawable = new BitmapDrawable(getResources(), bitmap);
		mTabToolsLinearLayout.setBackgroundDrawable(drawable);
	}

	/**
	 * 创建工具条Item
	 */
	private void createTabIndicators()
	{
		if (mTabToolsLinearLayout == null)
		{
			return;
		}
		mTabIndicatorList = new ArrayList<>();
		for (int i = 0; i < mAllCount; i++)
		{
			ZyaoCustomTabIndicator tabIndicator = new ZyaoCustomTabIndicator(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
			tabIndicator.setIndex(i);
			tabIndicator.setTag(mTags[i]);
			tabIndicator.setOnClickListener(this);//点击事件
			tabIndicator.setPadding(px2Dip(10), 0, px2Dip(10), 0);
			mTabToolsLinearLayout.addView(tabIndicator, params);
			mTabIndicatorList.add(tabIndicator);

			customTabIndicatorsConfig(tabIndicator, i, mTags[i]);//配置参数
		}

	}

	/**
	 * 获取总页数
	 *
	 * @return 总数
	 */
	public int getAllCount()
	{
		return mAllCount;
	}

	/**
	 * 获取所有标签
	 *
	 * @return 标签数组
	 */
	public String[] getTags()
	{
		return mTags;
	}

	/**
	 * 获取当前页标签
	 *
	 * @return 标签
	 */
	public String getCurrPageTag()
	{
		return mTags[mCurrPageIndex];
	}

	/**
	 * 按索引获取标签
	 *
	 * @param index 当前索引
	 * @return 标签
	 */
	public String getPageTagByIndex(int index)
	{
		return mTags[index];
	}

	/**
	 * 获取当前页索引
	 *
	 * @return 索引
	 */
	public int getCurrPageIndex()
	{
		return mCurrPageIndex;
	}

	/**
	 * 获取中间内容FragmentList集合
	 *
	 * @return 中间内容FragmentList集合
	 */
	public ArrayList<Fragment> getContentFragmentList()
	{
		return mContentFragmentList;
	}

	/**
	 * 获取指示器IndicatorList集合
	 *
	 * @return 指示器IndicatorList集合
	 */
	public ArrayList<ZyaoCustomTabIndicator> getTabIndicatorList()
	{
		return mTabIndicatorList;
	}

	/**
	 * 设置当前页
	 *
	 * @param index 索引
	 */
	public void setCurrentPageByIndex(int index)
	{
		if (mCurrPageIndex == index)
		{
			return;
		}

		this.mCurrPageIndex = index;

		for (ZyaoCustomTabIndicator i : mTabIndicatorList)
		{
			if (i.getIndex() == index)
			{
				i.setCurrentFocus(true);
			}
			else
			{
				i.setCurrentFocus(false);
			}
		}

		if (mCenterContentViewPager != null)
		{
			mCenterContentViewPager.setCurrentItem(index);
		}

		if (mListener != null)
		{
			mListener.onSelectedCurrPageIndex(index);
		}
	}


	/**
	 * 设置当前工具条状态
	 *
	 * @param tag 标签
	 */
	public void setCurrentPageByTag(String tag)
	{
		for (int i = 0; i < mTags.length; i++)
		{
			if (mTags[i] == tag)
			{
				setCurrentPageByIndex(i);
				break;
			}
		}
	}

	/** 页面滚动方向枚举 */
	public static final int ORIENTATION_LEFT      = 0x01;//左
	public static final int ORIENTATION_RIGHT     = 0x02;//右
	public static final int ORIENTATION_NO_SCROLL = 0x00;//没滚

	/**
	 * 自定义页面改变监听
	 */
	class CustomPageChangeListener implements ViewPager.OnPageChangeListener
	{
		private int     currOrientation = ORIENTATION_NO_SCROLL;//当前方向
		private boolean isScrolling     = false;//是否滑动
		private boolean isSelected      = false;//是否点击
		private int     lastValue       = -1;
		private int     fromPosition    = 0;//起始位置
		private int     toPosition      = 0;//结束位置
		private int     currPosition    = 0;//当前位置

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{
			if (!isScrolling)
			{
				//				System.out.println("isScrolling --> " + true);
				isScrolling = true;
			}

			if (lastValue > positionOffsetPixels)
			{
				// 递减，向右侧滑动
				currOrientation = ORIENTATION_RIGHT;
			}
			else if (lastValue < positionOffsetPixels)
			{
				// 递减，向右侧滑动
				currOrientation = ORIENTATION_LEFT;
			}
			else if (lastValue == positionOffsetPixels)
			{
				currOrientation = ORIENTATION_NO_SCROLL;
			}

			fromPosition = toPosition = position;
			switch (currOrientation)
			{
				case ORIENTATION_LEFT:
					toPosition = position + 1;
					break;
				case ORIENTATION_RIGHT:
					fromPosition = position + 1;
					break;
				case ORIENTATION_NO_SCROLL:
					toPosition = fromPosition = position;
					break;
			}

			if (Math.abs(currPosition - position) > 1 && isSelected)
			{
				setCurrentPageByIndex(currPosition);
				isScrolling = false;
			}
			else
			{
				if (isScrolling)
				{
					switchIndicator(fromPosition, toPosition, positionOffset, positionOffsetPixels);
				}
			}

			lastValue = positionOffsetPixels;
		}

		@Override
		public void onPageSelected(int position)
		{
			isSelected = true;
			currPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (state == ViewPager.SCROLL_STATE_DRAGGING)
			{
				System.out.println("isScrolling --> " + true);
				isScrolling = true;
				setViewPagerScrollNoSpeed(false);//页面滚动
			}
			else if (state == ViewPager.SCROLL_STATE_SETTLING)
			{
				//notify ....
				if (mListener != null)
				{
					mListener.onPageScrollOrientation(currOrientation);//设置当前滚动方向
				}
				System.out.println("SCROLL_STATE_SETTLING --> ");
			}
			else if (state == ViewPager.SCROLL_STATE_IDLE)
			{
				System.out.println("isScrolling --> " + false);
				isScrolling = false;
				isSelected = false;

				//				right = left = false;
				currOrientation = ORIENTATION_NO_SCROLL;
				//设置当前页面
				setCurrentPageByIndex(currPosition);
			}
		}

		/**
		 * 工具类滑动切换
		 *
		 * @param fromPosition         起始位置
		 * @param toPosition           结束位置
		 * @param positionOffset       偏移量
		 * @param positionOffsetPixels 绝对偏移量
		 */
		private void switchIndicator(int fromPosition, int toPosition, float positionOffset, float positionOffsetPixels)
		{
			if (fromPosition >= 0 && fromPosition < mAllCount && toPosition >= 0 && toPosition < mAllCount && positionOffset != 0)
			{
				if (fromPosition > toPosition)
				{
					mTabIndicatorList.get(fromPosition).switchCurrentFocus(positionOffset);
					mTabIndicatorList.get(toPosition).switchCurrentFocus((1 - positionOffset));
				}
				else if (fromPosition < toPosition)
				{
					mTabIndicatorList.get(fromPosition).switchCurrentFocus(1 - positionOffset);
					mTabIndicatorList.get(toPosition).switchCurrentFocus(positionOffset);
				}
			}
		}

	}

	@Override
	public void onClick(View v)
	{
		if (v instanceof ZyaoCustomTabIndicator && mTabIndicatorList.contains(v))
		{
			int index = ((ZyaoCustomTabIndicator) v).getIndex();
			setViewPagerScrollNoSpeed(true);//页面不滚动

			if (index != mCurrPageIndex)
				setCurrentPageByIndex(index);

			if (mListener != null)
			{
				mListener.onCustomTabIndicatorItemClick((ZyaoCustomTabIndicator) v);
			}
		}
	}

	/**
	 * 设置页面是否滚动
	 *
	 * @param isNoScrollDuration true-不滚，false-滚
	 */
	private void setViewPagerScrollNoSpeed(boolean isNoScrollDuration)
	{
		try
		{
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedCustomScroller scroller = new FixedSpeedCustomScroller(mContext, isNoScrollDuration);
			mScroller.set(mCenterContentViewPager, scroller);
		} catch (Exception e)
		{
			e.printStackTrace();
			new RuntimeException(e);
		}
	}

	/**
	 * 自定义滚动条，修复点击速度
	 */
	class FixedSpeedCustomScroller extends Scroller
	{
		private int     mDuration          = 0;
		private boolean isNoScrollDuration = false;

		public FixedSpeedCustomScroller(Context context, boolean isNoScrollDuration)
		{
			super(context);
			this.isNoScrollDuration = isNoScrollDuration;
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy)
		{
			if (isNoScrollDuration)
			{
				super.startScroll(startX, startY, dx, dy, mDuration);
			}
			else
			{
				super.startScroll(startX, startY, dx, dy);
			}
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration)
		{
			if (isNoScrollDuration)
			{
				super.startScroll(startX, startY, dx, dy, mDuration);
			}
			else
			{
				super.startScroll(startX, startY, dx, dy, duration);
			}
		}
	}

	/**
	 * 内部类创建默认Fragment(默认测试类)
	 * Created by Zyao89 on 2016/1/11.
	 */
	protected class ZyaoCustomFragment extends ZyaoBaseFragment
	{
		private static final String TAG = "ZyaoCustomFragment";

		public ZyaoCustomFragment(int index, String tag)
		{
			super(index, tag);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			ScrollView scrollView = new ScrollView(getContext());
			scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
			scrollView.setBackgroundColor(Color.parseColor("#158684"));
			return scrollView;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			super.onViewCreated(view, savedInstanceState);

			LinearLayout linearLayout = new LinearLayout(getContext());
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
			((ScrollView) view).addView(linearLayout);

			TextView viewhello = new TextView(getContext());
			viewhello.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
			viewhello.setTextColor(Color.WHITE);
			viewhello.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px2Dip(800)));
			viewhello.setGravity(Gravity.CENTER);
			viewhello.setText(mTag);
			linearLayout.addView(viewhello);
		}
	}


	private int px2Dip(int px)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}


	/**
	 * 设置数量
	 *
	 * @return 数量
	 */
	protected abstract int setItemAllCount();//数量

	/**
	 * 设置条目标签
	 *
	 * @return 标签数组
	 */
	protected abstract String[] setItemTags(final int allCount);//设置条目标签

	/**
	 * 初始化自定义Indicator配置参数
	 *
	 * @param tabIndicator 每个Item
	 * @param index        当前Item的脚标
	 * @param tag          当前Item的Tab标签
	 */
	protected abstract void customTabIndicatorsConfig(final ZyaoCustomTabIndicator tabIndicator, final int index, String tag);

	/**
	 * 创建CustomFragment
	 *
	 * @param index 当前Fragment的脚标
	 * @param tag   当前Fragment的标签
	 * @return 创建完成的Fragment
	 */
	protected abstract ZyaoBaseFragment createCustomContentFragments(final int index, final String tag);

	/**
	 * 设置监听
	 *
	 * @param listener 监听回调
	 */
	public void setOnCustomViewPagerListener(OnCustomViewPagerListener listener)
	{
		this.mListener = listener;
	}

	/**
	 * ZyaoCustomViewPager监听事件
	 */
	public interface OnCustomViewPagerListener
	{
		/**
		 * 切换当前页面
		 *
		 * @param index 当前索引
		 */
		void onSelectedCurrPageIndex(int index);

		/**
		 * 当前页面滚动方向
		 *
		 * @param orientation {ORIENTATION_LEFT,ORIENTATION_RIGHT,ORIENTATION_NO_SCROLL}
		 */
		void onPageScrollOrientation(int orientation);

		/**
		 * 工具条View的点击事件
		 *
		 * @param v item
		 */
		void onCustomTabIndicatorItemClick(ZyaoCustomTabIndicator v);
	}

	/**
	 * 显示自己
	 *
	 * @param root 根节点
	 */
	public void show(ViewGroup root)
	{
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		root.addView(this, root.getChildCount(), params);
	}
}
