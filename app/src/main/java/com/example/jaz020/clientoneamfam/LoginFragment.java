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

    EditText username_entry;
    EditText password_entry;

    CheckBox username_checkbox;
    CheckBox login_checkbox;

    Button login_button;

    SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                        "", "Signing in to Parse.com", true);

                final String username = username_entry.getText().toString();
                String password = password_entry.getText().toString();

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

                        } else if (user == null) {
                            loginFail();
                        } else {
                            loginError(e);
                        }
                    }
                });
            }
        });
    }

    public void loginSuccess() {
        Intent intent = new Intent(getActivity(), Splash.class);
        startActivity(intent);
    }

    public void loginFail() {
        Toast.makeText(getActivity(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
    }

    public void loginError(ParseException e) {
        Toast.makeText(getActivity(), "Login Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}