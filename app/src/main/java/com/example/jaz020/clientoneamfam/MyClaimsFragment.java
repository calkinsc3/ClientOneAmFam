package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyClaimsFragment extends Fragment {

    /**
     * The Fab.
     */
    com.github.clans.fab.FloatingActionButton fab;
    /**
     * The RecyclerView.
     */
    RecyclerView rv;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_claims, container, false);
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

        fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab);
        rv = (RecyclerView) view.findViewById(R.id.claims_recycler_view);

         /* Set up the recycler view */
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        //set fab button
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setVisibility(View.VISIBLE);

        getClaims();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle testBundle = new Bundle();
                ClaimScreenFragment claim = new ClaimScreenFragment();
                testBundle.putBoolean("ISNEW", true);
                claim.setArguments(testBundle);
                Tools.replaceFragment(R.id.fragment_container, claim, getFragmentManager(), true);
            }
        });
    }

    /**
     * queries parse for queries where the clientID = the currentOID
     */
    private void getClaims() {
        try {

            //get a list of all policies for current user
            ParseQuery query = ParseQuery.getQuery("Policy");
            query.whereEqualTo("ClientID", ParseUser.getCurrentUser().getObjectId());

            //todo find in background
            List<ParseObject> userPolicies = query.find();
            List<ParseQuery<ParseObject>> queries = new ArrayList<>();

            for(ParseObject policy: userPolicies){
                queries.add(ParseQuery.getQuery("Claim").whereEqualTo("PolicyID", policy.getObjectId()));
            }

            if(!queries.isEmpty()) {
                //query claims for items attached to each policy
                ParseQuery<ParseObject> claimQuery = ParseQuery.or(queries);

                List<ParseObject> claims = claimQuery.find();

                if(!claims.isEmpty()) {
                    /* Attach adapter to recycler view */
                    RVAdapter adapter = new RVAdapter("Claims", claims);
                    rv.setAdapter(adapter);
                } else {
                    //todo dialog
                    Toast.makeText(getActivity(), "No Claims Found", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (ParseException e) {
            Log.e("Exception: ", e.getMessage());
        }
    }
}