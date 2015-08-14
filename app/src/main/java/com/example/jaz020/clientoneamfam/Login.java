package com.example.jaz020.clientoneamfam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.Parse;


/**
 * The Login activity enables the user to log in.
 */
public class Login extends AppCompatActivity {

    //PARSE KEYS
    private static final String APPLICATION_ID = "4YBarCfwhDQKdD9w7edqe8fIazqWRXv8RhRbNgd7";
    private static final String CLIENT_KEY = "zUguFYSgfxNkzTw6lQGkCWssT1VCMWBccWD44MFw";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //INITIALIZE PARSE
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        sharedPreferences = getSharedPreferences("AmFam", 0);

        //CHECK FOR LOGIN
        if (sharedPreferences.getString("UserID", null) != null && sharedPreferences.getBoolean("StayLoggedIn", false)) {
            if(Tools.isNetworkAvailable(this)){
                Intent intent = new Intent(this, Splash.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Instantiates a new Sections pager adapter.
         *
         * @param fm the fm
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets item.
         *
         * @param position the position
         * @return the item
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position){
                case 0:
                    return new LoginFragment();
                case 1:
                    return new RegisterFragment();
            }

            return null;
        }

        /**
         * Gets count.
         *
         * @return the count
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        /**
         * Gets page title.
         *
         * @param position the position
         * @return the page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Login";
                case 1:
                    return "Register";
            }
            return null;
        }
    }
}