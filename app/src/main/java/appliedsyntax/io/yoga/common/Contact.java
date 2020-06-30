package appliedsyntax.io.yoga.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

import appliedsyntax.io.yoga.acivity.ActivityFeeHistory;


public class Contact {


    private static final int REQUEST_PHONE_SMS=1456;
    private static final int REQUEST_PHONE_CALL = 123;
    public static boolean SMS(String phoneNum ,Activity context)
    {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.SEND_SMS},REQUEST_PHONE_SMS);
            }
            else
            {
                sendLongSMS(phoneNum,context);
                return true;
            }
        }
        else
        {
            sendLongSMS(phoneNum,context);

            return true;
        }
        return false;
    }

    public static void sendLongSMS(String phoneNumber,Context context) {
        String message = "Hello My dear Student! " +
                "It is the time to pay fee for YOGA class.";
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
//        Toast.makeText(context, "SMS was sent", Toast.LENGTH_SHORT).show();

    }
    public static void makeCall(String phoneNumber,Activity context)
    {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+ phoneNumber));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else
            {
                context.startActivity(intent);
            }
        }
        else
        {
            context.startActivity(intent);
        }
    }
   public static void openWhatsApp(String smsNumber, Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone="+("91" +smsNumber) +"&text=" + URLEncoder.encode("Hello test message from Yoga Centre", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                activity.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
