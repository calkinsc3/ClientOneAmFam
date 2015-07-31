package com.example.jaz020.clientoneamfam;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


/**
 * Edits an appointment and adds the appointment to google calender.
 */
public class EditAppointment extends Fragment {

    /**
     * Public fields required for Google Calendar
     */
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    static List<Integer> mSelectedUsers;
    static String[] attendeesList;
    static boolean[] checkedUserIDs;

    AlertDialog.Builder builder;

    private EditText attendees_entry;
    EditText meeting_entry;
    EditText location_entry;
    EditText start_time_entry;
    EditText start_date_entry;
    EditText end_time_entry;
    EditText end_date_entry;
    EditText comments_entry;

    //the current users calendarInfo
    String[] calendarInfo;
    Calendar startDateCalendar;
    Calendar endDateCalendar;

    // Flag to prevent the alert dialog builder from showing twice on a double click.
    private int alertdialogFlag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        meeting_entry = (EditText) view.findViewById(R.id.meeting_entry);
        location_entry = (EditText) view.findViewById(R.id.location_entry);
        start_time_entry = (EditText) view.findViewById(R.id.start_time_entry);
        start_date_entry = (EditText) view.findViewById(R.id.start_date_entry);
        end_time_entry = (EditText) view.findViewById(R.id.end_time_entry);
        end_date_entry = (EditText) view.findViewById(R.id.end_date_entry);
        comments_entry = (EditText) view.findViewById(R.id.comments_entry);
        attendees_entry = (EditText) view.findViewById(R.id.attendees_entry);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        calendarInfo = getCalendar();

        mSelectedUsers = new ArrayList<>();
        checkedUserIDs = new boolean[0];
        builder = new AlertDialog.Builder(getActivity());

        alertdialogFlag = 0;

        loadUserInfo();

        editInvitees();

