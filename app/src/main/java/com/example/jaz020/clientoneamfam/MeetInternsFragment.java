package com.example.jaz020.clientoneamfam;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetInternsFragment extends Fragment {

    private String[] internNames;
    private HashMap<String, Integer> internPictureMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meet_interns, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setInterns();

        /* Set up the recycler view */
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.internsRecyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

         /* Attach adapter to recycler view */
        InternsRVAdapter adapter = new InternsRVAdapter(internNames, internPictureMap);
        rv.setAdapter(adapter);
    }

    private void setInterns() {
        internNames = getResources().getStringArray(R.array.intern_names);

        internPictureMap = new HashMap<>();
        internPictureMap.put(internNames[0], R.drawable.evan_headshot);
        internPictureMap.put(internNames[1], R.drawable.levi);
        internPictureMap.put(internNames[2], R.drawable.navneet);
        internPictureMap.put(internNames[3], R.drawable.james_world_party_time_excellent);
    }
}