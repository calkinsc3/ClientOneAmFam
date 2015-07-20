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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClaimScreenFragment extends Fragment {

    private final int NEW_IMAGE = 0;
    private Bundle args;
    private TextView policy;
    private EditText damages;
    private EditText comment;
    private RecyclerView claimsView;
    private ImageButton addImage;
    private Spinner policySpinner;
    private ParseObject currentClaim;
    private ArrayList<ParseObject> uploads;
    private ArrayList<String> commentsList;
    private ArrayList<ParseObject> policies;
    private ArrayList<String> policyIDs;
    private ArrayList<Uri> images;
    private boolean hasImages;

    public ClaimScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_claim_screen, container, false);

        initializeVariables(view);
        enableEditingifNew();

        return view;
    }

    private void initializeVariables(View view){
        policy = (TextView)view.findViewById(R.id.policyNumber);
        damages = (EditText)view.findViewById(R.id.claimDamages);
        comment = (EditText)view.findViewById(R.id.claimComment);
        claimsView = (RecyclerView)view.findViewById(R.id.claimUploadsView);
        addImage = (ImageButton)view.findViewById(R.id.addUploadButton);
        policySpinner = (Spinner)view.findViewById(R.id.policySpinner);
        uploads = new ArrayList<>();
        policyIDs = new ArrayList<>();
        hasImages = false;
        if(getArguments() != null){
            args = getArguments();
        } else {
            args = new Bundle();
        }
    }

    private void enableEditingifNew(){
        if(args.getBoolean("ISNEW", false)){
            makeFieldsEditable();
            setHasOptionsMenu(true);
            getAndSetParseIDs();
        }  else {
            currentClaim = Singleton.getCurrentClaim();
            claimWasSelectedPopulateViews();
        }
    }

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
        policySpinner.setVisibility(View.VISIBLE);
        policySpinner.setClickable(true);
        policySpinner.setFocusable(true);
        policySpinner.setFocusableInTouchMode(true);

        setAddUploadClickListener();
        loadPolicySpinner();
    }

    private void loadPolicySpinner(){

        policySpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, policyIDs));
    }

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
                startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), 0);
            }
        });
    }

    private void setFields(){
        policy.append(" " + currentClaim.getString("PolicyID"));
        damages.setText(String.valueOf(currentClaim.getDouble("Damages")));
        comment.setText(currentClaim.getString("Comment"));
    }

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

    private void getPolicyIDs(){
        for(int i = 0; policies.size() > i; i++){
            policyIDs.add(policies.get(i).getObjectId());
        }
    }

    private void queryParseForUploads(){
        uploads = new ArrayList<>();
        ArrayList uploadIDs = (ArrayList)currentClaim.getList("UploadIDs");
        for(int i = 0; uploadIDs.size() > i; i++) {
            ParseQuery imageQuery = new ParseQuery("Upload");
            imageQuery.whereEqualTo("objectId", uploadIDs.get(i).toString());
            try {
                uploads.add((ParseObject)imageQuery.find().get(0));
            } catch (com.parse.ParseException e) {
                Log.e("Upload Error", e.toString());
            }
        }
    }

    private void setUploadListAdapterAndComments(){
        /* Set up the recycler view */
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        claimsView.setHasFixedSize(true);
        claimsView.setLayoutManager(llm);

        queryParseForUploads();

        if(uploads.size() > 0) {
            setComments();
            ImageRVAdapter adapter = new ImageRVAdapter(uploads);
            claimsView.setAdapter(adapter);
            hasImages = true;
        }
    }

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

    private void claimWasSelectedPopulateViews(){
        //todo populate views
        setFields();
        setUploadListAdapterAndComments();
    }

    private boolean validateFields(){
        String error = getResources().getString(R.string.entryError);
        boolean validated = false;

        if(damages.getText().toString().equals("")){
            damages.setError(error);
        } else if(comment.getText().toString().equals("")){
            comment.setError(error);
        } else {
            validated = true;
        }

        return validated;
    }

    private void saveClaimInformation(){

        currentClaim = new ParseObject("Claim");
        currentClaim.put("Damages", Double.valueOf(damages.getText().toString()));
        currentClaim.put("Comment", comment.getText().toString());
        currentClaim.put("PolicyID", policySpinner.getSelectedItem().toString());
        //todo create list of upload ids
        //currentClaim.put("UploadIDs", )

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
        setUploadListAdapterAndComments();
    }

    private void saveImageComments(){
        //todo save image comments
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
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(R.id.optional_action).setTitle("Save");
        menu.findItem(R.id.optional_action).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
     * On activity result.
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

    public class ImageRVAdapter extends RecyclerView.Adapter<ImageRVAdapter.ViewHolder>{

        List<ParseObject> objectsToDisplay;

        ImageRVAdapter(List<ParseObject> objectsToDisplay){
            this.objectsToDisplay = objectsToDisplay;
        }

        @Override
        public int getItemCount() {
            if(objectsToDisplay != null){
                return objectsToDisplay.size();
            } else {
                return 0;
            }
        }

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

        @Override
        public void onBindViewHolder(final ViewHolder vHolder, int i){
            final ParseObject currentObject = objectsToDisplay.get(i);

            if(vHolder.comments != null) {
                vHolder.comments.setFocusable(false);
                vHolder.comments.setClickable(false);
                vHolder.comments.setText(commentsList.get(i));
            }
            if(vHolder.claimImage != null) {
                Picasso.with(getActivity()).load(currentObject.getParseFile("Media").getUrl())
                        .fit().centerInside().into(vHolder.claimImage);
            }
            if(args.getBoolean("ISNEW", false)) {
                vHolder.trash.setVisibility(View.VISIBLE);
                vHolder.trash.setClickable(true);
                vHolder.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo make delete picture method
                        try {
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            CardView cv;

            ImageButton claimImage;
            MultiAutoCompleteTextView comments;
            ImageButton trash;
            int index;

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
