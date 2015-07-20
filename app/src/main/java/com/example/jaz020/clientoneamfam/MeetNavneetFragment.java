package com.example.jaz020.clientoneamfam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetNavneetFragment extends Fragment {

    private TextView descriptionText;
    private Button emailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meet_navneet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionText = (TextView) view.findViewById(R.id.descriptionText);
        emailButton = (Button) view.findViewById(R.id.emailNavneetButton);

        setDescription();

        emailButtonListener();
    }

    private void setDescription() {
        String description =
                ""; // TODO

        descriptionText.setText(description);
    }

    private void emailButtonListener() {
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nreddy@amfam.com"});

                if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(Intent.createChooser(emailIntent, "Choose Mail Application"));
            }
        });
    }
}