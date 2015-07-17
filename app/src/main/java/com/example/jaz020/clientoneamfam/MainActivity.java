package com.example.jaz020.clientoneamfam;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
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
    private ExpandableListAdapter drawerExpandableListAdapter;

    private List<String> meetInternsHeader;
    private HashMap<String, List<String>> internNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.drawerItems)));

        setExpandDrawerLists();

        drawerExpandableList = (ExpandableListView) findViewById(R.id.expandable_intern_list);
        drawerExpandableListAdapter = new ExpandableListAdapter(this.getApplicationContext(), meetInternsHeader, internNames);
        drawerExpandableList.setAdapter(drawerExpandableListAdapter);

        // Sets the Up Navigation enabled only if fragments are on backStack
        enableUpAction();
        // Set the navigation drawer navigation
        setDrawerItemClickListener();

        /**
         * THIS CHECK IS IN PLACE TO STOP THE APP FROM CRASHING ON ROTATE:
         *
         */
        if (savedInstanceState == null) {

            // TODO!!!!!

            //loads the appropriate initial fragment
//            Tools.replaceFragment(new MainFragment(), getFragmentManager(), true);


            Tools.replaceFragment(R.id.fragment_container, new MainPageFragment(),
                    getFragmentManager(), true);
        }
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

                final int MY_AGENT = 0;
                final int FIND_AN_AGENT = 1;
                final int MY_POLICIES = 2;
                final int MY_CLAIMS = 3;
                final int SETTINGS = 4;

                switch (position) {
                    case MY_AGENT:
//                        Tools.replaceFragment(R.id.fragment_container, new MyAgent(),
//                                getFragmentManager(), true);
                        break;

                    case FIND_AN_AGENT:
//                        Tools.replaceFragment(R.id.fragment_container, new FindAgent(),
//                                getFragmentManager(), true);
                        break;

                    case MY_POLICIES:
//                        Tools.replaceFragment(R.id.fragment_container, new MyPolicies(),
//                                getFragmentManager(), true);
                        break;

                    case MY_CLAIMS:
//                        Tools.replaceFragment(R.id.fragment_container, new MyClaims(),
//                                getFragmentManager(), true);
                        break;

                    case SETTINGS:
                        Tools.replaceFragment(R.id.fragment_container, new Settings(),
                                getFragmentManager(), true);
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
                        Tools.replaceFragment(R.id.fragment_container, new MeetEvanFragment(),
                                getFragmentManager(), true);
                        break;

                    case LEVI:
                        Tools.replaceFragment(R.id.fragment_container, new MeetLeviFragment(),
                                getFragmentManager(), true);
                        break;

                    case NAVNEET:
                        Tools.replaceFragment(R.id.fragment_container, new MeetNavneetFragment(),
                                getFragmentManager(), true);
                        break;

                    case JAMES:
                        Tools.replaceFragment(R.id.fragment_container, new MeetJamesFragment(),
                                getFragmentManager(), true);
                        break;

                    default:
                        Log.d("Error", "Reached default in drawer");
                }

                return false;
            }
        });
    }

    public void enableUpAction(){
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getFragmentManager().getBackStackEntryCount();

                if (stackHeight > 1) {
                    // if we have something on the stack (doesn't include the current shown fragment).
                    // >0 removes initial frag and leave a blank space...use 1 instead.
                    getActionBar().setHomeButtonEnabled(true);
                    getActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getActionBar().setDisplayHomeAsUpEnabled(false);
                    getActionBar().setHomeButtonEnabled(false);
                }
            }
        });
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

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings:
                Tools.replaceFragment(R.id.fragment_container, new Settings(), getFragmentManager(), true);
                return true;

            case R.id.action_logout:
                Tools.logout(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}