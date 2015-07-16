package com.example.jaz020.clientoneamfam;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


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
    private EditText agent_entry;
    private Button register_button;
    private Spinner state_spinner;


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

        username_entry = (EditText) view.findViewById(R.id.username_entry);
        agent_entry = (EditText) view.findViewById(R.id.agent_entry);
        password_entry = (EditText) view.findViewById(R.id.password_entry);
        password_reentry = (EditText) view.findViewById(R.id.password_reentry);
        name_entry = (EditText) view.findViewById(R.id.name_entry);
        phone_entry = (EditText) view.findViewById(R.id.phone_entry);
        email_entry = (EditText) view.findViewById(R.id.email_entry);

        street_entry = (EditText) view.findViewById(R.id.address_street);
        city_entry = (EditText) view.findViewById(R.id.address_city);
        zip_entry = (EditText) view.findViewById(R.id.zip_code);

        state_spinner = (Spinner) view.findViewById(R.id.state_spinner);

        register_button = (Button) view.findViewById(R.id.register_button);

        //POPULATE STATE DROPDOWN
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        state_spinner.setAdapter(adapter);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //TODO TEXTWATCHER FOR PASSWORDS

    }

    private void registerUser(){

        String phoneUpdate = phone_entry.getText().toString();
        phoneUpdate = phoneUpdate.replace("(","");
        phoneUpdate = phoneUpdate.replace(")","");
        phoneUpdate = phoneUpdate.replace("-","");
        phoneUpdate = phoneUpdate.replace(" ","");

        ParseUser newUser = new ParseUser();

        newUser.setUsername(username_entry.getText().toString());
        newUser.put("Address", street_entry.getText().toString());
        newUser.put("City", city_entry.getText().toString());
        newUser.put("State", state_spinner.getSelectedItem().toString());
        newUser.put("Zip", Double.valueOf(zip_entry.getText().toString()));
        newUser.put("Name", name_entry.getText().toString());
        newUser.put("phoneNumber", Double.valueOf(phoneUpdate));
        newUser.put("AgentID", agent_entry.getText().toString());
        newUser.setEmail(email_entry.getText().toString());

        String p1 = password_entry.getText().toString();
        String p2 = password_reentry.getText().toString();

        if(p1.equals(p2) && (!p1.equals(""))){
            newUser.setPassword(p1);
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
