package com.example.jaz020.clientoneamfam;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;


/**
 * Created by nsr009 on 7/23/2015.
 */
public class InternsRVAdapter extends RecyclerView.Adapter<InternsRVAdapter.ViewHolder> {

    private String[] internNames;
    private HashMap<String, Integer> internPictureMap;

    InternsRVAdapter(String[] internNames, HashMap<String, Integer> internPictureMap) {
        this.internNames = internNames;
        this.internPictureMap = internPictureMap;
    }

    @Override
    public int getItemCount() {
        if (internNames != null) {
            return internNames.length;
        }

        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.intern_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int i) {
        vh.name.setText(internNames[i]);

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
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;

        ImageView image;
        TextView name;

        ViewHolder(View view) {
            super(view);

            cv = (CardView) view.findViewById(R.id.intern_card_view);

            image = (ImageView) view.findViewById(R.id.internPicture);
            name = (TextView) view.findViewById(R.id.internName);
        }

        public CardView getCardView() {
            return cv;
        }
    }
}