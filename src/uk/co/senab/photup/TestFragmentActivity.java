package uk.co.senab.photup;

import uk.co.senab.photup.events.PhotoSelectionAddedEvent;
import uk.co.senab.photup.events.PhotoSelectionRemovedEvent;
import uk.co.senab.photup.events.UploadsModifiedEvent;
import uk.co.senab.photup.events.UploadsStartEvent;
import uk.co.senab.photup.fragments.FacebookPhotosFragment;
import uk.co.senab.photup.fragments.FriendsListFragment;
import uk.co.senab.photup.fragments.SelectedPhotosFragment;
import uk.co.senab.photup.fragments.UserPhotosFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.yaser.instabaski.R;

import de.greenrobot.event.EventBus;

public class TestFragmentActivity extends SherlockFragmentActivity {

	public static final int TAB_SOCIAL = 0;
	public static final int TAB_PHOTOS = 1;
	public static final int TAB_SELECTED = 2;
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	protected PhotoUploadController mPhotoController;

	private UserPhotosFragment userPhotosFragment;
	private SelectedPhotosFragment selectedPhotosFragment;
	
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPhotoController = PhotoUploadController.getFromContext(this);
		EventBus.getDefault().register(this);
		//TODO bu kýsým uploading için yararlý olacak
//		if (Utils.isUploadingPaused(this)) {
//			showUploadingDisabledCrouton();
//		}
		
		setContentView(R.layout.activity_main);
		
		if (selectedPhotosFragment == null) {
			selectedPhotosFragment = new SelectedPhotosFragment();
		}

		Drawable colorDrawable = new ColorDrawable(Color.parseColor("#FFC74B46"));
		LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable});
		getActionBar().setBackgroundDrawable(ld);
		
		getActionBar().setTitle("");
//		// Set up the ViewPager with the sections adapter.
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
//		mViewPager.setCurrentItem(TAB_PHOTOS);
		
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		pager.setAdapter(mSectionsPagerAdapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pager.setCurrentItem(TAB_PHOTOS);

		tabs.setViewPager(pager);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				startActivity(new Intent(this, LoginActivity.class));
				overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
				return true;

			case R.id.menu_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;

			case R.id.menu_logout:
				startActivity(new Intent(Constants.INTENT_LOGOUT));
				finish();
				return true;

			case R.id.menu_clear_selection:
				mPhotoController.clearSelected();
				return true;

			case R.id.menu_select_all:
				//TODO getSupportFragmentManager ile yapýlmasý daha iyi olur
//				UserPhotosFragment fragment = (UserPhotosFragment) getSupportFragmentManager().findFragmentById(
//						R.id.frag_primary);
				int currentItem = pager.getCurrentItem();
				if (currentItem >= 0 && currentItem == TAB_PHOTOS) {
					userPhotosFragment.selectAll();
				}
				
				return true;

			case R.id.menu_uploads:
				startUploadsActivity();
				return true;

			case R.id.menu_retry_failed:
			case R.id.menu_uploading_stop:
			case R.id.menu_uploading_start:
				// Handled in super
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onEvent(PhotoSelectionAddedEvent event) {
		refreshSelectedPhotosTitle();
//		refreshUploadActionBarView();
	}

	public void onEvent(PhotoSelectionRemovedEvent event) {
		refreshSelectedPhotosTitle();
//		refreshUploadActionBarView();
	}

	public void onEvent(UploadsStartEvent event) {
//		ActionBar ab = getSupportActionBar();
//		if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_STANDARD) {
//			ab.setSelectedNavigationItem(TAB_UPLOADS);
//		} else {
//			startUploadsActivity();
//		}
	}

	public void onEventMainThread(UploadsModifiedEvent event) {
		refreshTabMenuItems();
	}
	
	private void refreshSelectedPhotosTitle() {
//		if (mSinglePane) {
//			getSupportActionBar().getTabAt(1).setText(formatSelectedFragmentTitle());
//		} else {
//			SelectedPhotosFragment userPhotos = (SelectedPhotosFragment) getSupportFragmentManager().findFragmentById(
//					R.id.frag_secondary);
//			if (null != userPhotos) {
//				userPhotos.setFragmentTitle(formatSelectedFragmentTitle());
//			}
//		}
	}

	private void refreshTabMenuItems() {
//		refreshUploadActionBarView();
//		refreshUploadsActionBarView();
		refreshSelectedPhotosTitle();
	}
	
	private void startUploadsActivity() {
		startActivity(new Intent(this, PhotoUploadsActivity.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_photo_grid_users, menu);
		
		MenuItem item = menu.findItem(R.id.menu_basket);
		
//		BadgeView badge = new BadgeView(this, item);
//		badge.setText("1");
//		badge.show();
		
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				pager.setCurrentItem(TAB_SELECTED, true);
				return true;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {


		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = new FriendsListFragment();
				break;
			case 1:
				fragment = userPhotosFragment = new UserPhotosFragment();
				break;
			case 2:
				fragment = new SelectedPhotosFragment();
				break;
			default:
				fragment = new UserPhotosFragment();
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_sectionSocial).toUpperCase();
			case 1:
				return getString(R.string.title_sectionPhotos).toUpperCase();
			case 2:
				return getString(R.string.title_sectionSelected).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText("Sosyal Menu");
			return textView;
		}
	}

}
