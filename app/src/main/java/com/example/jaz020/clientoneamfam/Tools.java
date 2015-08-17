package com.example.jaz020.clientoneamfam;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by jaz020 on 6/25/2015.
 */
public class Tools {

    /**
     * Replace fragment.
     *
     * @param container_id   the container _ id
     * @param fragment       the fragment to inflate
     * @param fManager       the fragmentmanager
     * @param addToBackStack if fragment need to be on the backstack
     */
    public static void replaceFragment(int container_id, Fragment fragment, FragmentManager fManager,
                                       boolean addToBackStack) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
        fTransaction.replace(container_id, fragment);

        if (addToBackStack) fTransaction.addToBackStack(null);

        fTransaction.commit();
    }

    /**
     * Sets my agent.
     *
     * @throws ParseException the parse exception
     */
    public static void setMyAgent() throws ParseException {
        ParseUser client = ParseUser.getCurrentUser();
        final String agentID = client.getString("AgentID");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", agentID);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null && !list.isEmpty())
                    Singleton.setMyAgent(list.get(0));
            }
        });
    }

    /**
     * Logs out the user.
     *
     * @param context the context
     */
    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("AmFam", 0).edit();
        editor.remove("UserID");
        editor.remove("StayLoggedIn");
        editor.apply();

        ParseUser.logOut();
        ((Activity) context).finish();
    }

    /**
     * formats dates.
     *
     * @param editText the edit text
     * @param calendar the calendar
     */
    public static void updateDateEntry(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * formats time.
     *
     * @param editText the edit text
     * @param calendar the calendar
     */
    public static void updateTimeEntry(EditText editText, Calendar calendar) {
        String timeFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * checks if there is network connection.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}