package com.zyao.slippingtab.customviews.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyao.slippingtab.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * TODO
 * Created by Zyao89 on 2016/1/16.
 */
public class TestFragment extends ZyaoBaseFragment
{

	private ArrayList<String> mDatas;
	private RecyclerView      mRecyclerView;
	private HomeAdapter       mAdapter;

	public TestFragment(int index, String tag)
	{
		super(index, tag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_home, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initData();
		mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);

		//		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));//线性管理器，支持横向、纵向。
		//		mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

		//		mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));//网格布局管理器
		mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));

		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));//瀑布就式布局管理器
		// 设置item动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mRecyclerView.setAdapter(mAdapter = new HomeAdapter());


	}

	private void initData()
	{
		mDatas = new ArrayList<String>();
		for (int i = 'A'; i < 'z'; i++)
		{
			mDatas.add("" + (char) i);
		}

	}

	class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
	{

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_home, parent, false));
			return holder;
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position)
		{
			holder.tv.setText(mDatas.get(position));
			ViewGroup.LayoutParams params = holder.tv.getLayoutParams();
			params.height = new Random().nextInt(500) + 200;
			holder.tv.setLayoutParams(params);
		}

		@Override
		public int getItemCount()
		{
			return mDatas.size();
		}

		class MyViewHolder extends RecyclerView.ViewHolder
		{

			TextView tv;

			public MyViewHolder(View view)
			{
				super(view);
				tv = (TextView) view.findViewById(R.id.id_num);
			}
		}
	}

	/**
	 * This class is from the v7 samples of the Android SDK. It's not by me!
	 * <p/>
	 * See the license above for details.
	 */
	public static class DividerItemDecoration extends RecyclerView.ItemDecoration
	{

		private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

		public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

		public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

		private Drawable mDivider;

		private int mOrientation;

		public DividerItemDecoration(Context context, int orientation)
		{
			final TypedArray a = context.obtainStyledAttributes(ATTRS);
			mDivider = a.getDrawable(0);
			a.recycle();
			setOrientation(orientation);
		}

		public void setOrientation(int orientation)
		{
			if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
			{
				throw new IllegalArgumentException("invalid orientation");
			}
			mOrientation = orientation;
		}

		@Override
		public void onDraw(Canvas c, RecyclerView parent)
		{
			Log.v("recyclerview - itemdecoration", "onDraw()");

			if (mOrientation == VERTICAL_LIST)
			{
				drawVertical(c, parent);
			}
			else
			{
				drawHorizontal(c, parent);
			}

		}


		public void drawVertical(Canvas c, RecyclerView parent)
		{
			final int left = parent.getPaddingLeft();
			final int right = parent.getWidth() - parent.getPaddingRight();

			final int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++)
			{
				final View child = parent.getChildAt(i);
				android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int top = child.getBottom() + params.bottomMargin;
				final int bottom = top + mDivider.getIntrinsicHeight();
				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}

		public void drawHorizontal(Canvas c, RecyclerView parent)
		{
			final int top = parent.getPaddingTop();
			final int bottom = parent.getHeight() - parent.getPaddingBottom();

			final int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++)
			{
				final View child = parent.getChildAt(i);
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int left = child.getRight() + params.rightMargin;
				final int right = left + mDivider.getIntrinsicHeight();
				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}

		@Override
		public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent)
		{
			if (mOrientation == VERTICAL_LIST)
			{
				outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
			}
			else
			{
				outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
			}
		}
	}

	/**
	 * @author zhy
	 */
	public static class DividerGridItemDecoration extends RecyclerView.ItemDecoration
	{

		private static final int[] ATTRS = new int[]{R.drawable.list_divider};
		//		private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
		private Drawable mDivider;

		public DividerGridItemDecoration(Context context)
		{
			//			final TypedArray a = context.obtainStyledAttributes(ATTRS);
			//			mDivider = a.getDrawable(0);
			//			a.recycle();
			mDivider = context.getResources().getDrawable(ATTRS[0]);
		}

		@Override
		public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
		{

			drawHorizontal(c, parent);
			drawVertical(c, parent);

		}

		private int getSpanCount(RecyclerView parent)
		{
			// 列数
			int spanCount = -1;
			RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
			if (layoutManager instanceof GridLayoutManager)
			{

				spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
			}
			else if (layoutManager instanceof StaggeredGridLayoutManager)
			{
				spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
			}
			return spanCount;
		}

		public void drawHorizontal(Canvas c, RecyclerView parent)
		{
			int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++)
			{
				final View child = parent.getChildAt(i);
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int left = child.getLeft() - params.leftMargin;
				final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
				final int top = child.getBottom() + params.bottomMargin;
				final int bottom = top + mDivider.getIntrinsicHeight();
				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}

		public void drawVertical(Canvas c, RecyclerView parent)
		{
			final int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++)
			{
				final View child = parent.getChildAt(i);

				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int top = child.getTop() - params.topMargin;
				final int bottom = child.getBottom() + params.bottomMargin;
				final int left = child.getRight() + params.rightMargin;
				final int right = left + mDivider.getIntrinsicWidth();

				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}

		private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount)
		{
			RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
			if (layoutManager instanceof GridLayoutManager)
			{
				if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
				{
					return true;
				}
			}
			else if (layoutManager instanceof StaggeredGridLayoutManager)
			{
				int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
				if (orientation == StaggeredGridLayoutManager.VERTICAL)
				{
					if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
					{
						return true;
					}
				}
				else
				{
					childCount = childCount - childCount % spanCount;
					if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
						return true;
				}
			}
			return false;
		}

		private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount)
		{
			RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
			if (layoutManager instanceof GridLayoutManager)
			{
				childCount = childCount - childCount % spanCount;
				if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
					return true;
			}
			else if (layoutManager instanceof StaggeredGridLayoutManager)
			{
				int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
				// StaggeredGridLayoutManager 且纵向滚动
				if (orientation == StaggeredGridLayoutManager.VERTICAL)
				{
					childCount = childCount - childCount % spanCount;
					// 如果是最后一行，则不需要绘制底部
					if (pos >= childCount)
						return true;
				}
				else
				// StaggeredGridLayoutManager 且横向滚动
				{
					// 如果是最后一行，则不需要绘制底部
					if ((pos + 1) % spanCount == 0)
					{
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent)
		{
			int spanCount = getSpanCount(parent);
			int childCount = parent.getAdapter().getItemCount();
			if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
			{
				outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
			}
			else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
			{
				outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
			}
			else
			{
				outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
			}
		}
	}


}
