package com.example.jaz020.clientoneamfam;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;


/**
 * Custom recycler view for the meet the interns page
 *
 * @author nreddy
 */
public class InternsRVAdapter extends RecyclerView.Adapter<InternsRVAdapter.ViewHolder> {

    private String[] internNames;
    private HashMap<String, Integer> internPictureMap;

    private LinearLayout backgroundView;
    private ImageView expandedImage;
    private TextView internTitleText;

    private FrameLayout frameLayout;

    private RecyclerView recyclerView;

    /**
     * Instantiates a new Interns rV adapter.
     *
     * @param internNames the intern names
     * @param internPictureMap the intern picture map
     * @param backgroundView the background view
     * @param expandedImage the expanded image
     * @param internTitleText the intern title text
     * @param recyclerView the recycler view
     * @param frameLayout the frame layout
     */
    InternsRVAdapter(String[] internNames, HashMap<String, Integer> internPictureMap,
                     LinearLayout backgroundView, ImageView expandedImage, TextView internTitleText,
                     RecyclerView recyclerView, FrameLayout frameLayout) {
        this.internNames = internNames;
        this.internPictureMap = internPictureMap;

        this.backgroundView = backgroundView;
        this.expandedImage = expandedImage;
        this.internTitleText = internTitleText;

        this.frameLayout = frameLayout;
        this.recyclerView = recyclerView;
    }

    /**
     * Gets item count.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        if (internNames != null) {
            return internNames.length;
        }

        return 0;
    }

    /**
     * On create view holder.
     *
     * @param viewGroup the view group
     * @param i the i
     * @return the view holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.intern_card,
                viewGroup, false);

        return new ViewHolder(v);
    }

    /**
     * On bind view holder.
     *
     * @param vh the vh
     * @param i the i
     */
    @Override
    public void onBindViewHolder(final ViewHolder vh, final int i) {
        vh.name.setText(internNames[i]);
        vh.image.setBackgroundColor(Color.TRANSPARENT);

        /* Load picture into current card's image view */
        Picasso.with(Singleton.getContext())
                .load(internPictureMap.get(internNames[i]))
                .fit()
                .centerInside()
                .into(vh.image);

        vh.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (vh.name.getText().toString()) {
                    case "Evan Feller":
                        Tools.replaceFragment(R.id.fragment_container, new MeetEvanFragment(),
                                Singleton.getFragmentManager(), true);
                        break;

                    case "Levi Lavender":
                        Tools.replaceFragment(R.id.fragment_container, new MeetLeviFragment(),
                                Singleton.getFragmentManager(), true);
                        break;

                    case "Navneet Reddy":
                        Tools.replaceFragment(R.id.fragment_container, new MeetNavneetFragment(),
                                Singleton.getFragmentManager(), true);
                        break;

                    case "James Ziglinski":
                        Tools.replaceFragment(R.id.fragment_container, new MeetJamesFragment(),
                                Singleton.getFragmentManager(), true);
                        break;

                    default:
                        break;
                }
            }
        });

        vh.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageZoom(internPictureMap.get(internNames[i]), vh.image, expandedImage, null,
                        internTitleText, null, null, null, null, null, null,
                        frameLayout, backgroundView, recyclerView);
            }
        });
    }

    /**
     * On attached to recycler view.
     *
     * @param recyclerView the recycler view
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * The type View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Cv.
         */
        CardView cv;

        /**
         * The Image.
         */
        ImageButton image;
        /**
         * The Name.
         */
        TextView name;

        /**
         * Instantiates a new View holder.
         *
         * @param view the view
         */
        ViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.intern_card_view);

            image = (ImageButton) view.findViewById(R.id.internPicture);
            name = (TextView) view.findViewById(R.id.internName);
        }

        /**
         * Gets card view.
         *
         * @return the card view
         */
        public CardView getCardView() {
            return cv;
        }
    }
}