package com.example.jaz020.clientoneamfam;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyScreenFragment extends Fragment {

    EditText policyDescription;
    EditText policyCost;
    EditText policyAddress;
    EditText city;
    EditText zip;

    Spinner stateSpinner;

    ImageButton addUploads;

    ListView uploadsList;



    public PolicyScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_policy_screen, container, false);

        initializeFields(view);

        return view;
    }

    public void initializeFields(View view){
        policyDescription = (EditText)view.findViewById(R.id.policyDescription);
        policyCost = (EditText)view.findViewById(R.id.policyCost);
        policyAddress = (EditText)view.findViewById(R.id.policyAddress);
        city = (EditText)view.findViewById(R.id.city);
        zip = (EditText)view.findViewById(R.id.zip);
        stateSpinner = (Spinner)view.findViewById(R.id.stateSpinner);
        addUploads = (ImageButton)view.findViewById(R.id.addUploadButton);
    }
}
