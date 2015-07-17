package com.example.jaz020.clientoneamfam;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private EditText username_entry;
    private EditText password_entry;
    private EditText password_reentry;
    private EditText name_entry;
    private EditText phone_entry;
    private EditText email_entry;

    private EditText street_entry;
    private EditText city_entry;
    private EditText zip_entry;
    private Spinner agent_spinner;
    private Button register_button;
    private Spinner state_spinner;

    private List<ParseUser> agentList;
    private List<String> agentNames;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO send email verifications

        initializeFields(view);

        retrieveAgentList();


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        password_reentry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String p1 = password_entry.getText().toString();
                String p2 = s.toString();


                //check if passwords match
                if (!p1.equals(p2)) {
                    password_reentry.setError("Passwords do not match");
                } else if (p1.length() < 4) {
                    password_reentry.setError("Password is not long enough");
                }

                //TODO anymore password checks


            }
        });

    }


    /**
     * Qeuries parse for Agent Users and creates a list of objects, as well as a list of names
     */
    private void retrieveAgentList(){

        //initialize agentNames
        agentNames = new ArrayList<>();

        ParseQuery<ParseUser> query = ParseUser.getQuery()
                .whereEqualTo("accountType", "Agent");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (!list.isEmpty()) {

                        //save the agentList
                        agentList = list;

                        //create a list of their names for display
                        for (ParseUser curr : list) {
                            agentNames.add(curr.getString("Name"));
                        }

                        //populate agent spinner
                        populateAgentSpinner();

                    } else {
                        Toast.makeText(getActivity(), "No Agents Available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * Initializes all fields required for this fragment
     * @param view the main view of this fragment
     */
    private void initializeFields(View view){

        username_entry = (EditText) view.findViewById(R.id.username_entry);
        password_entry = (EditText) view.findViewById(R.id.password_entry);
        password_reentry = (EditText) view.findViewById(R.id.password_reentry);
        name_entry = (EditText) view.findViewById(R.id.name_entry);
        phone_entry = (EditText) view.findViewById(R.id.phone_entry);
        email_entry = (EditText) view.findViewById(R.id.email_entry);
        street_entry = (EditText) view.findViewById(R.id.address_street);
        city_entry = (EditText) view.findViewById(R.id.address_city);
        zip_entry = (EditText) view.findViewById(R.id.zip_code);
        state_spinner = (Spinner) view.findViewById(R.id.state_spinner);
        agent_spinner = (Spinner) view.findViewById(R.id.agent_spinner);
        register_button = (Button) view.findViewById(R.id.register_button);

        //POPULATE STATE DROPDOWN
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        state_spinner.setAdapter(adapter);

    }

    /**
     * Populate the Agent Selection Spinner
     */
    private void populateAgentSpinner(){

        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, agentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        agent_spinner.setAdapter(adapter);

    }

    /**
     * Performs validation of all field entries and signs up a new user
     *
     */
    private void registerUser(){

        String phoneNumber = phone_entry.getText().toString();
        phoneNumber = phoneNumber.replace("(","");
        phoneNumber = phoneNumber.replace(")","");
        phoneNumber = phoneNumber.replace("-","");
        phoneNumber = phoneNumber.replace(" ","");

        String agentID = agentList.get(agent_spinner.getSelectedItemPosition()).getObjectId();

        ParseUser newUser = new ParseUser();

        String zip = zip_entry.getText().toString();

        //TODO validate all entries
        newUser.setUsername(username_entry.getText().toString());
        newUser.put("Address", street_entry.getText().toString());
        newUser.put("City", city_entry.getText().toString());
        newUser.put("State", state_spinner.getSelectedItem().toString());
        newUser.put("Name", name_entry.getText().toString());
        newUser.put("AgentID",agentID);
        newUser.put("accountType", "Client");
        newUser.setEmail(email_entry.getText().toString());

        if(password_entry.getError() == null){
            newUser.setPassword(password_entry.getText().toString());
        }

        if(zip.length() > 0 ) {
            newUser.put("Zip", Double.valueOf(zip_entry.getText().toString()));
        }

        if(phoneNumber.length() > 0) {
            newUser.put("phoneNumber", Double.valueOf(phoneNumber));
        }

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    signUpSuccess();
                } else {
                    Toast.makeText(getActivity(), "Could Not Create New User: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Log in the user that was just created and move to MainActivity
     */
    private void signUpSuccess(){
        Toast.makeText(getActivity(), "New User Created", Toast.LENGTH_SHORT).show();

        String username = username_entry.getText().toString();
        String password = password_entry.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if(parseUser != null){
                    Intent intent = new Intent(getActivity(), Splash.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "Could Not Log In: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
