package com.example.jaz020.clientoneamfam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by nsr009 on 7/23/2015.
 */
public class ImageZoom {

    private Animator currentAnimator;
    private int animationDuration;

    private int imageID;
    private ImageButton imageThumb;
    private ImageView expandedImage;
    private Button emailButton;
    private TextView nameText;
    private TextView jobTitleText;
    private TextView jobTeamText;
    private TextView linkedinText;
    private TextView githubText;
    private TextView aboutText;
    private TextView descriptionText;
    private FrameLayout rootView;
    private LinearLayout backgroundView;
    private RecyclerView recyclerView;

    public ImageZoom(int imageID, ImageButton imageThumb, ImageView expandedImage,
                     Button emailButton, TextView nameText, TextView jobTitleText,
                     TextView jobTeamText, TextView linkedinText, TextView githubText,
                     TextView aboutText, TextView descriptionText, FrameLayout rootView,
                     LinearLayout backgroundView, RecyclerView recyclerView) {
        this.imageID = imageID;
        this.imageThumb = imageThumb;
        this.expandedImage = expandedImage;
        this.emailButton = emailButton;
        this.nameText = nameText;
        this.jobTitleText = jobTitleText;
        this.jobTeamText = jobTeamText;
        this.linkedinText = linkedinText;
        this.githubText = githubText;
        this.aboutText = aboutText;
        this.descriptionText = descriptionText;
        this.rootView = rootView;
        this.backgroundView = backgroundView;
        this.recyclerView = recyclerView;

        zoomImageFromThumb();
    }

    private void zoomImageFromThumb() {
        animationDuration = Singleton.getContext()
                .getResources().getInteger(android.R.integer.config_shortAnimTime);

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
        imageThumb.getGlobalVisibleRect(startBounds);

        if (recyclerView == null) {
            if (Singleton.getContext().getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                rootView.findViewById(R.id.navneet_fragment_portrait_layout)
                        .getGlobalVisibleRect(finalBounds, globalOffset);
            } else {
                rootView.findViewById(R.id.navneet_fragment_landscape_layout)
                        .getGlobalVisibleRect(finalBounds, globalOffset);
            }
        } else {
            rootView.findViewById(R.id.flayout).getGlobalVisibleRect(finalBounds, globalOffset);
        }

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

        prepareVisibilitiesForAnimation();

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
                        endAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        endAnimation();
                    }
                });

                set.start();
                currentAnimator = set;
            }
        });
    }

    private void prepareVisibilitiesForAnimation() {
        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail and
        // the background will fade to transparent black.
        imageThumb.setAlpha(0f);
        backgroundView.setAlpha(.7f);
        backgroundView.setBackgroundColor(Color.BLACK);

        nameText.setTextColor(Color.DKGRAY);
        nameText.setBackgroundColor(Color.TRANSPARENT);

        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        } else {
            emailButton.setAlpha(.5f);

            aboutText.setBackgroundColor(Color.TRANSPARENT);
            descriptionText.setBackgroundColor(Color.TRANSPARENT);
            jobTitleText.setBackgroundColor(Color.TRANSPARENT);
            jobTeamText.setBackgroundColor(Color.TRANSPARENT);
            linkedinText.setBackgroundColor(Color.TRANSPARENT);
            githubText.setBackgroundColor(Color.TRANSPARENT);
        }

        expandedImage.setVisibility(View.VISIBLE);
        expandedImage.setAlpha(1f);
    }

    protected void endAnimation() {
        imageThumb.setAlpha(1f);
        backgroundView.setAlpha(1f);
        backgroundView.setBackgroundColor(Color.TRANSPARENT);

        nameText.setTextColor(Color.BLACK);
        nameText.setBackgroundColor(Singleton.getContext()
                .getResources().getColor(R.color.cloudy_white));

        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emailButton.setAlpha(1f);

            aboutText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
            descriptionText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
            jobTitleText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
            jobTeamText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
            linkedinText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
            githubText.setBackgroundColor(Singleton.getContext()
                    .getResources().getColor(R.color.cloudy_white));
        }

        expandedImage.setVisibility(View.GONE);
        currentAnimator = null;
    }
}