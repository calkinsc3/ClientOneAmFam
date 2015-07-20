package com.example.jaz020.clientoneamfam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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

    private FrameLayout rootView;
    private LinearLayout backgroundView;

    private ImageButton imageThumb;
    private ImageView expandedImage;
    private TextView nameText;
    private TextView descriptionText;
    private Button emailButton;

    private Animator currentAnimator;
    private int animationDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meet_navneet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = (FrameLayout) view.findViewById(R.id.navneet_fragment_root_layout);
        backgroundView = (LinearLayout) view.findViewById(R.id.navneet_fragment_linear_layout);
        imageThumb = (ImageButton) view.findViewById(R.id.picture);
        expandedImage = (ImageView) view.findViewById(R.id.expanded_image);
        nameText = (TextView) view.findViewById(R.id.name_navneet);
        descriptionText = (TextView) view.findViewById(R.id.descriptionText);
        emailButton = (Button) view.findViewById(R.id.emailNavneetButton);

        setDescription();

        emailButtonListener();

        imageClickListener();
    }

    private void setDescription() {
        String description =
                "Navneet is currently a student at the University of Wisconsin - Madison studying" +
                " Computer Sciences.\n\nSkills:\n  Languages: Java, C++, C, XML, JSON" +
                "\n  Operating Systems: Linux, Unix, OS X, Windows\n  Software: Android Studio, " +
                "Eclipse, Xcode\n\n\nIn his free time, Navneet loves to referee soccer, watch " +
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
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(imageThumb, R.drawable.navneet);
            }
        });
    }

    private void zoomImageFromThumb(final View thumbView, int imageID) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        expandedImage.setImageResource(imageID);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rootView.findViewById(R.id.navneet_fragment_root_layout)
                .getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();

            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;

            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();

            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;

            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail and
        // the background will fade to transparent black.
        thumbView.setAlpha(0f);
        backgroundView.setAlpha(.7f);
        backgroundView.setBackgroundColor(Color.BLACK);
        nameText.setTextColor(Color.DKGRAY);

        expandedImage.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the
        // top-left corner of the zoomed-in view (the default is the center of the view).
        expandedImage.setPivotX(0f);
        expandedImage.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(expandedImage, View.X,
                startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImage,
                View.SCALE_Y, startScale, 1f));

        set.setDuration(animationDuration);
        set.setInterpolator(new DecelerateInterpolator());

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });

        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImage, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_Y, startScaleFinal));

                set.setDuration(animationDuration);
                set.setInterpolator(new DecelerateInterpolator());

                // Sets the view to the normal view when animation is either done or cancelled.
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        backgroundView.setAlpha(1f);
                        backgroundView.setBackgroundColor(Color.TRANSPARENT);
                        nameText.setTextColor(Color.BLACK);

                        expandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        backgroundView.setAlpha(1f);
                        backgroundView.setBackgroundColor(Color.TRANSPARENT);
                        nameText.setTextColor(Color.BLACK);

                        expandedImage.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });

                set.start();
                currentAnimator = set;
            }
        });
    }
}