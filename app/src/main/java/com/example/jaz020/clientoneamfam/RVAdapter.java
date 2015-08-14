package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 * genereal RecyclerView adapter
 *
 * @author nreddy
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    /**
     * The Objects to display.
     */
    List<ParseObject> objectsToDisplay;
    /**
     * The Card type.
     */
    String cardType;

    /**
     * Instantiates a new RV adapter.
     *
     * @param cardType the card type
     * @param objectsToDisplay the objects to display
     */
    RVAdapter(String cardType, List<ParseObject> objectsToDisplay) {
        this.cardType = cardType;
        this.objectsToDisplay = objectsToDisplay;
    }

    /**
     * Gets item count.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        if(objectsToDisplay != null) {
            return objectsToDisplay.size();
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_card,
                viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * On bind view holder.
     *
     * @param ivh the ivh
     * @param i the i
     */
    @Override
    public void onBindViewHolder(final ViewHolder ivh, int i) {
        final ParseObject currentObject = objectsToDisplay.get(i);

        switch (cardType) {
            /* Display cards for list of policies */
            case "Policies":{
                ivh.image.setVisibility(View.GONE);

                try {
                    ParseQuery<ParseObject> policyQuery = new ParseQuery<>("Upload");
                    policyQuery.whereEqualTo("PolicyID", currentObject.getObjectId());

                    List<ParseObject> uploads = policyQuery.find();

                    if (!uploads.isEmpty()) {
                        String url = uploads.get(0).getParseFile("Media").getUrl();

                        ivh.image.setVisibility(View.VISIBLE);

                        /* Load picture into current card's image view */
                        Picasso.with(Singleton.getContext())
                                .load(url)
                                .resize(500, 500)
                                .centerInside()
                                .into(ivh.image);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String cost = currentObject.getNumber("Cost").toString();
                BigDecimal parsed = new BigDecimal(cost).setScale(2, BigDecimal.ROUND_FLOOR);
                String formattedCost = NumberFormat.getCurrencyInstance().format(parsed);

                String description = currentObject.getString("Description");

                ivh.cost.setText(formattedCost);
                ivh.description.setText(description);

                /* Handle card click */
                ivh.getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Singleton.setCurrentPolicy(currentObject);

                        Tools.replaceFragment(R.id.fragment_container, new PolicyScreenFragment(),
                                Singleton.getFragmentManager(), true);
                    }
                });

                /* Handle edit button click */
                ivh.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Singleton.setCurrentPolicy(currentObject);

                        PolicyScreenFragment policyScreenFragment = new PolicyScreenFragment();

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("ISEDIT", true);
                        policyScreenFragment.setArguments(bundle);

                        Tools.replaceFragment(R.id.fragment_container, policyScreenFragment,
                                Singleton.getFragmentManager(), true);
                    }
                });

                break;
            }
            /* Display cards for list of claims */
            case "Claims": {
                ivh.image.setVisibility(View.GONE);

                try {
                    ParseQuery<ParseObject> claimQuery = new ParseQuery<>("Upload");
                    claimQuery.whereEqualTo("ClaimID", currentObject.getObjectId());

                    claimQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {

                            if(e == null & !list.isEmpty()) {
                                String url = list.get(0).getParseFile("Media").getUrl();

                                ivh.image.setVisibility(View.VISIBLE);

                                /* Load picture into current card's image view */
                                Picasso.with(Singleton.getContext())
                                        .load(url)
                                        .resize(500, 500)
                                        .centerInside()
                                        .into(ivh.image);
                            }
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String damages = currentObject.getNumber("Damages").toString();
                BigDecimal parsed = new BigDecimal(damages).setScale(2, BigDecimal.ROUND_FLOOR);
                String formattedCost = NumberFormat.getCurrencyInstance().format(parsed);

                ivh.cost.setText(formattedCost);
                ivh.description.setText(currentObject.getString("Comment"));

                ivh.editButton.setVisibility(View.GONE);

                // TODO Handle clicks.
                /* Handle card click */
                ivh.getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Singleton.setCurrentClaim(currentObject);

                        Fragment fragment = new ClaimScreenFragment();
                        Tools.replaceFragment(R.id.fragment_container, fragment,
                                Singleton.getFragmentManager(), true);
                    }
                });

                break;
            }

            default:
                Log.e("CardRVAdapter", cardType + " passed in as argument.");
                break;
        }
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
        ImageView image;
        /**
         * The Edit button.
         */
        ImageButton editButton;
        /**
         * The Description.
         */
        TextView description;
        /**
         * The Cost.
         */
        TextView cost;

        /**
         * Instantiates a new View holder.
         *
         * @param view the view
         */
        ViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.list_card_view);

            image = (ImageView) view.findViewById(R.id.cardImage);
            editButton = (ImageButton) view.findViewById(R.id.cardEditButton);
            description = (TextView) view.findViewById(R.id.cardDescription);
            cost = (TextView) view.findViewById(R.id.cardCost);
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