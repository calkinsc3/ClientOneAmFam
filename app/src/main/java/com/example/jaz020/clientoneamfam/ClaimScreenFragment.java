package com.example.jaz020.clientoneamfam;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The claim scree fragment holds the information pertaining to each Claim if a claim was chosen. If
 * the AddClaim button was pressed it will enable the user to create a new claim from a list of possible
 * policies and allow the user to upload photos.
 *
 * @author lsl017
 */
public class ClaimScreenFragment extends Fragment {

    private final int NEW_IMAGE = 0;
    private final int REPLACE_IMAGE = 1;

    LinearLayoutManager llm;

    private Bundle args;

    private TextView policy;

    private EditText damages;
    private EditText comment;

    private LinearLayout uploadsLayout;

    private RecyclerView claimsView;

    private ImageButton addImage;

    private Spinner policySpinner;

    private ParseObject currentClaim;

    private ArrayList<ParseObject> uploads;
    private ArrayList<String> commentsList;
    private ArrayList<ParseObject> policies;
    private ArrayList<String> policyIDs;
    private ArrayList<String> uploadIDs;
    private ArrayList<Uri> images;

    private boolean hasImages;

    /**
     * Instantiates a new Claim screen fragment.
     */
    public ClaimScreenFragment() {
        // Required empty public constructor
    }

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
        View view = inflater.inflate(R.layout.fragment_claim_screen, container, false);

        initializeVariables(view);
        enableEditingifNew();

