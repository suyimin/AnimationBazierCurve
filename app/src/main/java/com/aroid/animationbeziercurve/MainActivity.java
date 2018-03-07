package com.aroid.animationbeziercurve;

import com.aroid.animationbeziercurve.view.BezierLayout;
import com.example.animationbaziercurve.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private BezierLayout mBezierLayout;
	private Button mPiaoYiPiao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBezierLayout = (BezierLayout)findViewById(R.id.bezier_layout);
		mPiaoYiPiao = (Button)findViewById(R.id.piao_yi_piao);
		mPiaoYiPiao.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.piao_yi_piao:
			for (int i = 0; i < 2; i++) {
				mBezierLayout.addBezierView();
			}
			break;

		default:
			break;
		}
		
		
	}

}
