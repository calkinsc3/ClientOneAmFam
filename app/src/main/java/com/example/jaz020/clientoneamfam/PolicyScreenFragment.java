package com.example.jaz020.clientoneamfam;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyScreenFragment extends Fragment {

    private final int CHANGE_IMAGE = 1;
    private final int NEW_IMAGE = 0;
    ArrayList<Uri> images;
    private boolean policyWasCreated;
    private boolean hasImages;
    private Bundle args;
    private EditText policyDescription;
    private EditText policyCost;
    private EditText policyAddress;
    private EditText city;
    private EditText zip;
    private Spinner stateSpinner;
    private ImageButton addUploads;
    private ListView uploadsList;
    private ParseObject currentPolicy;
    private ArrayList<ParseObject> uploads;
    private ArrayAdapter stateAdapter;
    private ObjectArrayAdapter imageAdapter;
    private LinearLayout address2;

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
            currentPolicy = Singleton.getCurrentPolicy();
            makeFieldsEditable();
            setHasOptionsMenu(true);
            policyWasSelectedPopulateViews();
        } else {
            currentPolicy = Singleton.getCurrentPolicy();
            policyWasSelectedPopulateViews();
            disableStateSpinner();
        }
    }

    private void disableStateSpinner(){
        stateSpinner.setEnabled(false);
        stateSpinner.setClickable(false);
    }

    private void policyWasSelectedPopulateViews(){
        setFields();
        setUploadsListAdapter();
    }

    private void makeFieldsEditable(){
        policyDescription.setClickable(true);
        policyDescription.setFocusable(true);
        policyDescription.setFocusableInTouchMode(true);
        policyCost.setClickable(true);
        policyCost.setFocusable(true);
        policyCost.setFocusableInTouchMode(true);
        policyAddress.setClickable(true);
        policyAddress.setFocusable(true);
        policyAddress.setFocusableInTouchMode(true);
        city.setClickable(true);
        city.setFocusable(true);
        city.setFocusableInTouchMode(true);
        zip.setClickable(true);
        zip.setFocusable(true);
        zip.setFocusableInTouchMode(true);
        addUploads.setVisibility(View.VISIBLE);
        addUploads.setClickable(true);

        setAddUploadClickListener();
    }

    private void initializeFields(View view){
        hasImages = false;
        policyWasCreated = false;
        policyDescription = (EditText)view.findViewById(R.id.policyDescription);
        policyCost = (EditText)view.findViewById(R.id.policyCost);
        policyAddress = (EditText)view.findViewById(R.id.policyAddress);
        city = (EditText)view.findViewById(R.id.city);
        zip = (EditText)view.findViewById(R.id.zip);
        addUploads = (ImageButton)view.findViewById(R.id.addUploadButton);
        uploads = new ArrayList<>();
        uploadsList = (ListView)view.findViewById(R.id.uploadsList);
        address2 = (LinearLayout)view.findViewById(R.id.address2Layout);
        stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.states, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner = (Spinner)view.findViewById(R.id.stateSpinner);
        stateSpinner.setAdapter(stateAdapter);
        if(getArguments() != null){
            args = getArguments();
        } else {
            args = new Bundle();
        }
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
        queryParseForUploads();
        if(uploads.size() > 1) {
            imageAdapter = new ObjectArrayAdapter(getActivity(), R.layout.edit_upload_card, uploads);
            uploadsList.setAdapter(imageAdapter);
            hasImages = true;
        }
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

    private void checkOrientationSetLayoutOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            address2.setOrientation(LinearLayout.VERTICAL);
            city.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            zip.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }

    private boolean validateFields(){
        String error = getResources().getString(R.string.entryError);
        boolean validated = false;

        if(policyDescription.getText().toString().equals("")){
            policyDescription.setError(error);
        } else if (policyCost.getText().toString().equals("")){
            policyCost.setError(error);
        } else if(policyAddress.getText().toString().equals("")){
            policyAddress.setError(error);
        } else if(city.getText().toString().equals("")){
            city.setError(error);
        } else if (zip.getText().toString().equals("")){
            zip.setError(error);
        } else if (stateSpinner.getSelectedItem().toString().equals("Select a State")){
            Toast.makeText(getActivity(), "You must select a state", Toast.LENGTH_SHORT).show();
        } else {
            validated = true;
        }

        return validated;
    }

    private void savePolicyInformation(){
        if(args.getBoolean("ISNEW", false) && !policyWasCreated){
            currentPolicy = new ParseObject("Policy");
            currentPolicy.put("AgentID", ParseUser.getCurrentUser().getString("AgentID"));
            currentPolicy.put("ClientID", ParseUser.getCurrentUser().getObjectId());
            currentPolicy.put("Accepted", true);
        }
        currentPolicy.put("Description", policyDescription.getText().toString());
        currentPolicy.put("Cost", Double.valueOf(policyCost.getText().toString()));
        currentPolicy.put("Address", policyAddress.getText().toString());
        currentPolicy.put("City", city.getText().toString());
        currentPolicy.put("Zip", zip.getText().toString());
        currentPolicy.put("State", stateSpinner.getSelectedItem().toString());

        currentPolicy.saveEventually();

        Singleton.setCurrentPolicy(currentPolicy);
    }

    private void saveImages(){
        for(int i = 0; images.size() > i; i++){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(images.get(i)));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageBytes = stream.toByteArray();
                ParseFile image = new ParseFile("Photo.jpg", imageBytes, "jpeg");
                image.save();

                ParseObject mediaUpload = new ParseObject("Upload");
                mediaUpload.put("PolicyID", Singleton.getCurrentPolicy().getObjectId());
                mediaUpload.put("UserID", ParseUser.getCurrentUser().getObjectId());
                mediaUpload.put("Media", image);

                mediaUpload.save();

            } catch(FileNotFoundException e){
                Log.e("File Not Found", e.toString());
            } catch(com.parse.ParseException e) {
                Log.e("Error Saving", e.toString());
            }
        }
        setUploadsListAdapter();
    }

    private void saveImageComments(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //User is changing an image
                case CHANGE_IMAGE:

                    break;
                case NEW_IMAGE:

                    ClipData clipData = data.getClipData();
                    Uri targetUri;
                    images = new ArrayList<>();

                    if(clipData != null){

                        for(int i =0; clipData.getItemCount() > i; i++) {
                            // get the target Uri
                            targetUri = clipData.getItemAt(i).getUri();
                            //add to images array
                            images.add(targetUri);
                        }
                    } else {
                        //get target Uri
                        targetUri = data.getData();
                        images.add(targetUri);
                    }
                    saveImages();
                    break;
                default:
                    Log.d("Error: ", "Reached default in upload");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optional_action).setVisible(true);
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(R.id.optional_action).setTitle("Save");
        menu.findItem(R.id.optional_action).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //save action button enabled
            case R.id.optional_action:
                if(validateFields()) {
                    savePolicyInformation();
                    Toast.makeText(getActivity(), "Policy Saved", Toast.LENGTH_SHORT).show();
                }
                if (hasImages) {
                    saveImageComments();
                }
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
                    if(args.getBoolean("ISEDIT", false) || args.getBoolean("ISNEW", false)) {
                        trash.setVisibility(View.VISIBLE);
                        trash.setClickable(true);
                        trash.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
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
