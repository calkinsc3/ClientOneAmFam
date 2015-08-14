package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetJamesFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    /**
     * The Github _ button.
     */
    ImageButton github_button;
    /**
     * The Linkedin _ button.
     */
    ImageButton linkedin_button;
    /**
     * The Facebook _ button.
     */
    ImageButton facebook_button;
    /**
     * The Stack _ exchange _ button.
     */
    ImageButton stack_exchange_button;
    /**
     * The Codecademy _ button.
     */
    ImageButton codecademy_button;
    /**
     * The Google _ button.
     */
    ImageButton google_button;
    /**
     * The Email _ james _ button.
     */
    Button email_james_button;
    /**
     * The View flipper.
     */
    ViewFlipper viewFlipper;
    /**
     * The Context.
     */
    Context context;

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
        context = getActivity();
        return inflater.inflate(R.layout.fragment_meet_james, container, false);
    }

    /**
     * On view created.
     *
     * @param view the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeButtons(view);

        setListeners();

        new Thread(new Runnable() {
            @Override
            public void run() {
                setUpFlipper();
            }
        }).start();
    }

    /**
     * maps the views to the respective code
     * @param view holds the individual views
     */
    private void initializeButtons(View view){
        github_button = (ImageButton) view.findViewById(R.id.github_button);
        linkedin_button= (ImageButton) view.findViewById(R.id.linkedin_button);
        facebook_button = (ImageButton) view.findViewById(R.id.facebook_button);
        stack_exchange_button = (ImageButton) view.findViewById(R.id.stack_exchange_button);
        codecademy_button = (ImageButton) view.findViewById(R.id.codecademy_button);
        google_button = (ImageButton) view.findViewById(R.id.google_plus_button);

        email_james_button = (Button) view.findViewById(R.id.email_james_button);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
    }

    /**
     * sets up the slideshow flipper
     */
    private void setUpFlipper(){
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
        viewFlipper.startFlipping();

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    /**
     * sets the listeners for all buttons on the page
     */
    private void setListeners(){
        github_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to github account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/jimmy2394"));
                startActivity(intent);
            }
        });

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to facebook account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/jimmy.ziglinski"));
                startActivity(intent);
            }
        });

        linkedin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to linkedin account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/pub/james-ziglinski/78/637/776"));
                startActivity(intent);
            }
        });

        stack_exchange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to stackoverflow account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://stackoverflow.com/users/5140012/james-ziglinski"));
                startActivity(intent);
            }
        });

        codecademy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to codecademy account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.codecademy.com/jimmy_2394"));
                startActivity(intent);
            }
        });

        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to google account
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://plus.google.com/u/0/103284925479978448579/posts"));
                startActivity(intent);
            }
        });

        email_james_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, "ziglinski@wisc.edu");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Message from OneAmFam Client");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi James! Would you like a job?");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }

    /**
     * The type Swipe gesture detector.
     */
    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        /**
         * On fling.
         *
         * @param e1 the e 1
         * @param e2 the e 2
         * @param velocityX the velocity x
         * @param velocityY the velocity y
         * @return the boolean
         */
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