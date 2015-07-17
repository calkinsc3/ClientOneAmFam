package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by nsr009 on 6/16/2015.
 */
public class MainPageFragment extends Fragment {

    View rootView;

    ImageView amfamlogo;

    Button MY_AGENT;
    Button FIND_AN_AGENT;
    Button MY_POLICIES;
    Button MY_CLAIMS;
    Button SETTINGS;

    private ExpandableListView drawerExpandableList;
    private ExpandableListAdapter drawerExpandableListAdapter;

    private List<String> meetInternsHeader;
    private HashMap<String, List<String>> internNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_page, container, false);

        amfamlogo = (ImageView) rootView.findViewById(R.id.amfamlogo);
        amfamlogo.setImageResource(R.drawable.amfam3);

        MY_AGENT = (Button) rootView.findViewById(R.id.clientsButton);
        FIND_AN_AGENT = (Button) rootView.findViewById(R.id.claimsButton);
        MY_POLICIES = (Button) rootView.findViewById(R.id.scheduleButton);
        MY_CLAIMS = (Button) rootView.findViewById(R.id.settingsButton);
        SETTINGS = (Button) rootView.findViewById(R.id.myUploadsButton);

        setExpandDrawerLists();

        drawerExpandableList = (ExpandableListView) rootView.findViewById(R.id.expandable_intern_list);
        drawerExpandableListAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(),
                meetInternsHeader, internNames);
        drawerExpandableList.setAdapter(drawerExpandableListAdapter);


        /* Clients button clicked */
        MY_AGENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tools.replaceFragment(R.id.fragment_container, new MyAgent(),
//                        getFragmentManager(), true);
            }
        });

        /* Claims button clicked */
        FIND_AN_AGENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tools.replaceFragment(R.id.fragment_container, new FindAgent(),
//                        getFragmentManager(), true);
            }
        });

        /* Schedule button clicked */
        MY_POLICIES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tools.replaceFragment(R.id.fragment_container, new MyPolicies(),
//                        getFragmentManager(), true);
            }
        });

        /* Settings button clicked */
        MY_CLAIMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tools.replaceFragment(R.id.fragment_container, new MyClaims(),
//                        getFragmentManager(), true);
            }
        });

        /* Uploads button clicked */
        SETTINGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new Settings(),
                        getFragmentManager(), true);
            }
        });

        drawerExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
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

        return rootView;
    }

    private void setExpandDrawerLists() {
        List<String> internNamesList = new ArrayList<>();
        meetInternsHeader = new ArrayList<>();
        internNames = new HashMap<>();

        internNamesList.addAll(Arrays.asList(getResources().getStringArray(R.array.intern_names)));
        meetInternsHeader.add(getResources().getString(R.string.meet_the_interns));
        internNames.put(meetInternsHeader.get(0), internNamesList);
    }
}