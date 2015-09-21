package de.jeisfeld.augendiagnoselib.activities;

import com.admarvel.android.ads.AdMarvelView;

import android.app.Activity;
import android.view.View;
import android.view.ViewManager;
import de.jeisfeld.augendiagnoselib.Application;
import de.jeisfeld.augendiagnoselib.Application.AuthorizationLevel;
import de.jeisfeld.augendiagnoselib.R;
import de.jeisfeld.augendiagnoselib.util.AdMarvelUtil;
import de.jeisfeld.augendiagnoselib.util.PreferenceUtil;

/**
 * An activity showing AdMarvel ads.
 */
public abstract class AdMarvelActivity extends Activity {
	/**
	 * The view holding the ad.
	 */
	private AdMarvelView mAdMarvelView;

	/**
	 * Display a banner ad.
	 */
	public final void requestBannerAdIfEligible() {
		getAdMarvelView();
		AdMarvelUtil.requestBannerAdIfEligible(this);
	}

	/**
	 * Delete the ad view.
	 */
	protected final void deleteAdMarvelView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					((ViewManager) mAdMarvelView.getParent()).removeView(mAdMarvelView);
				}
				catch (Exception e) {
					// do nothing.
				}
			}
		});
	}

	// OVERRIDABLE
	@Override
	protected void onStart() {
		super.onStart();
		if (mAdMarvelView != null && mAdMarvelView.getVisibility() != View.GONE) {
			if (PreferenceUtil.getSharedPreferenceBoolean(R.string.key_admarvel_iscurrentlyclicked)
					|| Application.getAuthorizationLevel() != AuthorizationLevel.FULL_ACCESS_WITH_ADS) {
				// do not trigger again after it was once clicked.
				mAdMarvelView.setVisibility(View.GONE);
				mAdMarvelView.destroy();
				mAdMarvelView = null;
			}
			else {
				mAdMarvelView.start(this);
			}
		}
	}

	// OVERRIDABLE
	@Override
	protected void onResume() {
		super.onResume();
		if (mAdMarvelView != null && mAdMarvelView.getVisibility() != View.GONE) {
			mAdMarvelView.resume(this);
		}
	}

	// OVERRIDABLE
	@Override
	protected void onPause() {
		super.onPause();
		if (mAdMarvelView != null && mAdMarvelView.getVisibility() != View.GONE) {
			mAdMarvelView.pause(this);
		}
	}

	// OVERRIDABLE
	@Override
	public void onStop() {
		super.onStop();
		if (mAdMarvelView != null && mAdMarvelView.getVisibility() != View.GONE) {
			mAdMarvelView.stop(this);
		}
	}

	// OVERRIDABLE
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mAdMarvelView != null && mAdMarvelView.getVisibility() != View.GONE) {
			mAdMarvelView.destroy();
		}
	}

	/**
	 * Get the AdMarvelView.
	 *
	 * @return The AdMarvelView.
	 */
	public final AdMarvelView getAdMarvelView() {
		if (mAdMarvelView == null) {
			mAdMarvelView = (AdMarvelView) findViewById(R.id.admarvel);
		}
		return mAdMarvelView;
	}

}
