package com.example.jaz020.clientoneamfam;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Shows all data pertaining to the selected policy or creates a new policy
 *
 * @author llavender
 */
public class PolicyScreenFragment extends Fragment {

    private final int NEW_IMAGE = 0;
    private final int REPLACE_IMAGE = 1;

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

    private LinearLayout uploadsLayout;

    private RecyclerView uploadsList;

    private LinearLayoutManager llm;

    private ParseObject currentPolicy;

    private ArrayList<Uri> images;
    private ArrayList<ParseObject> uploads;
    private ArrayList<String> commentsList;

    private ArrayAdapter stateAdapter;
    private ImageRVAdapter imageAdapter;

    private LinearLayout address2;

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
        View view = inflater.inflate(R.layout.fragment_policy_screen, container, false);

        initializeFields(view);
        checkOrientationSetLayoutOrientation();
        enableEditIfNewOrEditing();

        return view;
    }

    /**
     * Sets the on click listener for the addImage button, enables uploading of images;
     *
     */
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

    /**
     * sets the listener for text changed on policyCost. Formats the number on text changed
     */
    private void setPolicyCostTextListener(){
        policyCost.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    policyCost.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    BigDecimal parsed = new BigDecimal(cleanString)
                            .setScale(2, BigDecimal.ROUND_FLOOR)
                            .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);

                    String formatted = NumberFormat.getCurrencyInstance().format(parsed);

                    current = formatted;
                    policyCost.setText(formatted);
                    policyCost.setSelection(formatted.length());

                    policyCost.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * If new Policy make fields editable; if edit make fields editable and populate views; if
     * policy selected disable spinner and populate views
     */
    private void enableEditIfNewOrEditing(){
        if(args.getBoolean("ISNEW", false)){
            makeFieldsEditable();
            setHasOptionsMenu(true);
            uploadsLayout.setVisibility(View.GONE);
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

    /**
     * disables the stateSpinner
     */
    private void disableStateSpinner(){
        stateSpinner.setEnabled(false);
        stateSpinner.setClickable(false);
    }

    /**
     * set the fields and the upload List adapter
     */
    private void policyWasSelectedPopulateViews(){
        setFields();
        setUploadsListAdapterAndComments();
    }

    /**
     * makes fields editable and sets the upload click listener and cost change listener
     */
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
        setPolicyCostTextListener();
    }

    /**
     * initalizes all global variables and sets required size of Recycler view
     * @param view the view attached to the fragment
     */
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

        uploadsList = (RecyclerView)view.findViewById(R.id.uploadsList);
        uploadsLayout = (LinearLayout)view.findViewById(R.id.uploadsLayout);
        llm = new LinearLayoutManager(getActivity().getApplicationContext());

        uploadsList.setHasFixedSize(true);
        uploadsList.setLayoutManager(llm);

        uploadsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        address2 = (LinearLayout)view.findViewById(R.id.address2Layout);
        stateAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.states, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner = (Spinner)view.findViewById(R.id.stateSpinner);
        stateSpinner.setAdapter(stateAdapter);

        if(getArguments() != null){
            args = getArguments();
        } else {
            args = new Bundle();
        }
    }

    /**
     * sets the available fields and formats the cost field to a number
     */
    private void setFields(){
        String damages = currentPolicy.getNumber("Cost").toString();
        BigDecimal parsed = new BigDecimal(damages).setScale(2, BigDecimal.ROUND_FLOOR);
        String formattedDamages = NumberFormat.getCurrencyInstance().format(parsed);

        policyCost.setText(formattedDamages);
        policyDescription.setText(currentPolicy.getString("Description"));
        policyAddress.setText(currentPolicy.getString("Address"));
        city.setText(currentPolicy.getString("City"));
        zip.setText(String.valueOf(currentPolicy.getNumber("Zip")));
        stateSpinner.setSelection(stateAdapter.getPosition(currentPolicy.getString("State")));
    }

    /**
     * if uploads exist reset a new adapter for the uploadsList otherwise make it invisible
     */
    private void setUploadsListAdapterAndComments(){
        queryParseForUploads();

        if(uploads.size() > 0) {
            uploadsList.setVisibility(View.VISIBLE);
            setComments();
            imageAdapter = new ImageRVAdapter(uploads);
            uploadsList.setAdapter(imageAdapter);
            hasImages = true;
        } else {
            uploadsList.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * loads the comments list from the uploads, if no comment is there set the entry to ""
     */
    private void setComments(){
        commentsList = new ArrayList<>();

        for(ParseObject upload : uploads){
            if(upload.getString("Comment") != null) {
                commentsList.add(upload.getString("Comment"));
            } else {
                commentsList.add("");
            }
        }
    }

    /**
     * queries parse for uploads whose policyID matches the currentPolicy Object ID
     */
    private void queryParseForUploads(){
        ParseQuery imageQuery = new ParseQuery("Upload");
        imageQuery.whereEqualTo("PolicyID", currentPolicy.getObjectId());
//todo find in background
        try{
            uploads = (ArrayList<ParseObject>)imageQuery.find();
        } catch (com.parse.ParseException e) {
            Log.e("Upload Error", e.toString());
        }
    }

    /**
     * sets the orientation of the address2 layout to differentiate between portrait and landscape
     */
    private void checkOrientationSetLayoutOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            address2.setOrientation(LinearLayout.VERTICAL);
            stateSpinner.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            city.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            zip.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }

    /**
     * validates user input
     * @return validated whether the input is valid or not
     */
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

    /**
     * saves the policy information after removing formatting from the policy cost.
     */
    private void savePolicyInformation(){
        String policyCost = this.policyCost.getText().toString();
        policyCost = policyCost.replace("$","");
        policyCost = policyCost.replace(",","");

        if(args.getBoolean("ISNEW", false) && !policyWasCreated){
            currentPolicy = new ParseObject("Policy");
            currentPolicy.put("AgentID", ParseUser.getCurrentUser().getString("AgentID"));
            currentPolicy.put("ClientID", ParseUser.getCurrentUser().getObjectId());
            currentPolicy.put("Accepted", true);
        }

        currentPolicy.put("Description", policyDescription.getText().toString());
        currentPolicy.put("Cost", Double.valueOf(policyCost));
        currentPolicy.put("Address", policyAddress.getText().toString());
        currentPolicy.put("City", city.getText().toString());
        currentPolicy.put("Zip", Double.valueOf(zip.getText().toString()));
        currentPolicy.put("State", stateSpinner.getSelectedItem().toString());


        currentPolicy.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //TODO A;SF;AF

                    Toast.makeText(getActivity(), "Policy Saved", Toast.LENGTH_SHORT).show();
                    policyWasCreated = true;
                    Singleton.setCurrentPolicy(currentPolicy);

                    uploadsLayout.setVisibility(View.VISIBLE);
                } else {
                    e.printStackTrace();
                }
            }
        });




    }

    /**
     * Saves all images associated with this Policy. Re-attaches adapter to the Recycler view
     */
    private void saveImages(){
        for(int i = 0; images.size() > i; i++) {
            try {
                Bitmap bitmap = BitmapFactory
                        .decodeStream(getActivity()
                                .getContentResolver()
                                .openInputStream(images.get(i)));

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

        setUploadsListAdapterAndComments();
    }

    /**
     * saves all image comments to parse
     */
    private void saveImageComments(){
        for(int i = 0; uploads.size() > i; i++){
            uploads.get(i).put("Comment", commentsList.get(i));
        }

        ParseObject.saveAllInBackground(uploads);
    }

    /**
     * On activity result. Handles the Image data upon return to the activity
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case NEW_IMAGE:

                    ClipData clipData = data.getClipData();
                    Uri targetUri;
                    images = new ArrayList<>();

                    if(clipData != null) {
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

                case REPLACE_IMAGE:
                    images = new ArrayList<>();
                    //get target Uri
                    final Uri singleUri = data.getData();

                    uploads.get(Singleton.getTempLocation()).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                images.add(singleUri);
                                saveImages();
                            }
                        }
                    });
                    break;

                default:
                    Log.d("Error: ", "Reached default in upload");
            }
        }
    }

    /**
     * On create options menu. enables the save icon
     *
     * @param menu the menu
     * @param inflater the inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optional_action).setVisible(true);
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(R.id.optional_action).setTitle("Save");
        menu.findItem(R.id.optional_action).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * On options item selected. validates and saves the policy and images
     *
     * @param item the item
     * @return the boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //save action button enabled
            case R.id.optional_action:
                if(validateFields()) {
                    savePolicyInformation();
                }

                if (hasImages) {
                    saveImageComments();
                    Toast.makeText(getActivity(), "Comments Saved", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Custom RecyclerView adapter
     */
    public class ImageRVAdapter extends RecyclerView.Adapter<ImageRVAdapter.ViewHolder>{

        List<ParseObject> objectsToDisplay;

        ImageRVAdapter(List<ParseObject> objectsToDisplay){
            this.objectsToDisplay = objectsToDisplay;
        }

        /**
         * gets the number of items that the recyclerview is displaying
         * @return the number of items to display
         */
        @Override
        public int getItemCount() {
            if(objectsToDisplay != null){
                return objectsToDisplay.size();
            } else {
                return 0;
            }
        }

        /**
         * Creates a holder for the veiw and stores it in memory
         * @param viewGroup the viewgroup
         * @param i the position
         * @return the viewholder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_upload_card,
                    viewGroup, false);
            final ViewHolder vHolder = new ViewHolder(v);
            vHolder.index = i;

            //Adds a text change listener to save the comments in the background
            vHolder.comments.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    commentsList.set(vHolder.index, s.toString());
                }
            });

            return vHolder;
        }

        /**
         * called by the recyclerview when binding the viewholder to the view in the recycler
         * @param vHolder the current view holder
         * @param i position in the recyclerview
         */
        @Override
        public void onBindViewHolder(final ViewHolder vHolder, int i){
            final ParseObject currentObject = objectsToDisplay.get(i);

            vHolder.index = i;

            //populates the comments and sets editable/non-editable
            if(vHolder.comments != null){
                if(!args.getBoolean("ISEDIT", false) && !args.getBoolean("ISNEW", false)){
                    vHolder.comments.setFocusable(false);
                    vHolder.comments.setClickable(false);
                    vHolder.comments.setHint("");
                }

                vHolder.comments.setText(commentsList.get(vHolder.index));
            }

            //populates the policy image if available
            if(vHolder.policyImage != null){
                Picasso.with(getActivity())
                        .load(currentObject.getParseFile("Media").getUrl())
                        .resize(500, 500)
                        .centerInside()
                        .into(vHolder.policyImage);

                //check if the policyImage should be editable/changeable
                if(args.getBoolean("ISEDIT", false) || args.getBoolean("ISNEW", false)) {
                    vHolder.policyImage.setFocusableInTouchMode(true);
                    vHolder.policyImage.setFocusable(true);
                    vHolder.policyImage.setLongClickable(true);

                    //sets a long click listener to change the image
                    vHolder.policyImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Singleton.setTempLocation(vHolder.index);

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                                    REPLACE_IMAGE);

                            return false;
                        }
                    });
                }
            }

            //sets the visibility of the trash image and sets the click listener to delete
            if(vHolder.trash != null){
                if(args.getBoolean("ISEDIT", false) || args.getBoolean("ISNEW", false)) {
                    vHolder.trash.setVisibility(View.VISIBLE);
                    vHolder.trash.setClickable(true);

                    vHolder.trash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                uploads.get(vHolder.index).delete();
                                commentsList.remove(vHolder.index);
                                Toast.makeText(getActivity(), "Image removed",
                                        Toast.LENGTH_SHORT).show();
                                setUploadsListAdapterAndComments();
                            } catch (com.parse.ParseException e) {
                                Log.d("Save Error", e.toString());
                            }
                        }
                    });
                }
            }
        }

        /**
         * an inner class that defines the recyclerView viewholder class
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            CardView cv;

            ImageButton policyImage;
            MultiAutoCompleteTextView comments;
            ImageButton trash;
            int index;

            ViewHolder(View view) {
                super(view);

                cv = (CardView) view.findViewById(R.id.claims_card_view);
                policyImage = (ImageButton)view.findViewById(R.id.policyImage);
                comments = (MultiAutoCompleteTextView)view.findViewById(R.id.imageDescription);
                trash = (ImageButton)view.findViewById(R.id.deleteImageButton);
            }
        }
    }
}