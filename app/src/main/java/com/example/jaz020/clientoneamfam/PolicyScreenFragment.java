package com.example.jaz020.clientoneamfam;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyScreenFragment extends Fragment {

    Bundle args;
    EditText policyDescription;
    EditText policyCost;
    EditText policyAddress;
    EditText city;
    EditText zip;
    Spinner stateSpinner;
    ImageButton addUploads;
    ListView uploadsList;
    ParseObject currentPolicy;
    ArrayList<ParseObject> uploads;
    ArrayAdapter stateAdapter;
    ObjectArrayAdapter imageAdapter;
    LinearLayout address2;

    public PolicyScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_policy_screen, container, false);

        initializeFields(view);
        checkOrientationSetLayoutOrientation();
        enableEditIfNewOrEditing();



        return view;
    }

    private void setAddUploadClickListener(){
        addUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT FOR MOVING TO GALLERY
                Intent intent = new Intent()
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                //WILL START A CHOOSER ACTIVITY WITH GALLERY AND OTHER OPTIONS IN IT
                startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), 0);
            }
        });
    }

    private void enableEditIfNewOrEditing(){
        if(args.getBoolean("ISNEW", false)){
            makeFieldsEditable();
            setHasOptionsMenu(true);
        } else if(args.getBoolean("ISEDIT", false)) {
            makeFieldsEditable();
            setHasOptionsMenu(true);
            policyWasSelectedPopulateViews();
        } else {
            policyWasSelectedPopulateViews();
        }
    }

    private void policyWasSelectedPopulateViews(){
        setFields();
        queryParseForUploads();
        setUploadsListAdapter();
    }

    private void makeFieldsEditable(){
        policyDescription.setClickable(true);
        policyCost.setClickable(true);
        policyAddress.setClickable(true);
        city.setClickable(true);
        zip.setClickable(true);
        addUploads.setVisibility(View.VISIBLE);
        addUploads.setClickable(true);

        setAddUploadClickListener();
    }

    private void initializeFields(View view){
        args = getArguments();
        policyDescription = (EditText)view.findViewById(R.id.policyDescription);
        policyCost = (EditText)view.findViewById(R.id.policyCost);
        policyAddress = (EditText)view.findViewById(R.id.policyAddress);
        city = (EditText)view.findViewById(R.id.city);
        zip = (EditText)view.findViewById(R.id.zip);
        addUploads = (ImageButton)view.findViewById(R.id.addUploadButton);
        uploadsList = (ListView)view.findViewById(R.id.uploadsList);
        address2 = (LinearLayout)view.findViewById(R.id.address2Layout);
        currentPolicy = Singleton.getCurrentPolicy();
        stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner = (Spinner)view.findViewById(R.id.stateSpinner);
        stateSpinner.setAdapter(stateAdapter);
    }

    private void setFields(){
        policyDescription.setText(currentPolicy.getString("Description"));
        policyCost.setText(String.valueOf(currentPolicy.getDouble("Cost")));
        policyAddress.setText(currentPolicy.getString("Address"));
        city.setText(currentPolicy.getString("City"));
        zip.setText(String.valueOf(currentPolicy.getInt("Zip")));
        stateSpinner.setSelection(stateAdapter.getPosition(currentPolicy.getString("State")));
    }

    private void setUploadsListAdapter(){
        imageAdapter = new ObjectArrayAdapter(getActivity(), R.layout.edit_upload_card, uploads);
        uploadsList.setAdapter(imageAdapter);
    }

    private void queryParseForUploads(){
        ParseQuery imageQuery = new ParseQuery("Upload");
        imageQuery.whereEqualTo("PolicyID", currentPolicy.getObjectId());

        try{
            uploads = (ArrayList<ParseObject>)imageQuery.find();
        } catch (com.parse.ParseException e) {
            Log.e("Upload Error", e.toString());
        }

    }

    public void checkOrientationSetLayoutOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            address2.setOrientation(LinearLayout.VERTICAL);
            city.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            zip.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optional_action).setVisible(true);
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_save);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //save action button enabled
            case R.id.optional_action:
                //todo create save action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ObjectArrayAdapter extends ArrayAdapter<ParseObject> {

        //declare Array List of items we create
        private ArrayList<ParseObject> uploads;

        /**
         * Constructor overrides constructor for array adapter
         * The only variable we care about is the ArrayList<PlatformVersion> objects
         * it is the list of the objects we want to display
         *
         * @param context The current context.
         * @param resource The resource ID for a layout file containing a layout to use when
         *                           instantiating views.
         * @param uploads The objects to represent in the ListView.
         */
        public ObjectArrayAdapter(Context context, int resource, ArrayList uploads) {
            super(context, resource, uploads);
            this.uploads = uploads;
        }

        /**
         * Creates a custom view for our list View and populates the data
         *
         * @param position position in the ListView
         * @param convertView the view to be inflated
         * @param parent the parent view
         * @return the view created
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder vHolder;
            final FragmentManager fm = getFragmentManager();

            /**
             * Checking to see if the view is null. If it is we must inflate the view
             * "inflate" means to render/show the view
             */

            if (view == null) {
                vHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.edit_upload_card, null);
                vHolder.policyImage = (ImageButton)view.findViewById(R.id.policyImage);
                vHolder.trash = (ImageButton)view.findViewById(R.id.deleteImageButton);
                vHolder.comments = (MultiAutoCompleteTextView)view.findViewById(R.id.imageDescription);

                view.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }

            vHolder.index = position;

            /**
             * Remember the variable position is sent in as an argument to this method.
             * The variable simply refers to the position of the current object on the list\
             * The ArrayAdapter iterate through the list we sent it
             */
            final ParseObject upload = uploads.get(position);

            if (upload != null) {
                // obtain a reference to the widgets in the defined layout
                ImageButton policyImage = vHolder.policyImage;
                ImageButton trash = vHolder.trash;
                MultiAutoCompleteTextView comments = vHolder.comments;

                if(policyImage != null){
                    Picasso.with(getContext()).load(upload.getParseFile("Media").getUrl()).fit().centerInside().into(vHolder.policyImage);
                }
                if(trash != null){
                    //todo enable trash
                }
                if(comments != null){
                    comments.setText(currentPolicy.getString("Comment"));
                }
            }

            // view must be returned to our current activity
            return view;
        }

        public class ViewHolder {
            ImageButton policyImage;
            ImageButton trash;
            MultiAutoCompleteTextView comments;

            int index;
        }
    }

}
