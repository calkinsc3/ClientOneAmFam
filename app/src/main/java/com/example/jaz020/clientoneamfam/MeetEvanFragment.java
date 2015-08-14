package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetEvanFragment extends Fragment {

    /**
     * On create view.
     *
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_evan, container, false);

        ImageView mainImg = (ImageView) view.findViewById(R.id.mainImage);

        ImageButton linked = (ImageButton) view.findViewById(R.id.evan_linked_button);
        ImageButton github = (ImageButton) view.findViewById(R.id.evan_github_button);
        ImageButton gmail = (ImageButton) view.findViewById(R.id.evan_gmail_button);
        ImageButton stack = (ImageButton) view.findViewById(R.id.evan_stack_button);
        ImageButton code = (ImageButton) view.findViewById(R.id.evan_code_academy_button);

        Picasso.with(getActivity()).load(R.drawable.evan_headshot).fit().centerCrop().into(mainImg);
        Picasso.with(getActivity()).load(R.drawable.stackoverflow_logo_evan).resize(400, 325).into(stack);
        Picasso.with(getActivity()).load(R.drawable.linked_in_logo).resize(500, 150).into(linked);
        Picasso.with(getActivity()).load(R.drawable.githug_logo).resize(400,150).into(github);
        Picasso.with(getActivity()).load(R.drawable.gmail_logo_evan).resize(400,150).into(gmail);
        Picasso.with(getActivity()).load(R.drawable.code_academy_logo_evan).resize(550, 125).into(code);

        linked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.linkedin.com/in/evfeller"));
                startActivity(browserIntent);
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.github.com/fellere"));
                startActivity(browserIntent);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                String[] emails = {"evanfeller.se@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails);
                intent.putExtra(Intent.EXTRA_SUBJECT, "From Intern App");
                startActivity(intent);
            }
        });

        stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://stackoverflow.com/users/3803190/evan-feller"));
                startActivity(browserIntent);
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.codecademy.com/fellere"));
                startActivity(browserIntent);
            }
        });

        return view;
    }
}