        setListeners();
    }

    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;

    /**
     * Sets all the Listeners for edittexts and datePickers
     */
    public void setListeners(){
        startDatePicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateCalendar.set(Calendar.YEAR, year);
                        startDateCalendar.set(Calendar.MONTH, monthOfYear);
                        startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Tools.updateDateEntry(start_date_entry, startDateCalendar);
                    }
                },
                startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePicker =  new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateCalendar.set(Calendar.YEAR, year);
                        endDateCalendar.set(Calendar.MONTH, monthOfYear);
                        endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Tools.updateDateEntry(end_date_entry, endDateCalendar);
                    }
                },
                endDateCalendar.get(Calendar.YEAR),
                endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH));

        startTimePicker =    new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startDateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        startDateCalendar.set(Calendar.MINUTE, selectedMinute);
                        Tools.updateTimeEntry(start_time_entry, startDateCalendar);
                    }
                },
                startDateCalendar.get(Calendar.HOUR_OF_DAY),
                startDateCalendar.get(Calendar.MINUTE), false);

        endTimePicker = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endDateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        endDateCalendar.set(Calendar.MINUTE, selectedMinute);
                        Tools.updateTimeEntry(end_time_entry, endDateCalendar);
                    }
                },
                endDateCalendar.get(Calendar.HOUR_OF_DAY),
                endDateCalendar.get(Calendar.MINUTE), false);

        start_date_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startDatePicker.isShowing()){
                    startDatePicker.show();
                }
            }
        });

        end_date_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!endDatePicker.isShowing()){
                    endDatePicker.show();
                }
            }
        });

        start_time_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startTimePicker.isShowing()) {
                    startTimePicker.show();
                }
            }
        });

        end_time_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!endTimePicker.isShowing()) {
                    endTimePicker.show();
                }
            }
        });
    }

    private void loadUserInfo(){
        //load current date into datePickers
        startDateCalendar.setTime(Calendar.getInstance().getTime());
        endDateCalendar.setTime(Calendar.getInstance().getTime());

        Tools.updateTimeEntry(start_time_entry, startDateCalendar);
        Tools.updateTimeEntry(end_time_entry, endDateCalendar);
        Tools.updateDateEntry(start_date_entry, startDateCalendar);
        Tools.updateDateEntry(end_date_entry, endDateCalendar);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("accountType", "Office");

        try {
            List<ParseUser> possibleAttendees = query.find();

            attendeesList = new String[possibleAttendees.size()];
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            ParseUser currUser = ParseUser.getCurrentUser();
            String currUserName = currUser.getString("Name");

            ParseQuery<ParseUser> agentQuery = ParseUser.getQuery();
            ParseUser agent = agentQuery.get(currUser.getString("AgentID"));
            String agentName = agent.getString("Name");

            if (currUserName == null || currUserName.equals("")) {
                currUserName = currUser.getString("username");
            }

            if (agentName == null || agentName.equals("")) {
                agentName = agent.getString("username");
            }

            attendees_entry.setText(currUserName + ", " + agentName);

            attendeesList[0] = currUser.getObjectId();
            attendeesList[1] = agent.getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current appointment (new or old) and uploads it to the user's Google Calendar
     */
    private void saveAppointment(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "", true);
        ParseObject appointmentToSave = new ParseObject("Meeting");

        //TODO entry validations
        // Save input from user to parse database.
        String title = meeting_entry.getText().toString();
        JSONArray invitedIDS = new JSONArray((Arrays.asList(attendeesList)));
        String location = location_entry.getText().toString();
        Date startDate = startDateCalendar.getTime();
        Date endDate = endDateCalendar.getTime();
        String comments = comments_entry.getText().toString();

        appointmentToSave.put("Title", title);
        appointmentToSave.put("InvitedIDs", invitedIDS);
        appointmentToSave.put("Location", location);
        appointmentToSave.put("StartDate", startDate);
        appointmentToSave.put("EndDate", endDate);
        appointmentToSave.put("Comment", comments);

        // TODO ??
        appointmentToSave.put("Accepted", true);

        appointmentToSave.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();

                if (e == null) {
                    Toast.makeText(getActivity(), "Appointment Saved to Parse.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), "Appointment NOT Saved to Parse.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveToGoogle(){
        Date startDate = startDateCalendar.getTime();
        Date endDate = endDateCalendar.getTime();
        String comments = comments_entry.getText().toString();
        String title = meeting_entry.getText().toString();

        /**
         * SAVE TO GOOGLE CALENDAR
         */
        long calID = Long.parseLong(calendarInfo[PROJECTION_ID_INDEX]);

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startDate.getTime());
        values.put(CalendarContract.Events.DTEND, endDate.getTime());
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, comments);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        if(eventID != -1) {
            Toast.makeText(getActivity(), "Appointment saved to google Calendar",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Appointment not saved to google Calendar",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the User's google calendar from google.
     *
     * @return an array of information regarding the user's calendar
     *         it is put into the array in the format outlined by the google calendar fields
     */
    private String[] getCalendar(){
        // Run query
        Cursor cur;
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String user_email = ParseUser.getCurrentUser().getEmail();

        //TODO user email set here + check valid result
        String[] selectionArgs = new String[] {user_email , "com.google", user_email};

        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        long calID = 0;
        String displayName = null;
        String accountName = null;
        String ownerName = null;

        while (cur.moveToNext()) {
            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
        }

        return new String[] {String.valueOf(calID), displayName, accountName, ownerName};
    }

    /**
     * Edit invitees editText on click listener.
     */
    private void editInvitees() {
        attendees_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertdialogFlag == 0) {
                    getPossibleAttendees();
                    alertdialogFlag = 1;
                }
            }
        });
    }

    /**
     * Gets ids of all the possible users that can be invited (Agents and Clients).
     */
    private void getPossibleAttendees() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("accountType", "Office");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> possibleAttendees, ParseException e) {
                if (e == null && possibleAttendees.size() != 0) {
                    String[] attendeeNames = new String[possibleAttendees.size()];
                    checkedUserIDs = new boolean[possibleAttendees.size()];

                    for (int i = 0; i < possibleAttendees.size(); i++) {
                        // Add names of all possible attendees to a concurrent string array.
                        String objID = possibleAttendees.get(i).getObjectId();
                        String attendeeName = possibleAttendees.get(i).getString("Name");

                        if (attendeeName != null && !attendeeName.equals(""))
                            attendeeNames[i] = attendeeName;
                        else
                            attendeeNames[i] = possibleAttendees.get(i).getString("username");

                        // Checks if a user is already added to the list of invitees and adds them
                        // to a list to have them already checked when alert dialog pops up.
                        if (attendeesList != null && Arrays.asList(attendeesList).contains(objID)) {
                            mSelectedUsers.add(i);
                            checkedUserIDs[i] = true;
                        }
                    }

                    showPopupDialog(possibleAttendees, attendeeNames);

                } else if (possibleAttendees.size() == 0) {
                    builder.setTitle("Select Attendees");
                    builder.setMessage("No users found");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {}
                    });
                } else {
                    //Something went wrong.
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
    }

    /**
     * Shows the alert dialog for all the possible attendees.
     *
     * @param possibleAttendees List of all the users that can be invited to attend the meeting.
     * @param userIDs Object ids of all the users that can be invited to attend the meeting.
     */
    private void showPopupDialog(final List<ParseUser> possibleAttendees, String[] userIDs) {
        builder.setTitle("Select Attendees");

        // Alert dialog with multi-select.
        builder.setMultiChoiceItems(userIDs, checkedUserIDs,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                        if (isChecked && !mSelectedUsers.contains(item)) {
                            // A new user is invited.
                            mSelectedUsers.add(item);
                            checkedUserIDs[item] = true;

                        } else if (mSelectedUsers.contains(item)) {

                            if (mSelectedUsers.size() == 1) {
                                // All the possible invitees are unselected.
                                mSelectedUsers = new ArrayList<>();

                            } else {
                                // A previous invitee was unselected.
                                mSelectedUsers.remove(Integer.valueOf(item));
                                checkedUserIDs[item] = false;
                            }
                        }

                        // Removes the selection of multiple users from the list to display in the
                        // attendees_entry edit text.
                        mSelectedUsers = removeDuplicates(mSelectedUsers);
                    }
                });

        builder.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                attendeesList = new String[mSelectedUsers.size()];
                String attendees = "";

                // Build the string to display the selected invitees in the
                // attendees_entry edit text.
                for (int i = 0; i < mSelectedUsers.size(); i++) {
                    String objID = possibleAttendees.get(mSelectedUsers.get(i)).getObjectId();
                    String attendeeName = possibleAttendees.get(mSelectedUsers.get(i)).getString("Name");
                    attendeesList[i] = objID;

                    if (attendeeName == null  || attendeeName.equals("")) {
                        attendeeName = possibleAttendees.get(mSelectedUsers.get(i)).getString("username");
                    }

                    if (i == 0)
                        attendees += attendeeName;
                    else
                        attendees += (", " + attendeeName);
                }

                attendees_entry.setText(attendees);
                mSelectedUsers = new ArrayList<>();
                alertdialogFlag = 0;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mSelectedUsers = new ArrayList<>();
                alertdialogFlag = 0;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Removes all the duplicate items from a list.
     *
     * @param oldList List from which to remove the duplicate items from.
     * @return List one occurrence of each item.
     */
    private List<Integer> removeDuplicates(List<Integer> oldList) {
        Set<Integer> newList = new HashSet<>();
        newList.addAll(oldList);

        return new ArrayList<>(newList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optional_action).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(R.id.optional_action).setVisible(true);
        menu.findItem(R.id.optional_action).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optional_action:
                saveAppointment();
                saveToGoogle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}