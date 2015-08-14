package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;


/**
 * MyAgentFragment shows the information of the Agent the user currently holds
 */
public class MyAgentFragment extends Fragment {

    private final long CLOUD_SPEED = 500;

    private ParseUser agent;

    private ImageButton agentDirection;
    private ImageButton agentCall;
    private ImageButton emailBtn;

    private Button agentScheduleButton;

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
        View view = inflater.inflate(R.layout.fragment_my_agent, container, false);

        TextView agentName = (TextView) view.findViewById(R.id.agentName);
        TextView agentAddress1 = (TextView) view.findViewById(R.id.AgentAddress1TextView);
        TextView agentAddress2 = (TextView) view.findViewById(R.id.AgentAddress2TextView);
        TextView agentPhone = (TextView) view.findViewById(R.id.AgentPhoneTextView);

        ImageView agentImg = (ImageView) view.findViewById(R.id.agentImage);

        agentDirection = (ImageButton) view.findViewById(R.id.agentDirectionsButton);
        agentCall = (ImageButton) view.findViewById(R.id.agentCallButton);
        emailBtn = (ImageButton) view.findViewById(R.id.agentEmailingButton);

        agentScheduleButton = (Button) view.findViewById(R.id.AgentScheduleButton);

        agent = Singleton.getMyAgent();
        String phoneNum = agent.getNumber("phoneNumber").toString();

        agentName.setText(agent.getString("Name"));
        agentAddress1.setText(agent.getString("Address"));
        agentAddress2.setText(agent.getString("City") + "," +
                agent.getString("State") + " " + agent.getNumber("Zip").toString());
        agentPhone.setText("( " + phoneNum.substring(0, 3) + " ) " +
                phoneNum.substring(3, 6) + " - " + phoneNum.substring(6));

        Picasso.with(getActivity())
                .load(agent.getParseFile("AgentPhoto").getUrl())
                .fit()
                .centerCrop()
                .into(agentImg);

        setOnClickListeners();

        return view;
    }

    /**
     * sets the click listeners for the buttons on the view
     */
    private void setOnClickListeners() {
        //starts a new EditAppointmentFragment
        agentScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agentScheduleButton.setEnabled(false);

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                agentScheduleButton.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new EditAppointment(),
                                getFragmentManager(), true);
                    }
                }, CLOUD_SPEED);
            }
        });

        //searches for the directions to the agent's address
        agentDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agentDirection.setEnabled(false);

                String addrstr = Singleton.getMyAgent().getString("Address");
                addrstr = addrstr.replace(" ", "+");

                String citystr = Singleton.getMyAgent().getString("City");
                citystr = citystr.replace(" ", "+");

                String statestr = Singleton.getMyAgent().getString("State");

                Uri gmnIntentUri = Uri.parse("google.navigation:q=" + addrstr + ",+" + citystr + "+" + statestr);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmnIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);
            }
        });

        //calls the agent
        agentCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agentCall.setEnabled(false);

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                        Singleton.getMyAgent().getNumber("phoneNumber").toString()));
                startActivity(intent);
            }
        });

        //emails the agent
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailBtn.setEnabled(false);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");

                String[] emails = new String[1];
                emails[0] = agent.getString("email");

                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                startActivity(intent);
            }
        });
    }

    /**
     * On resume re-enables the buttons.
     */
    @Override
    public void onResume() {
        super.onCreate(null);

        agentScheduleButton.setEnabled(true);
        agentDirection.setEnabled(true);
        agentCall.setEnabled(true);
        emailBtn.setEnabled(true);
    }
}