        return view;
    }

    /**
     * Intializes all global variables and sets the size of the Recycler view.
     * @param view the view connected to the fragment
     */
    private void initializeVariables(View view){
        policy = (TextView)view.findViewById(R.id.policyNumber);

        damages = (EditText)view.findViewById(R.id.claimDamages);
        comment = (EditText)view.findViewById(R.id.claimComment);

        claimsView = (RecyclerView)view.findViewById(R.id.claimUploadsView);

        addImage = (ImageButton)view.findViewById(R.id.addUploadButton);

        policySpinner = (Spinner)view.findViewById(R.id.policySpinner);

        uploadsLayout = (LinearLayout)view.findViewById(R.id.uploadsLayout);

        llm = new LinearLayoutManager(getActivity().getApplicationContext());

        claimsView.setHasFixedSize(true);
        claimsView.setLayoutManager(llm);

        uploads = new ArrayList<>();
        policyIDs = new ArrayList<>();
        uploadIDs = new ArrayList<>();

        hasImages = false;

        if(getArguments() != null){
            args = getArguments();
        } else {
            args = new Bundle();
        }
    }

    /**
     * Enables editing, and gets ParseIDs if it is a new Claim. If a claim was selected it populates
     * the views with data and hides the policy spinner.
     */
    private void enableEditingifNew(){
        if(args.getBoolean("ISNEW", false)){
            getAndSetParseIDs();
            makeFieldsEditable();
            setHasOptionsMenu(true);
            uploadsLayout.setVisibility(View.GONE);
        }  else {
            currentClaim = Singleton.getCurrentClaim();
            claimWasSelectedPopulateViews();
            hideSpinner();
        }
    }

    /**
     * Hides the spinner and makes it un-clickable
     */
    private void hideSpinner(){
        policySpinner.setVisibility(View.GONE);
        policySpinner.setClickable(false);
        policySpinner.setFocusable(false);
        policySpinner.setFocusableInTouchMode(false);
    }

    /**
     * makes the fields editable and loads the policySpinner. Also sets the textWatcher and
     * Add upload listener
     */
    private void makeFieldsEditable() {
        damages.setClickable(true);
        damages.setFocusable(true);
        damages.setFocusableInTouchMode(true);

        comment.setClickable(true);
        comment.setFocusable(true);
        comment.setFocusableInTouchMode(true);

        addImage.setVisibility(View.VISIBLE);
        addImage.setClickable(true);
        addImage.setFocusable(true);
        addImage.setFocusableInTouchMode(true);

        setAddUploadClickListener();
        setDamagesChangeListener();
        loadPolicySpinner();
    }

    /**
     * loads the policy spinner
     */
    private void loadPolicySpinner(){
        String[] policyIDs = this.policyIDs.toArray(new String[this.policyIDs.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, policyIDs);

        policySpinner.setAdapter(adapter);
    }

    /**
     * if the user has saved the Claim it sets the on click listener for the addImage button,
     * enables uploading of images;
     */
    private void setAddUploadClickListener(){
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT FOR MOVING TO GALLERY
                Intent intent = new Intent()
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                //WILL START A CHOOSER ACTIVITY WITH GALLERY AND OTHER OPTIONS IN IT
                startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), NEW_IMAGE);
            }
        });
    }

    /**
     * sets the listener for text changed on damages. Formats the number on text changed
     */
    private void setDamagesChangeListener(){
        damages.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    damages.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    BigDecimal parsed = new BigDecimal(cleanString)
                            .setScale(2, BigDecimal.ROUND_FLOOR)
                            .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);

                    String formatted = NumberFormat.getCurrencyInstance().format(parsed);

                    current = formatted;
                    damages.setText(formatted);
                    damages.setSelection(formatted.length());

                    damages.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * sets the values of all edit texts. Formats the damages text to a number
     */
    private void setFields(){
        String damages = currentClaim.getNumber("Damages").toString();
        BigDecimal parsed = new BigDecimal(damages).setScale(2, BigDecimal.ROUND_FLOOR);
        String formattedDamages = NumberFormat.getCurrencyInstance().format(parsed);

        this.damages.setText(formattedDamages);
        policy.append(" " + currentClaim.getString("PolicyID"));
        comment.setText(currentClaim.getString("Comment"));
    }

    /**
     * gets parse ID's where the clientID equals the current user ID
     */
    private void getAndSetParseIDs(){
        ParseQuery policyQuery = new ParseQuery("Policy");
        policyQuery.whereEqualTo("ClientID", ParseUser.getCurrentUser().getObjectId());

        try{
            policies = (ArrayList<ParseObject>)policyQuery.find();
        } catch (com.parse.ParseException e){
            Log.e("Policy errror", e.toString());
        }

        getPolicyIDs();
    }

    /**
     * adds the Object ID of each policy connected to the user to an array list of ID's
     */
    private void getPolicyIDs(){
        for(int i = 0; policies.size() > i; i++){
            policyIDs.add(policies.get(i).getObjectId());
        }
    }

    /**
     * queries Parse.com for uploads connected to the uploadIDs
     */
    private void queryParseForUploads(){
        uploads = new ArrayList<>();

        ParseQuery imageQuery = new ParseQuery("Upload");
        imageQuery.whereEqualTo("ClaimID", currentClaim.getObjectId());
        try {
            uploads.addAll(imageQuery.find());
        } catch (ParseException e){
            Log.d("Upload Query Error", e.toString());
        }
    }

    /**
     * if uploads contains content re-attach new adapter. If non set the RecyclerView to invisible
     */
    private void setUploadListAdapterAndComments(){

        queryParseForUploads();

        if(uploads.size() > 0) {
            setComments();
            ImageRVAdapter adapter = new ImageRVAdapter(uploads);
            claimsView.setAdapter(adapter);
            claimsView.setVisibility(View.VISIBLE);
            hasImages = true;
        } else {
            claimsView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * loads the commentsList with the comments for each photo
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
     * sets the fields and sets the upload List adapter
     */
    private void claimWasSelectedPopulateViews(){
        setFields();
        setUploadListAdapterAndComments();
    }

    /**
     * Validates the data in each edit text field and returns true if valid
     *
     * @return validated if the entered text areas are valid
     */
    private boolean validateFields(){
        String error = getResources().getString(R.string.entryError);
        boolean validated = false;

        if (damages.getText().toString().equals("")){
            damages.setError(error);
        } else if (comment.getText().toString().equals("")){
            comment.setError(error);
        } else {
            validated = true;
        }

        return validated;
    }

    /**
     * saves the claim information
     */
    private void saveClaimInformation(){
        String damages = this.damages.getText().toString();
        damages = damages.replace("$","");
        damages = damages.replace(",","");

        if(currentClaim == null) {
            currentClaim = new ParseObject("Claim");
            currentClaim.put("Damages", Double.valueOf(damages));
            currentClaim.put("Comment", comment.getText().toString());
            currentClaim.put("PolicyID", policySpinner.getSelectedItem().toString());

            try {
                currentClaim.save();
            } catch (com.parse.ParseException e) {
                Log.e("Error creating claim", e.toString());
            }
        } else {
            currentClaim.put("Damages", Double.valueOf(damages));
            currentClaim.put("Comment", comment.getText().toString());
            currentClaim.put("UploadIDs", uploadIDs);
            currentClaim.saveInBackground();
        }

        uploadsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * saves the images to parse and populates the required fields. Re-sets the uploadlist adapter
     * after saving.
     */
    private void saveImages(){
        for(int i = 0; images.size() > i; i++){
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
                Toast.makeText(getActivity(), policySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                mediaUpload.put("PolicyID", policySpinner.getSelectedItem().toString());
                mediaUpload.put("ClaimID", currentClaim.getObjectId());
                mediaUpload.put("UserID", ParseUser.getCurrentUser().getObjectId());
                mediaUpload.put("Media", image);

                mediaUpload.save();
                uploadIDs.add(mediaUpload.getObjectId());

                saveClaimInformation();
            } catch(FileNotFoundException e){
                Log.e("File Not Found", e.toString());
            } catch(com.parse.ParseException e) {
                Log.e("Error Saving", e.toString());
            }
        }
        setUploadListAdapterAndComments();
    }

    /**
     * saves all of the image comments
     */
    private void saveImageComments(){
        for(int i = 0; uploads.size() > i; i++){
            uploads.get(i).put("Comment", commentsList.get(i));
            ParseObject.saveAllInBackground(uploads);
        }
    }

    /**
     * On create options menu. Enables the save icon
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
     * On options item selected. Saves all information on screen
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
                    saveClaimInformation();
                    Toast.makeText(getActivity(), "Policy Saved", Toast.LENGTH_SHORT).show();
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
     * On activity result. Handles the return of image data from the intent
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            ClipData clipData = data.getClipData();
            Uri targetUri;
            switch (requestCode) {
                case NEW_IMAGE:
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
                case REPLACE_IMAGE:
                    //get target Uri
                    final Uri singleImage = data.getData();
                    //remove previous image
                    uploads.get(Singleton.getTempLocation()).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                images.add(singleImage);

                                saveImages();
                            }
                        }
                    });

                default:
                    Log.d("Error: ", "Reached default in upload");
            }
        }
    }

    /**
     * The type Image rV adapter.
     */
    public class ImageRVAdapter extends RecyclerView.Adapter<ImageRVAdapter.ViewHolder>{

        /**
         * The Objects to display.
         */
        List<ParseObject> objectsToDisplay;

        /**
         * Instantiates a new Image rV adapter.
         *
         * @param objectsToDisplay the objects to display
         */
        ImageRVAdapter(List<ParseObject> objectsToDisplay){
            this.objectsToDisplay = objectsToDisplay;
        }

        /**
         * Gets item count.
         *
         * @return the item count
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
         * On create view holder.
         *
         * @param viewGroup the view group
         * @param i the i
         * @return the view holder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_upload_card,
                    viewGroup, false);
            final ViewHolder vHolder = new ViewHolder(v);
            vHolder.index = i;

            vHolder.comments.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    commentsList.set(vHolder.index, s.toString());
                }
            });

            return vHolder;
        }

        /**
         * On bind view holder.
         *
         * @param vHolder the v holder
         * @param i the i
         */
        @Override
        public void onBindViewHolder(final ViewHolder vHolder, int i){
            final ParseObject currentObject = objectsToDisplay.get(i);
            vHolder.index = i;

            if(vHolder.comments != null) {
                if(!args.getBoolean("ISNEW", false)) {
                    vHolder.comments.setFocusable(false);
                    vHolder.comments.setClickable(false);
                    vHolder.comments.setText(commentsList.get(i));
                }
            }
            if(vHolder.claimImage != null) {
                Picasso.with(getActivity()).load(currentObject.getParseFile("Media").getUrl())
                        .fit().centerInside().into(vHolder.claimImage);
                if(!args.getBoolean("ISNEW", false)) {
                    vHolder.claimImage.setLongClickable(true);
                    vHolder.claimImage.setFocusable(true);
                    vHolder.claimImage.setFocusableInTouchMode(true);
                    vHolder.claimImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            Singleton.setTempLocation(vHolder.index);

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REPLACE_IMAGE);
                            return false;
                        }
                    });
                }
            }
            if(args.getBoolean("ISNEW", false)) {
                vHolder.trash.setVisibility(View.VISIBLE);
                vHolder.trash.setClickable(true);
                vHolder.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo make delete picture method
                        try {
                            uploadIDs.remove(vHolder.index);
                            uploads.get(vHolder.index).delete();
                            commentsList.remove(vHolder.index);
                            Toast.makeText(getActivity(), "Image removed", Toast.LENGTH_SHORT).show();
                            setUploadListAdapterAndComments();
                        } catch (com.parse.ParseException e) {
                            Log.d("Save Error", e.toString());
                        }
                    }
                });
            }


        }

        /**
         * The type View holder.
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            /**
             * The Cv.
             */
            CardView cv;

            /**
             * The Claim image.
             */
            ImageButton claimImage;
            /**
             * The Comments.
             */
            MultiAutoCompleteTextView comments;
            /**
             * The Trash.
             */
            ImageButton trash;
            /**
             * The Index.
             */
            int index;

            /**
             * Instantiates a new View holder.
             *
             * @param view the view
             */
            ViewHolder(View view) {
                super(view);

                cv = (CardView) view.findViewById(R.id.claims_card_view);
                claimImage = (ImageButton)view.findViewById(R.id.policyImage);
                comments = (MultiAutoCompleteTextView)view.findViewById(R.id.imageDescription);
                trash = (ImageButton)view.findViewById(R.id.deleteImageButton);
            }
        }

    }
}
