package com.example.jaz020.clientoneamfam;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetJamesFragment extends Fragment {

    public MeetJamesFragment() {
        // Required empty public constructor
    }

    ImageButton github_button;
    ImageButton linkedin_button;
    ImageButton facebook_button;
    ImageButton stack_exchange_button;
    ImageButton codecademy_button;
    ImageButton google_button;
    ViewFlipper viewFlipper;
    Context context;

    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        return inflater.inflate(R.layout.fragment_meet_james, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeButtons(view);
        setUpFlipper(view);

        setListeners();
    }

    private void initializeButtons(View view){

        github_button = (ImageButton) view.findViewById(R.id.github_button);
        linkedin_button= (ImageButton) view.findViewById(R.id.linkedin_button);
        facebook_button = (ImageButton) view.findViewById(R.id.facebook_button);
        stack_exchange_button = (ImageButton) view.findViewById(R.id.stack_exchange_button);
        codecademy_button = (ImageButton) view.findViewById(R.id.codecademy_button);
        google_button = (ImageButton) view.findViewById(R.id.google_plus_button);


    }

    private void setUpFlipper(View view){
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);

        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
        viewFlipper.startFlipping();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewFlipper.stopFlipping();
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setListeners(){

        github_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jimmy2394"));
                startActivity(intent);
            }
        });

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/jimmy.ziglinski"));
                startActivity(intent);
            }
        });

        linkedin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/pub/james-ziglinski/78/637/776"));
                startActivity(intent);
            }
        });
        stack_exchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stackoverflow.com/users/5140012/james-ziglinski"));
                startActivity(intent);
            }
        });
        codecademy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.codecademy.com/jimmy_2394"));
                startActivity(intent);
            }
        });
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/103284925479978448579/posts"));
                startActivity(intent);
            }
        });

    }



    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
                    viewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_left));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right));
                    viewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

}