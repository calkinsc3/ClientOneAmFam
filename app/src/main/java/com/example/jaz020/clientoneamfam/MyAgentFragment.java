package com.example.jaz020.clientoneamfam;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAgentFragment extends Fragment {


    public MyAgentFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_agent, container, false);
        TextView agentAddress1 = (TextView) view.findViewById(R.id.AgentAddress1TextView);
        TextView agentAddress2 = (TextView) view.findViewById(R.id.AgentAddress2TextView);
        TextView agentPhone = (TextView) view.findViewById(R.id.AgentPhoneTextView);
        Button agentScheduleButton = (Button) view.findViewById(R.id.AgentScheduleButton);

        Tools.setMyAgent();

        ParseUser agent = Singleton.getMyAgent();

        agentAddress1.setText(agent.getString("Address"));
        agentAddress2.setText(agent.getString("City") + "," + agent.getString("State")+ " " + agent.getNumber("Zip").toString());
        agentPhone.setText(agent.getNumber("phoneNumber").toString());

        agentScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Open Scheduling Here", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
