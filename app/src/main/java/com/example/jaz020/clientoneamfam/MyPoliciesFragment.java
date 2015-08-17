package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * MyPoliciesFragement displays a list of the policies that the user currently holds
 *
 * @author nreddy
 */
public class MyPoliciesFragment extends Fragment {

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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_my_policies, container, false);
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

        /* Set up the recycler view */
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.policyRecyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Policy");
            query.whereEqualTo("ClientID", ParseUser.getCurrentUser().getObjectId());
            //todo findinbackground
            List<ParseObject> policies = query.find();

            if (!policies.isEmpty()) {
                /* Attach adapter to recycler view */
                RVAdapter adapter = new RVAdapter("Policies", policies);
                rv.setAdapter(adapter);
            } else {
                //todo dialog
                Toast.makeText(getActivity(), "No Policies Found", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException pe) {  pe.printStackTrace(); }
    }

    /**
     * On create options menu.
     *
     * @param menu the menu
     * @param inflater the inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optional_action).setVisible(true);
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_add);
        menu.findItem(R.id.optional_action).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * On options item selected.
     *
     * @param item the item
     * @return the boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optional_action:
                PolicyScreenFragment policyScreenFragment = new PolicyScreenFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("ISNEW", true);
                policyScreenFragment.setArguments(bundle);

                Tools.replaceFragment(R.id.fragment_container, policyScreenFragment,
                        Singleton.getFragmentManager(), true);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}