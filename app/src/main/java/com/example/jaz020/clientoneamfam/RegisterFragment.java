package com.example.jaz020.clientoneamfam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
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

    private boolean agentSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

        phone_entry.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    /**
     * Queries parse for Agent Users and creates a list of objects, as well as a list of names
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

                        //set flag
                        agentSelected = true;

                    } else {
                        Toast.makeText(getActivity(), "No Agents Available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Agents could not be loaded. Check Network Connection", Toast.LENGTH_LONG).show();
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

    private boolean validateAllEntries(){
        String username = username_entry.getText().toString();
        String password = password_entry.getText().toString();
        String repassword = password_reentry.getText().toString();
        String street = street_entry.getText().toString();
        String city = city_entry.getText().toString();
        String state = state_spinner.getSelectedItem().toString();
        String zip = zip_entry.getText().toString();
        String name = name_entry.getText().toString();
        String phone = phone_entry.getText().toString();
        String email = email_entry.getText().toString();

        if(!agentSelected){
            Toast.makeText(getActivity(), "Select an Agent.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.length() < 1) {
            username_entry.setError("You must enter a username.");
            return false;
        }

        if(password.length() < 1) {
            password_entry.setError("Please enter a password");
            return false;
        }

        if(password_reentry.getError() != null || repassword.length() < 1) {
            password_reentry.setError("Passwords do not match");
            return false;
        }

        if(street.length() < 1) {
            street_entry.setError("You must enter your address.");
            return false;
        }

        if(city.length() < 1) {
            city_entry.setError("You must enter your city.");
            return false;
        }

        if(state.length() > 2) {
            Toast.makeText(getActivity(), "Please select a state.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(zip.length() != 5) {
            zip_entry.setError("Enter a valid zip-code.");
            return false;
        }

        if(name.length() < 1) {
            name_entry.setError("You must enter your name.");
            return false;
        }

        //TODO make it check for exact ammounts (xxx-xxxx or xxx-xxx-xxxx or 1-xxx-xxx-xxxx)
        if(phone.length() < 8 || phone.length() > 8 && phone.length() < 14) {
            phone_entry.setError("Please enter a phone number");
            return false;
        }

        //check for valid email
        if (email.length() < 1) {
            email_entry.setError("Please enter a valid email address");
            return false;
        }

        return true;
    }

    /**
     * Performs validation of all field entries and signs up a new user
     *
     */
    private void registerUser(){
        if(validateAllEntries()) {
            String phoneNumber = phone_entry.getText().toString();
            phoneNumber = phoneNumber.replace("(", "");
            phoneNumber = phoneNumber.replace(")", "");
            phoneNumber = phoneNumber.replace("-", "");
            phoneNumber = phoneNumber.replace(" ", "");

            final String agentID = agentList.get(agent_spinner.getSelectedItemPosition()).getObjectId();

            ParseUser newUser = new ParseUser();

            //TODO validate all entries
            newUser.setUsername(username_entry.getText().toString());
            newUser.put("Address", street_entry.getText().toString());
            newUser.put("City", city_entry.getText().toString());
            newUser.put("State", state_spinner.getSelectedItem().toString());
            newUser.put("Name", name_entry.getText().toString());
            newUser.put("AgentID", agentID);
            newUser.put("accountType", "Client");
            newUser.setEmail(email_entry.getText().toString());
            newUser.setPassword(password_entry.getText().toString());
            newUser.put("Zip", Double.valueOf(zip_entry.getText().toString()));
            newUser.put("phoneNumber", Double.valueOf(phoneNumber));

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        signUpSuccess();
                    } else {
                        String message = e.getMessage();

                        if(message.contains("email")) {
                            email_entry.setError("Please enter a valid email address");
                        } else {
                            switch (e.getCode()){
                                case 100:
                                    //cleared the message from here so it doesn't show up on parse connection
                                    break;

                                default:
                                    message = "Could Not Create User.";
                                    break;
                            }

                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                    }
                }
            });
        }
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
                if (parseUser != null) {
                    Intent intent = new Intent(getActivity(), Splash.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Could Not Log In: " +
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}