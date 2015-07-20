package com.example.jaz020.clientoneamfam;

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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by nsr009 on 6/10/2015.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;

        ImageView image;
        ImageButton editButton;
        TextView description;
        TextView cost;

        ViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.list_card_view);

            image = (ImageView) view.findViewById(R.id.cardImage);
            editButton = (ImageButton) view.findViewById(R.id.cardEditButton);
            description = (TextView) view.findViewById(R.id.cardDescription);
            cost = (TextView) view.findViewById(R.id.cardCost);
        }

        public CardView getCardView() {
            return cv;
        }
    }

    List<ParseObject> objectsToDisplay;
    String cardType;

    RVAdapter(String cardType, List<ParseObject> objectsToDisplay) {
        this.cardType = cardType;
        this.objectsToDisplay = objectsToDisplay;
    }

    @Override
    public int getItemCount() {
        if(objectsToDisplay != null) {
            return objectsToDisplay.size();
        }

        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder ivh, int i) {
        final ParseObject currentObject = objectsToDisplay.get(i);

        switch (cardType) {
            /* Display cards for list of policies */
            case "Policies":{
                ivh.image.setVisibility(View.GONE);

                try {
                    ParseQuery<ParseObject> claimQuery = new ParseQuery<>("Upload");
                    claimQuery.whereEqualTo("PolicyID", currentObject.getObjectId());

                    List<ParseObject> uploads = claimQuery.find();

                    if (!uploads.isEmpty()) {
                        String url = uploads.get(0).getParseFile("Media").getUrl();

                        ivh.image.setVisibility(View.VISIBLE);

                            /* Load picture into current card's image view */
                        Picasso.with(Singleton.getContext())
                                .load(url)
                                .fit()
                                .centerInside()
                                .into(ivh.image);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String cost = currentObject.getNumber("Cost").toString();
                BigDecimal parsed = new BigDecimal(cost).setScale(2, BigDecimal.ROUND_FLOOR);
                String formattedCost = NumberFormat.getCurrencyInstance().format(parsed);

                ivh.cost.setText(formattedCost);
                ivh.description.setText(currentObject.getString("Description"));

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
                                        .fit()
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


                ivh.image.setVisibility(View.VISIBLE);
                ivh.editButton.setVisibility(View.GONE);


                // TODO Handle clicks.
                /* Handle card click */
                ivh.getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Singleton.setCurrentClaim(currentObject);

//                        Tools.replaceFragment(R.id.fragment_container, new ClaimScreenFragment(),
//                                Singleton.getFragmentManager(), true);
                    }
                });

                break;
            }

            default:
                Log.e("CardRVAdapter", cardType + " passed in as argument.");
                break;

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}