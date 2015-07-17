package com.example.jaz020.clientoneamfam;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class MyPoliciesFragment extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_policies, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Set up the recycler view */
        rv = (RecyclerView) view.findViewById(R.id.policyRecyclerView);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(llm);

        try {
            List<ParseObject> claims = new ArrayList<>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Policy");
            query.whereEqualTo("AgentID", ParseUser.getCurrentUser().getObjectId());
            List<ParseObject> policies = query.find();

            for (int i = 0; i < policies.size(); i++) {
                ParseQuery<ParseObject> claimQuery = ParseQuery.getQuery("Claim");
                claimQuery.whereEqualTo("PolicyID", policies.get(i).getObjectId());

                claims.addAll(claimQuery.find());
            }

            if (claims.size() != 0) {
//                /* Attach adapter to recycler view */
//                PolicyRVAdapter adapter = new RVAdapter(claims, "Claims");
//                rv.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "No Claims Found", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException pe) {  pe.printStackTrace(); }
    }
}
