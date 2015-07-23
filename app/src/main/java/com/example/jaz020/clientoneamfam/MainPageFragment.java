package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;

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

    Button myAgent;
    Button findAnAgent;
    Button myPolicies;
    Button myClaims;
    Button settings;
    Button meetTheInterns;

//    private ExpandableListView drawerExpandableList;
//
//    private List<String> meetInternsHeader;
//    private HashMap<String, List<String>> internNames;

    //PARSE KEYS
    private static final String APPLICATION_ID = "4YBarCfwhDQKdD9w7edqe8fIazqWRXv8RhRbNgd7";
    private static final String CLIENT_KEY = "zUguFYSgfxNkzTw6lQGkCWssT1VCMWBccWD44MFw";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_page, container, false);

        myAgent = (Button) rootView.findViewById(R.id.clientsButton);
        findAnAgent = (Button) rootView.findViewById(R.id.claimsButton);
        myPolicies = (Button) rootView.findViewById(R.id.scheduleButton);
        myClaims = (Button) rootView.findViewById(R.id.settingsButton);
        settings = (Button) rootView.findViewById(R.id.myUploadsButton);
        meetTheInterns = (Button) rootView.findViewById(R.id.meetInternsButton);

        Singleton.getMyAgent();

//        setExpandDrawerLists();
//
//        ExpandableListAdapter drawerExpandableListAdapter =
//                new ExpandableListAdapter(getActivity().getApplicationContext(),
//                        meetInternsHeader, internNames);
//        drawerExpandableList = (ExpandableListView) rootView.findViewById(R.id.expandable_intern_list);
//        drawerExpandableList.setAdapter(drawerExpandableListAdapter);

        buttonClickListeners();
//        expandableListClickListener();

        try {
            Tools.setMyAgent();
        }
        catch (ParseException e){
            //INITIALIZE PARSE
            Parse.enableLocalDatastore(getActivity());
            Parse.initialize(getActivity(), APPLICATION_ID, CLIENT_KEY);
        }

        return rootView;
    }

    private void buttonClickListeners() {
        myAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new MyAgentFragment(),
                        getFragmentManager(), true);
            }
        });

        findAnAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=american+family+agents");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        myPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new MyPoliciesFragment(),
                        getFragmentManager(), true);
            }
        });

        myClaims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new MyClaimsFragment(),
                        getFragmentManager(), true);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new Settings(),
                        getFragmentManager(), true);
            }
        });

        meetTheInterns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.replaceFragment(R.id.fragment_container, new MeetInternsFragment(),
                        getFragmentManager(), true);
            }
        });
    }
//
//    private void expandableListClickListener() {
//        drawerExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                drawerExpandableList.collapseGroup(0);
//
//                final int EVAN = 0;
//                final int LEVI = 1;
//                final int NAVNEET = 2;
//                final int JAMES = 3;
//
//                switch (childPosition) {
//                    case EVAN:
//                        Tools.replaceFragment(R.id.fragment_container, new MeetEvanFragment(),
//                                getFragmentManager(), true);
//                        break;
//
//                    case LEVI:
//                        Tools.replaceFragment(R.id.fragment_container, new MeetLeviFragment(),
//                                getFragmentManager(), true);
//                        break;
//
//                    case NAVNEET:
//                        Tools.replaceFragment(R.id.fragment_container, new MeetNavneetFragment(),
//                                getFragmentManager(), true);
//                        break;
//
//                    case JAMES:
//                        Tools.replaceFragment(R.id.fragment_container, new MeetJamesFragment(),
//                                getFragmentManager(), true);
//                        break;
//
//                    default:
//                        Log.d("Error", "Reached default in drawer");
//                }
//
//                return false;
//            }
//        });
//    }

//    private void setExpandDrawerLists() {
//        List<String> internNamesList = new ArrayList<>();
//        meetInternsHeader = new ArrayList<>();
//        internNames = new HashMap<>();
//
//        internNamesList.addAll(Arrays.asList(getResources().getStringArray(R.array.intern_names)));
//        meetInternsHeader.add(getResources().getString(R.string.meet_the_interns));
//        internNames.put(meetInternsHeader.get(0), internNamesList);
//    }
}