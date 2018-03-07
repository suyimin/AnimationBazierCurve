package com.aroid.animationbeziercurve.view;

import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.graphics.PointF;

@SuppressLint("NewApi")
public class BezierEvaluator implements TypeEvaluator<PointF> {

	private PointF pointF1;
	private PointF pointF2;

	public BezierEvaluator(PointF pointF1, PointF pointF2) {
		this.pointF1 = pointF1;
		this.pointF2 = pointF2;
	}

	@Override
	public PointF evaluate(float fraction, PointF pointF0, PointF pointF3) {

		PointF pointF = new PointF();
		pointF.x = (float) ((pointF0.x * (Math.pow((1 - fraction), 3))) + 3
				* pointF1.x * fraction * (Math.pow((1 - fraction), 2)) + 3
				* pointF2.x * (Math.pow(fraction, 2) * (1 - fraction)) + pointF3.x
				* (Math.pow(fraction, 3)));

		pointF.y = (float) ((pointF0.y * (Math.pow((1 - fraction), 3))) + 3
				* pointF1.y * fraction * (Math.pow((1 - fraction), 2)) + 3
				* pointF2.y * (Math.pow(fraction, 2) * (1 - fraction)) + pointF3.y
				* (Math.pow(fraction, 3)));

		return pointF;
	}

}
