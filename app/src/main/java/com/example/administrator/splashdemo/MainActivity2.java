package com.example.administrator.splashdemo;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

public class MainActivity2 extends FragmentActivity {
	private Handler mHandler = new Handler();
	private ViewStub viewStub;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		final SplashFragment splashFragment = new SplashFragment();
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.frame, splashFragment);
		transaction.commit();
		viewStub = (ViewStub) findViewById(R.id.content_viewstub);
		getWindow().getDecorView().post(new Runnable() {

			@Override
			public void run() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						viewStub.inflate();
					}
				});
			}
		});
		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				mHandler.postDelayed(new DelayRunnable(MainActivity2.this,
						splashFragment), 2500);
			}
		});
	}

	static class DelayRunnable implements Runnable {
		private WeakReference<Context> contextRef;
		private WeakReference<SplashFragment> fragmentRef;

		public DelayRunnable(Context context, SplashFragment splashFragment) {
			contextRef = new WeakReference<Context>(context);
			fragmentRef = new WeakReference<SplashFragment>(splashFragment);
		}

		@Override
		public void run() {
			FragmentActivity context = (FragmentActivity) contextRef.get();
			if (context != null) {
				SplashFragment splashFragment = fragmentRef.get();
				if (splashFragment == null)
					return;
				final FragmentTransaction transaction = context
						.getSupportFragmentManager().beginTransaction();
				transaction.remove(splashFragment);
				transaction.commit();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}

}
