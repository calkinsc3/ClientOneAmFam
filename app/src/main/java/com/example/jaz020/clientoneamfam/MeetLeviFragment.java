package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;


/**
 * Meet Levi Fragment shows information about Levi in a tabbed view
 *
 * @author llavender
 */
public class MeetLeviFragment extends Fragment {

    TabHost tabHost;
    TabHost.TabSpec tab1;
    TabHost.TabSpec tab2;
    TabHost.TabSpec tab3;

    /**
     * Called when the view is being inflated to the screen
     *
     * @param inflater the layout inflater
     * @param container the ViewGroup
     * @param savedInstanceState the instance state saved on rotation
     * @return the view to inflate
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_levi, container, false);

        initializeTabsAndContent(view);

        return view;
    }

    /**
     * initializeTabsAndContent creates the separate tabs for the TabHost. Populates the views with
     * text
     *
     * @param view the content view
     */
    private void initializeTabsAndContent(final View view){
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
                info.setVisibility(View.VISIBLE);

                info.setText("Levi Lavender currently holds a Associates Degree from the " +
                        "University of Wisconsin Baraboo/Sauk County. He is one semester away from " +
                        "graduating from Madison Area Technical College with a degree in Mobile " +
                        "Application Development with an emphasis on Android. He has experience with" +
                        " Android Studio, Java, JavaScript, HTML, CSS, XML, Swift, JQuery, AngularJS," +
                        " and Oracle SQL. He is am currently interning with American Family Insurance" +
                        " developing internal applications for Android Devices. ");

                return ll_view;
            }
        });

        final TabHost.TabSpec skillsTab = tabHost.newTabSpec("about").setIndicator("Skills")
                .setContent(new TabHost.TabContentFactory() {
                    public View createTabContent(String tag) {
                        final View ll_view = LayoutInflater.from(getActivity()).inflate(R.layout.levi_info, null);
                        // do all the stuff you need to do to your view
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
                        information2.setText("Problem Solver, Critical Thinking, Teamwork, Customer Service," +
                                "Dependable, Consistent, and Versatile");

                        label.setVisibility(View.VISIBLE);
                        label2.setVisibility(View.VISIBLE);
                        information.setVisibility(View.VISIBLE);
                        information2.setVisibility(View.VISIBLE);
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
                        TextView label = (TextView)ll_view.findViewById(R.id.label);
                        TextView label2 = (TextView)ll_view.findViewById(R.id.label2);
                        TextView information = (TextView)ll_view.findViewById(R.id.information);
                        TextView information2 = (TextView)ll_view.findViewById(R.id.information2);

                        label.setText("Email: ");
                        information.setText("llavender@madisoncollege.edu");
//                        info.setPadding(0, 15, 0, 15);
                        label2.setText("Phone: ");
                        information2.setText("(608)415-1288");

                        label.setVisibility(View.VISIBLE);
                        label2.setVisibility(View.VISIBLE);
                        information.setVisibility(View.VISIBLE);
                        information2.setVisibility(View.VISIBLE);

                        return ll_view;
                    }
                });


        tabHost.addTab(aboutTab);
        tabHost.addTab(skillsTab);
        tabHost.addTab(contactTab);
    }
}