package com.example.jaz020.clientoneamfam;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetNavneetFragment extends Fragment {

    private View view;
    private FrameLayout rootView;
    private LinearLayout backgroundView;

    private ImageButton imageThumb;
    private ImageView expandedImage;
    private TextView nameText;
    private TextView descriptionText;
    private Button emailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meet_navneet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields();

        setDescription();

        emailButtonListener();

        imageClickListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeFields();
    }

    private void initializeFields() {
        // Check the orientation of the device.
        // The fragment has different layouts for the different device orientations.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView = (FrameLayout) view.findViewById(R.id.navneet_fragment_portrait_layout);
            backgroundView = (LinearLayout) view.findViewById(R.id.navneet_fragment_linear_layout_portrait);

            imageThumb = (ImageButton) view.findViewById(R.id.picture_portrait);
            expandedImage = (ImageView) view.findViewById(R.id.expanded_image_portrait);

            nameText = (TextView) view.findViewById(R.id.name_navneet_portrait);
            descriptionText = (TextView) view.findViewById(R.id.descriptionText_portrait);

            emailButton = (Button) view.findViewById(R.id.emailNavneetButton_portrait);

            rootView.setVisibility(View.VISIBLE);
        } else {
            rootView = (FrameLayout) view.findViewById(R.id.navneet_fragment_landscape_layout);
            backgroundView = (LinearLayout) view.findViewById(R.id.navneet_fragment_linear_layout_landscape);

            imageThumb = (ImageButton) view.findViewById(R.id.picture_landscape);
            expandedImage = (ImageView) view.findViewById(R.id.expanded_image_landscape);

            nameText = (TextView) view.findViewById(R.id.name_navneet_landscape);
            descriptionText = (TextView) view.findViewById(R.id.descriptionText_landscape);

            emailButton = (Button) view.findViewById(R.id.emailNavneetButton_landscape);

            rootView.setVisibility(View.VISIBLE);
        }
    }

    private void setDescription() {
        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        String description =
                "Navneet is currently a student at the University of Wisconsin - Madison studying" +
                " Computer Sciences.\n\nSkills:\n  Languages: Java, C++, C, XML, JSON" +
                "\n  Operating Systems: Linux, Unix, OS X, Windows\n  Software: Android Studio, " +
                "Eclipse, Xcode\n\nIn his free time, Navneet loves to referee soccer, watch " +
                "sports, and write personal applications.";

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

    private void imageClickListener() {
        imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageZoom(R.drawable.navneet, imageThumb, expandedImage, nameText,
                        rootView, backgroundView, null);
            }
        });
    }
}