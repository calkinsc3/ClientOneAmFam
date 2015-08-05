package com.example.jaz020.clientoneamfam;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ExpandableListView drawerExpandableList;
    private ActionBarDrawerToggle drawerToggle;

    private List<String> meetInternsHeader;
    private HashMap<String, List<String>> internNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.setFragmentManager(getFragmentManager());
        Singleton.setContext(this.getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        setUpDrawer();

        // Set the navigation drawer navigation
        setDrawerItemClickListener();

        /**
         * THIS CHECK IS IN PLACE TO STOP THE APP FROM CRASHING ON ROTATE:
         */
        if (savedInstanceState == null) {
            Tools.replaceFragment(R.id.fragment_container, new MainPageFragment(),
                    getFragmentManager(), true);
        }
    }

    private void setUpDrawer(){
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_launcher,
                R.string.james_ziglinski,  /* "open drawer" description */
                R.string.connect_with_james  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.basic_list_item,
                getResources().getStringArray(R.array.drawerItems)));

        setExpandDrawerLists();

        ExpandableListAdapter drawerExpandableListAdapter =
                new ExpandableListAdapter(this.getApplicationContext(),
                        meetInternsHeader, internNames);
        drawerExpandableList = (ExpandableListView) findViewById(R.id.expandable_intern_list);
        drawerExpandableList.setAdapter(drawerExpandableListAdapter);
    }

    private void setExpandDrawerLists() {
        List<String> internNamesList = new ArrayList<>();
        meetInternsHeader = new ArrayList<>();
        internNames = new HashMap<>();

        internNamesList.addAll(Arrays.asList(getResources().getStringArray(R.array.intern_names)));
        meetInternsHeader.add(getResources().getString(R.string.meet_the_interns));
        internNames.put(meetInternsHeader.get(0), internNamesList);
    }

    public void setDrawerItemClickListener(){
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(findViewById(R.id.main_drawer_linear_layout));
                drawerExpandableList.collapseGroup(0);


                final int HOME_PAGE = 0;
                final int MY_AGENT = 1;
                final int FIND_AN_AGENT = 2;
                final int MY_POLICIES = 3;
                final int MY_CLAIMS = 4;
                final int SETTINGS = 5;

                switch (position) {
                    case HOME_PAGE:
                        if(!fragmentIsInflated(new MainPageFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MainPageFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case MY_AGENT:
                        if(!fragmentIsInflated(new MyAgentFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MyAgentFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case FIND_AN_AGENT:
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=american+family+agents");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);

                        break;

                    case MY_POLICIES:
                        if(!fragmentIsInflated(new MyPoliciesFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MyPoliciesFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case MY_CLAIMS:
                        if(!fragmentIsInflated(new MyClaimsFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MyClaimsFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case SETTINGS:
                        if(!fragmentIsInflated(new Settings())) {
                            Tools.replaceFragment(R.id.fragment_container, new Settings(),
                                    getFragmentManager(), true);
                        }
                        break;

                    default:
                        Log.d("Error", "Reached default in drawer");
                }
            }
        });

        drawerExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                drawerLayout.closeDrawer(findViewById(R.id.main_drawer_linear_layout));
                drawerExpandableList.collapseGroup(0);

                final int EVAN = 0;
                final int LEVI = 1;
                final int NAVNEET = 2;
                final int JAMES = 3;

                switch (childPosition) {
                    case EVAN:
                        if(!fragmentIsInflated(new MeetEvanFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MeetEvanFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case LEVI:
                        if(!fragmentIsInflated(new MeetLeviFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MeetLeviFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case NAVNEET:
                        if(!fragmentIsInflated(new MeetNavneetFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MeetNavneetFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    case JAMES:
                        if(!fragmentIsInflated(new MeetJamesFragment())) {
                            Tools.replaceFragment(R.id.fragment_container, new MeetJamesFragment(),
                                    getFragmentManager(), true);
                        }
                        break;

                    default:
                        Log.d("Error", "Reached default in drawer");
                }

                return false;
            }
        });
    }

    public boolean fragmentIsInflated(Fragment currFragment){
        FragmentManager fm = getFragmentManager();
        boolean isVisible = false;
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if(currFragment.getClass().toString().equals(f.getClass().toString())){
            isVisible = true;
        }

        return isVisible;
    }

    /**
     * Moves the the down arrow on the expandable list view to the right side of the screen.
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            drawerExpandableList.setIndicatorBounds(drawerExpandableList.getRight() - 200,
                    drawerExpandableList.getWidth());
        } else {
            drawerExpandableList.setIndicatorBoundsRelative(drawerExpandableList.getRight() - 200,
                    drawerExpandableList.getWidth());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                if(!fragmentIsInflated(new Settings())) {
                    Tools.replaceFragment(R.id.fragment_container, new Settings(),
                            getFragmentManager(), true);
                }
                return true;

            case R.id.action_logout:
                Tools.logout(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 1){
            super.onBackPressed();
        }
    }
}