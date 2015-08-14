package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    private final long CLOUD_SPEED = 500;
    /**
     * The Version _ text _ view.
     */
    TextView version_text_view;
    /**
     * The Username _ text _ view.
     */
    TextView username_text_view;
    /**
     * The Address _ text _ view.
     */
    TextView address_text_view;
    /**
     * The Phone _ text _ view.
     */
    TextView phone_text_view;
    /**
     * The Email _ text _ view.
     */
    TextView email_text_view;

    /**
     * The Logout _ button.
     */
    Button logout_button;

    /**
     * The Size.
     */
    Point size = new Point();

    /**
     * On create view.
     *
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /**
     * On view created.
     *
     * @param view the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        version_text_view = (TextView) view.findViewById(R.id.version_text_view);
        username_text_view = (TextView) view.findViewById(R.id.username_text_view);
        address_text_view = (TextView) view.findViewById(R.id.address_text_view);
        phone_text_view  = (TextView) view.findViewById(R.id.phone_text_view);
        email_text_view = (TextView) view.findViewById(R.id.email_text_view);

        logout_button = (Button) view.findViewById(R.id.logout_button);

        //initializes the text views to the information from the network
        setTextViews();

        //logs the user out
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_button.setEnabled(false);

                SlideAnimation sa = new SlideAnimation(logout_button.getX(), size.x, logout_button);
                sa.setInterpolator(new AccelerateInterpolator());
                sa.setDuration(CLOUD_SPEED);
                logout_button.startAnimation(sa);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tools.logout(getActivity());
                    }
                }, CLOUD_SPEED);
            }
        });
    }

    /**
     * populates the views on the page with information from the network
     */
    private void setTextViews(){
        ParseUser currUser = ParseUser.getCurrentUser();

        String versionName;

        try {
            versionName = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch(PackageManager.NameNotFoundException e) {
           versionName = "Could not retrieve version: " + e.getMessage();
        }

        String username = currUser.getUsername();
        String address = currUser.getString("Address") + "\n" + currUser.getString("City") + ", " +
                currUser.getString("State") + " " + currUser.getNumber("Zip").toString();
        String phone = currUser.getNumber("phoneNumber").toString();

        phone = PhoneNumberUtils.formatNumber(phone);
        String email = currUser.getEmail();

        version_text_view.setText("Version: " + versionName);
        username_text_view.setText(username);
        address_text_view.setText(address);
        phone_text_view.setText(phone);
        email_text_view.setText(email);
    }

    /**
     * On resume re-enable buttons.
     */
    @Override
    public void onResume() {
        super.onCreate(null);

        logout_button.setEnabled(true);
    }
}