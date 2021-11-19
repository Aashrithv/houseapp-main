package com.example.improvedhouse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.improvedhouse.chat.ChatActivity;
import com.example.improvedhouse.houses.HousesActivity;
import com.example.improvedhouse.profile.ProfileActivity;

/**
 * This is the main activity, where we have the structure of the App.
 * User is directed to this page upon successful registration or sig-in
 * In this page, we 2 tabs -
 *      i) Houses -> where all the houses are displayed
 *      ii) Profile -> where user can add a new house, view the houses they added or logout.
 *      iii) Chats -> where all the chats for the user are displayed.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /**
         * The {@link ViewPager} that will host the section contents.
         */
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // This is where we are listening to the tabs that are selected and accordingly we move the control.
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch (position){
                case 0:
                    return new HousesActivity();
                case 1:
                    return new ProfileActivity();
                case 2:
                    return new ChatActivity();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(final int position){
            switch (position){
                case 0:
                    return "HOUSES";
                case 1:
                    return "PROFILE";
                case 2:
                    return "CHATS";
                default:
                    return null;
            }
        }
    }
}
