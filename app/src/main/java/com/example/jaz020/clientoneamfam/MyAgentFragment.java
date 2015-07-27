package com.example.jaz020.clientoneamfam;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAgentFragment extends Fragment {


    public MyAgentFragment() {
        // Required empty public constructor
    }

    private final long CLOUD_SPEED = 500;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_agent, container, false);
        TextView agentAddress1 = (TextView) view.findViewById(R.id.AgentAddress1TextView);
        TextView agentAddress2 = (TextView) view.findViewById(R.id.AgentAddress2TextView);
        TextView agentPhone = (TextView) view.findViewById(R.id.AgentPhoneTextView);
        final Button agentScheduleButton = (Button) view.findViewById(R.id.AgentScheduleButton);
        ImageView agentImg = (ImageView) view.findViewById(R.id.agentImage);
        ImageButton agentDirection = (ImageButton) view.findViewById(R.id.agentDirectionsButton);
        ImageButton agentCall = (ImageButton) view.findViewById(R.id.agentCallButton);
        ImageButton emailBtn = (ImageButton) view.findViewById(R.id.agentEmailingButton);

        final ParseUser agent = Singleton.getMyAgent();
        String phoneNum = agent.getNumber("phoneNumber").toString();
        agentAddress1.setText(agent.getString("Address"));
        agentAddress2.setText(agent.getString("City") + "," + agent.getString("State")+ " " + agent.getNumber("Zip").toString());
        agentPhone.setText("( " + phoneNum.substring(0, 3) + " ) - " + phoneNum.substring(3, 6) + " - " + phoneNum.substring(6));
        Picasso.with(getActivity()).load(agent.getParseFile("AgentPhoto").getUrl()).fit().centerCrop().into(agentImg);
        agentScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_right);
                anim.setDuration(CLOUD_SPEED + 500);
                agentScheduleButton.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Tools.replaceFragment(R.id.fragment_container, new EditAppointment(), getFragmentManager(), true);

                    }
                }, CLOUD_SPEED);



            }
        });

        agentDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addrstr = Singleton.getMyAgent().getString("Address");
                addrstr = addrstr.replace(" ", "+");
                String citystr = Singleton.getMyAgent().getString("City");
                citystr = citystr.replace(" ", "+");
                String statestr = Singleton.getMyAgent().getString("State");
                Uri gmnIntentUri = Uri.parse("google.navigation:q=" + addrstr + ",+" + citystr + "+" + statestr);
                //Uri gmnIntentUri = Uri.parse("google.navigation:q=6917+Ramsey+Road,+Middleton+Wisconsin"); My home address to test
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmnIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        agentCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Singleton.getMyAgent().getNumber("phoneNumber").toString()));
                startActivity(intent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                String[] emails = new String[1];
                emails[0] = agent.getString("email");
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                startActivity(intent);
            }
        });
        return view;
    }



}
