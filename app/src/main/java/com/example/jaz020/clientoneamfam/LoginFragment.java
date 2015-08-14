package com.example.jaz020.clientoneamfam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginFragment extends Fragment {

    EditText username_entry, password_entry;
    CheckBox username_checkbox, login_checkbox;
    Button login_button;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields(view);

    }

    /**
     * Initializes all fields, sets the checkboxes of the login screen, and sets the on click listener
     * for the login button
     *
     * @param view main view
     */
    private void initializeFields(View view) {
        sharedPreferences = getActivity().getSharedPreferences("AmFam", 0);
        username_entry = (EditText) view.findViewById(R.id.username_entry);
        password_entry = (EditText) view.findViewById(R.id.password_entry);
        username_checkbox = (CheckBox) view.findViewById(R.id.remember_username_checkbox);
        login_checkbox = (CheckBox) view.findViewById(R.id.stay_logged_in_checkbox);
        login_button = (Button) view.findViewById(R.id.login_button);

        //set checkboxes and username if the user has data saved
        if (sharedPreferences.getString("Username", null) != null) {
            username_checkbox.setChecked(true);
            username_entry.setText(sharedPreferences.getString("Username", ""));
        }

        //LOGIN CLICK
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = username_entry.getText().toString();
                String password = password_entry.getText().toString();

                if(validateEntries(username, password)) {

                    //check for interner connection
                    if (Tools.isNetworkAvailable(getActivity())) {

                        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                                "", "Signing in to Parse.com", true);

                        ParseUser.logInInBackground(username, password, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                progressDialog.dismiss();

                                if (e == null && user != null) {
                                    //UPDATE SHARED PREFERENCES
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("UserID", user.getObjectId());
                                    editor.putBoolean("StayLoggedIn", login_checkbox.isChecked());

                                    if (username_checkbox.isChecked()) {
                                        editor.putString("Username", username);
                                    } else {
                                        editor.remove("Username");
                                    }

                                    editor.apply();

                                    //login successful
                                    loginSuccess();

                                } else if (e == null) {
                                    loginFail();
                                } else {
                                    loginError(e);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Called after a successful log in to Parse
     * Saves the user's info, and starts the main activity
     */
    public void loginSuccess() {
        password_entry.setText("");
        if(!username_checkbox.isChecked()) username_entry.setText("");
        Intent intent = new Intent(getActivity(), Splash.class);
        startActivity(intent);
    }

    /**
     * Informs user login has failed
     */
    public void loginFail() {
        Toast.makeText(getActivity(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles any login errors
     * @param e {@link com.parse.ParseException} thrown by the erroneous login
     */
    public void loginError(ParseException e) {
        String message;
        switch (e.getCode()){
            case 100:
                message = "Connection Timed Out";
                break;

            default:
                message = "Login Error";
                break;
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Validates the username and password entries before attempted login
     * @param username username_entry.getText()
     * @param password password_entry.getText()
     * @return true if valid, false otherwise
     */
    private boolean validateEntries(String username, String password){

        if(username.length() < 1){
            username_entry.setError("Enter Your Username");
            return false;
        }
        if(password.length() < 1){
            password_entry.setError("Enter Your Password.");
            return false;
        }
        return true;
    }


}