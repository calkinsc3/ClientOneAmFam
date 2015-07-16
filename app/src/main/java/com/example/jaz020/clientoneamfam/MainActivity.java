package com.example.jaz020.clientoneamfam;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItems;
    private ExpandableListView drawerExpandableList;
    private ExpandableListAdapter drawerExpandableListAdapter;

    private List<String> meetInternsHeader;
    private HashMap<String, List<String>> internNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerItems = getResources().getStringArray(R.array.drawerItems);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, drawerItems));

        setExpandDrawerLists();

        drawerExpandableList = (ExpandableListView) findViewById(R.id.expandable_intern_list);
        drawerExpandableListAdapter = new ExpandableListAdapter(this, meetInternsHeader, internNames);

        // Sets the Up Navigation enabled only if fragments are on backStack
        enableUpAction();
        // Set the navigation drawer navigation
        setDrawerItemClickListener();

        // TODO!!!!!
        if (savedInstanceState == null) {
            //loads the appropriate initial fragment
//            Tools.replaceFragment(new MainFragment(), getFragmentManager(), true);
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
                drawerLayout.closeDrawers();

                final int MY_AGENT = 0;
                final int FIND_AN_AGENT = 1;
                final int MY_POLICIES = 2;
                final int MY_CLAIMS = 3;
                final int SETTINGS = 4;
                final int MEET_THE_INTERNS = 5;

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
    }

    public void enableUpAction(){
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getFragmentManager().getBackStackEntryCount();

                if (stackHeight > 1) {
                    // if we have something on the stack (doesn't include the current shown fragment).
                    // >0 removes initial frag and leave a blank space...use 1 instead.
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });
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

        }


        return super.onOptionsItemSelected(item);
    }
}
