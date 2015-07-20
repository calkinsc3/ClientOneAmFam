package com.example.jaz020.clientoneamfam;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
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


    public MyClaimsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_claims, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         /* Set up the recycler view */
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.claims_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);



        try {

            //get a list of all policies for current user
            ParseQuery query = ParseQuery.getQuery("Policy");
            query.whereEqualTo("ClientID", ParseUser.getCurrentUser().getObjectId());

            List<ParseObject> userPolicies = query.find();
            List<ParseQuery<ParseObject>> queries = new ArrayList<>();

            for(ParseObject policy: userPolicies){
            queries.add(new ParseQuery<>("Claim").whereEqualTo("PolicyID", policy.getObjectId()));

            }

            if(!queries.isEmpty()) {
                //query claims for items attached to each policy
                ParseQuery<ParseObject> claimQuery = ParseQuery.or(queries);

                claimQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> claims, ParseException e) {

                        if(e == null && (!claims.isEmpty())){

                            //load claims

                        }
                        else if(e == null){

                            Toast.makeText(getActivity(), "No Claims Found", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }


        }catch (ParseException e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
