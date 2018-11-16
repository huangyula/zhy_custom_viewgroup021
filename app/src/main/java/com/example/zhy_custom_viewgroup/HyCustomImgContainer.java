package com.example.zhy_custom_viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class HyCustomImgContainer extends ViewGroup
{
	private static final String TAG="CustomImgContainer";

	public HyCustomImgContainer(Context context) {
		super(context);
	}

	public HyCustomImgContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HyCustomImgContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 计算所以ChildView的宽度和高度，然后根据ChildView的计算
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//疑问，为什么这里要去掉super.
		//super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		/**
		 * 获得此ViewGroup上级容器为其推荐的宽和高,以及计算模式
		 */
		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth=MeasureSpec.getSize(widthMeasureSpec);
		int heightSize=MeasureSpec.getSize(heightMeasureSpec);

		//计算出所有childView的宽和高
		measureChildren(widthMeasureSpec,heightMeasureSpec);

		/**
		 * 记录如果是wrap_content是设置的宽和高
		 */
		int width=0;
		int height=0;

		int count=getChildCount();

		int cWidth=0;
		int cHeight=0;

		MarginLayoutParams cParams=null;

		//用于计算左边两个childView的高度
		int lHeight=0;
		//用于计算右边两个childView的高度
		int rHeight=0;
		//用于计算上边两个childView的宽度
		int tWidth=0;
		//用于计算下面两个childView的宽度
		int bWidth=0;

		/**
		 * 根据childView计算出的宽和高，以及设置margin计算容器的宽和高，只要用于容器是wrap_content时
		 */
		for(int i=0;i<count;i++){
			View childView=getChildAt(i);
			cWidth=childView.getMeasuredWidth();
			cHeight=childView.getMeasuredHeight();
			cParams=(MarginLayoutParams)childView.getLayoutParams();

			if(i==0||i==1){
				tWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
			}
			if(i==2||i==3){
				bWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
			}
			if(i==0||i==2){
				lHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
			}
			if(i==1||i==3){
				rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}

			width=Math.max(tWidth,bWidth);
			height=Math.max(lHeight,rHeight);

			/**
			 * 如果是wrap_content设置为我们计算的值
			 * 否则：直接设置为父容器计算的值
			 */
			setMeasuredDimension((widthMode==MeasureSpec.EXACTLY?sizeWidth:width),(heightMode==MeasureSpec.EXACTLY)?heightSize:height);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cCount=getChildCount();
		int cWidth=0;
		int cHeight=0;
		MarginLayoutParams cParams=null;

		/**
		 * 遍历所有childView根据其宽和高，以及margin进行布局
		 */
		for(int i=0;i<cCount;i++){
			View childView=getChildAt(i);
			cWidth=childView.getMeasuredWidth();
			cHeight=childView.getMeasuredHeight();
			cParams=(MarginLayoutParams)childView.getLayoutParams();

			int cl = 0, ct = 0, cr = 0, cb = 0;

			switch (i){
				case 0:
					cl=cParams.leftMargin;
					ct=cParams.topMargin;
					break;
				case 1:
					cl=getWidth()-cWidth-cParams.leftMargin-cParams.rightMargin;
					ct = cParams.topMargin;
					break;
				case 2:
					cl = cParams.leftMargin;
					ct = getHeight() - cHeight - cParams.bottomMargin;
					break;
				case 3:
					cl = getWidth() - cWidth - cParams.leftMargin
							- cParams.rightMargin;
					ct = getHeight() - cHeight - cParams.bottomMargin;
					break;
			}
			cr=cl+cWidth;
			cb=ct+cHeight;
			childView.layout(cl,ct,cr,cb);
		}
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
//		return super.generateDefaultLayoutParams();
		return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}
}
