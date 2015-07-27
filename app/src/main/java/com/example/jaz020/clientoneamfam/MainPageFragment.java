package com.example.jaz020.clientoneamfam;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;




/**
 *
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

    Display display;
    Point size = new Point();

    private static final long CLOUD_SPEED = 500;

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

        display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(size);

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

//        Animator anim = ViewAnimationUtils.

        return rootView;
    }

    private void buttonClickListeners() {
        myAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SlideAnimation sa = new SlideAnimation(myAgent.getX(), size.x, myAgent);
//               sa.setInterpolator(new AccelerateInterpolator());
//                sa.setDuration(CLOUD_SPEED);
//                myAgent.startAnimation(sa);

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                myAgent.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new MyAgentFragment(),
                                getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);

            }
        });

        findAnAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                findAnAgent.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=american+family+agents");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, CLOUD_SPEED);

            }
        });

        myPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                myPolicies.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new MyPoliciesFragment(),
                                getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);

            }
        });

        myClaims.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                myClaims.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new MyClaimsFragment(),
                                getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                settings.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new Settings(),
                                getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);
            }
        });

        meetTheInterns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                meetTheInterns.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new MeetInternsFragment(),
                                getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);
            }
        });
    }


    @Override
    public void onResume() {
        super.onCreate(null);

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