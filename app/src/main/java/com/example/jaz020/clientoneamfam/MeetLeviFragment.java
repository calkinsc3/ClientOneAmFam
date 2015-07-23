package com.example.jaz020.clientoneamfam;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetLeviFragment extends Fragment {

    TabHost tabHost;
    TabHost.TabSpec tab1;
    TabHost.TabSpec tab2;
    TabHost.TabSpec tab3;

    public MeetLeviFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_levi, container, false);

        initailizeTabs(view);

        return view;
    }

    private void initailizeTabs(final View view){
        tabHost = (TabHost)view.findViewById(android.R.id.tabhost);
        tabHost.setup();

        tab1 = tabHost.newTabSpec("About");
        tab2 = tabHost.newTabSpec("Skills");
        tab3 = tabHost.newTabSpec("Contact Info");

        final TabHost.TabSpec aboutTab = tabHost.newTabSpec("about").setIndicator("About").setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                final View ll_view = LayoutInflater.from(getActivity()).inflate(R.layout.levi_info, null);
                // do all the stuff you need to do to your view
                TextView info = (TextView)ll_view.findViewById(R.id.basicInfo);

                info.setText("Levi Lavender is one semester away from graduating from Madison Area " +
                        "Technical College with a degree in Mobile Application Development with an " +
                        "emphasis on Android. He has experience with Android Studio, Java, JavaScript," +
                        " HTML, CSS, XML, Swift, JQuery, AngularJS, and Oracle SQL. He is am currently " +
                        "interning with American Family Insurance developing internal applications " +
                        "for Android Devices. He currently holds a Associates Degree from the " +
                        "University of Wisconsin Baraboo/Sauk County.");

                return ll_view;
            }
        });

        final TabHost.TabSpec skillsTab = tabHost.newTabSpec("about").setIndicator("Skills")
                .setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        final View ll_view = LayoutInflater.from(getActivity()).inflate(R.layout.levi_info, null);
                        // do all the stuff you need to do to your view
                        TextView info = (TextView)ll_view.findViewById(R.id.basicInfo);
                        TextView label = (TextView)ll_view.findViewById(R.id.label);
                        TextView label2 = (TextView)ll_view.findViewById(R.id.label2);
                        TextView information = (TextView)ll_view.findViewById(R.id.information);
                        TextView information2 = (TextView)ll_view.findViewById(R.id.information2);
                        TextView github = (TextView)ll_view.findViewById(R.id.github);
                        TextView cademy = (TextView)ll_view.findViewById(R.id.codecademy);

                        label.setText("Technical Skills: ");
                        information.setText("Android Studio, Java, Swift, jQuery, AngularJS, JavaScript" +
                                "Oracle SQL, HTML,CSS, and XML");
                        label2.setText("Additional Skills: ");
                        information2.setText("Problem Solving, Critical Thinking, Teamwork, Customer Service," +
                                "Dependable, Consistent, and Versatile");

                        github.setVisibility(View.VISIBLE);
                        cademy.setVisibility(View.VISIBLE);
                        return ll_view;
                    }
                });

        final TabHost.TabSpec contactTab = tabHost.newTabSpec("about").setIndicator("Contact")
                .setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        final View ll_view = LayoutInflater.from(getActivity()).inflate(R.layout.levi_info, null);
                        // do all the stuff you need to do to your view
                        TextView info = (TextView)ll_view.findViewById(R.id.basicInfo);
                        TextView address = (TextView)ll_view.findViewById(R.id.label);
                        TextView address2 = (TextView)ll_view.findViewById(R.id.information);

                        info.setText("Email: llavender@madisoncollege.edu");
                        info.setPadding(0,15,0,15);
                        address.setVisibility(View.VISIBLE);
                        address2.setText("420 Russell Street" +
                                "\nBaraboo, Wi 53913");

                        return ll_view;
                    }
                });


        tabHost.addTab(aboutTab);
        tabHost.addTab(skillsTab);
        tabHost.addTab(contactTab);
    }
}