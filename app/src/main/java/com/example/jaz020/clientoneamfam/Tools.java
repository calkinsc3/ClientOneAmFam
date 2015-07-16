package com.example.jaz020.clientoneamfam;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

//import com.parse.ParseObject;
//import com.parse.ParseUser;


/**
 * Created by jaz020 on 6/25/2015.
 */
public class Tools {

    public static void replaceFragment(int container_id, Fragment fragment, FragmentManager fManager, boolean addToBackStack) {

        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(container_id, fragment);
        if (addToBackStack) fTransaction.addToBackStack(null);

        fTransaction.commit();

        //refresh options menu
    }

//    public static void logout(Context context) {
//
//        SharedPreferences.Editor editor = context.getSharedPreferences(Singleton.PREFERENCES, 0).edit();
//        editor.remove("OfficeUserID");
//        editor.remove("OfficeStayLoggedIn");
//        editor.apply();
//
//        ParseUser.logOut();
//        ((Activity) context).finish();
//
//    }
//
//    public static void updateDateEntry(EditText editText, Calendar calendar) {
//
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        editText.setText(sdf.format(calendar.getTime()));
//    }
//
//    public static void updateTimeEntry(EditText editText, Calendar calendar) {
//
//        String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
//        String am_pm;
//
//        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
//            am_pm = "am";
//        } else {
//            am_pm = "pm";
//        }
//
//        if (minutes.length() < 2) {
//            minutes += "0";
//        }
//
//        editText.setText(calendar.get(Calendar.HOUR) + ":" + minutes + " " + am_pm);
//    }
//
//    public static byte[] readBytes(Context context, Uri uri) throws IOException {
//        // this dynamically extends to take the bytes you read
//        InputStream inputStream = context.getContentResolver().openInputStream(uri);
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//
//        // this is storage overwritten on each iteration with bytes
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        // we need to know how may bytes were read to write them to the byteBuffer
//        int len = 0;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//
//        // and then we can return your byte array.
//        return byteBuffer.toByteArray();
//    }
//
//    //TODO recomment
//    /**
//     * Formats the information of a client or a policy object to be
//     * viewed as a list item in a TextView
//     *
//     * @param object The object that is to be formatted(Client or Policy)
//     * @param mode   ClIENT or POLICY
//     * @return
//     */
//    public static String buildMessage(ParseObject object, int mode) {
//
//        String message = "";
//
//        switch (mode) {
//
//            case Singleton.CLIENT:
//                message = object.getString("FirstName");
//                message += " " + object.getString("LastName") + "\n";
//                message += object.getString("Address") + "\n";
//                message += object.getString("City") + ", " + object.getString("State") + " " + object.getNumber("ZIP");
//                break;
//
//            case Singleton.POLICY:
//                message = object.getString("Line1") + "\n" + object.getString("Line2") + "\n" + object.getNumber("PolicyNumber");
//                break;
//
//            case Singleton.CLAIM:
//                String damages = String.valueOf(object.get("Damages"));
//                BigDecimal parsed = new BigDecimal(damages).setScale(2,BigDecimal.ROUND_FLOOR);
//                String formattedDamages = NumberFormat.getCurrencyInstance().format(parsed);
//
//                message = object.getObjectId() + "\n" + formattedDamages;
//                break;
//
//            case Singleton.MEETING:
//                message = object.getString("Title") + "\n" + object.getString("Location") + "\n" +
//                        object.getDate("StartDate").toString() +  "\n" +
//                        object.getDate("EndDate").toString() + "\n" +
//                        object.getString("Comment");
//                break;
//
//        }
//
//        return message;
//    }
}