package com.aroid.animationbeziercurve.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.animationbaziercurve.R;

import java.util.Random;

public class BezierLayout extends RelativeLayout {

	// 图片数组
	private Drawable[] loves = new Drawable[3];
	// 图片的宽高
	private int mWidth;
	private int mHeight;
	// 屏幕的宽高
	private int cWidth;
	private int cHeight;
	// 添加到当前view的参数
	private LayoutParams mParams;
	// 随机对象
	private Random mRandom;
	// 渐变动画执行的时间
	private long mPDuration = 500;
	// 贝塞尔执行的时间
	private long mBDuration = 5000;
	//插补器集 用于随机插补器
	Interpolator[] interpolators = new Interpolator[3];



	public BezierLayout(Context context) {
		super(context);
		init();
	}

	public BezierLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BezierLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		cWidth = MeasureSpec.getSize(widthMeasureSpec);
		cHeight = MeasureSpec.getSize(heightMeasureSpec);
	}

	private void init() {
		//初始化插补器
//		interpolators[0] = new CycleInterpolator(0.3f);
//		interpolators[1] = new CycleInterpolator(1);
//		interpolators[1] = new CycleInterpolator(0.3f);
//		interpolators[2] = new CycleInterpolator(0.3f);

//		interpolators[0] = new AccelerateDecelerateInterpolator(); // 加速减速插补器
//		interpolators[1] = new DecelerateInterpolator(); // 减速插补器
//		interpolators[2] = new AnticipateInterpolator(); // 向前插补器
//		interpolators[3] = new AnticipateOvershootInterpolator(); // 向前向后插补器
//		interpolators[4] = new OvershootInterpolator(); // 超出插补器

		mRandom = new Random();
		loves[0] = getResources().getDrawable(R.drawable.love11);
		loves[1] = getResources().getDrawable(R.drawable.love22);
		loves[2] = getResources().getDrawable(R.drawable.love33);
		// 初始化所添加View的宽高
		Drawable drawable = loves[1];
		mWidth = drawable.getIntrinsicWidth() * 2;
		mHeight = drawable.getIntrinsicHeight() * 2;
		mParams = new LayoutParams(mWidth, mHeight);
	}

	/**
	 * 添加一个View.
	 */
	@SuppressLint("NewApi")
	public void addBezierView() {
		ImageView view = new ImageView(getContext());
		int nextInt = mRandom.nextInt(loves.length - 1);
		view.setImageDrawable(loves[nextInt]);
		mParams.addRule(CENTER_IN_PARENT);
		mParams.addRule(ALIGN_PARENT_BOTTOM);
		view.setLayoutParams(mParams);
		addView(view);
		AnimatorSet matorSet = getAnimatorSet(view);
		//设置插补器.
		matorSet.setInterpolator(interpolators[mRandom.nextInt(interpolators.length-1)]);
		matorSet.start();
	}

	/**
	 * 获取一个贝塞尔+平移等动画效果的AnimatorSet;
	 *
	 * @param view
	 * @return
	 */
	@SuppressLint("NewApi")
	private AnimatorSet getAnimatorSet(final ImageView view) {
		// 创建动画
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator trax = ObjectAnimator.ofFloat(view, "scaleX", 0.4f, 1f);
		ObjectAnimator tray = ObjectAnimator.ofFloat(view, "scaleY", 0.4f, 1f);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.4f, 1f);

		// 三个动画一起执行
		AnimatorSet enterSet = new AnimatorSet();
		enterSet.setDuration(mPDuration);
		enterSet.playTogether(trax, tray, alpha);

		// 创建贝塞尔动画
		ValueAnimator bezierAnimator = getBezierAnimator(view);

		// 所有动画一起执行
		set.playSequentially(enterSet, bezierAnimator);
		set.setTarget(view);

		// 给动画添加一个执行的状态监听,当动画执行结束的时候把view释放掉.
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				removeView(view);
				super.onAnimationEnd(animation);
			}
		});
		return set;
	}

	/**
	 * 获取贝塞尔动画
	 *
	 * @param view
	 * @return
	 */
	@SuppressLint("NewApi")
	private ValueAnimator getBezierAnimator(final ImageView view) {

		// 初始化贝塞尔动画的几个点
		PointF pointF0 = new PointF((cWidth - mWidth) / 2, cHeight - mHeight);
		PointF pointF1 = getTogglePointF(1);
		PointF pointF2 = getTogglePointF(2);
		PointF pointF3 = new PointF(mRandom.nextInt(cWidth), 0);

		// 贝塞尔动画的路径由 一个估值器来表示.
		// 获取一个估值器,估值器的点集为pointF1,pointF2;
		BezierEvaluator bezierEvaluator = new BezierEvaluator(pointF1, pointF2);
		ValueAnimator valueAnimator = ValueAnimator.ofObject(bezierEvaluator,
				pointF0, pointF3);
		valueAnimator.setDuration(mBDuration);

		// 给动画添加一个动画的进度监听;在动画执行的过程中动态的改变view的位置;
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				PointF pointF = (PointF) animation.getAnimatedValue();
				view.setX(pointF.x);
				view.setY(pointF.y);

				// 设置view的透明度,达到动画执行过程view逐渐透明效果;
				view.setAlpha(1 - animation.getAnimatedFraction());

			}
		});

		return valueAnimator;
	}

	/**
	 * 生成不同的PointF;
	 *
	 * @param i
	 *            从上到下标记依次为1-....
	 * @return
	 */
	private PointF getTogglePointF(int i) {

		PointF pointF = new PointF();
		pointF.x = mRandom.nextInt(cWidth);
		float nextFloat = mRandom.nextFloat();
		float nextFloat2 = mRandom.nextFloat();

		if (nextFloat > 0.5)
			nextFloat /= 2;

		if (nextFloat2 < 0.5)
			nextFloat2 /= 0.5;

		if (i == 1) {
			pointF.y = (float) (cHeight * nextFloat);
		} else if (i == 2) {
			pointF.y = (float) ((cHeight-mHeight) * nextFloat2);
		}
		return pointF;
	}

}
