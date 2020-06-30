package appliedsyntax.io.yoga.common;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class LoadDimensions {

    public static void calculate(Activity activity)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        float h = 1920/(view_value/((float) activity.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
//        return  (int)((height/h)*3);

        int dp_200 = (int)((height/9.6)*3);
        int dp_120 = (int)((height/16)*3);
        int dp_110 = (int)((height/18)*3);
        int dp_H10 = (int)((width/108)*3);
        int dp_20 = ((height/96)*3);
        int dp_10 = ((height/192)*3);
        int dp_H20 = (width/54)*3;
        int dp_30 =  (height/64)*3;
        int dp_70 = ((height/28)*3);
        int dp_H70 = (height/16)*3;
        int dp_40= (height/48)*3;

        MySharedPreference.setDp(activity,"dp_120" ,dp_120);
        MySharedPreference.setDp(activity,"dp_200" , dp_200);
        MySharedPreference.setDp(activity,"dp_H10", dp_H10);
        MySharedPreference.setDp(activity,"dp_110", dp_110);
        MySharedPreference.setDp(activity,"dp_20",dp_20);
        MySharedPreference.setDp(activity,"dp_H20" ,dp_H20);
        MySharedPreference.setDp(activity,"dp_30" ,dp_30);
        MySharedPreference.setDp(activity,"dp_70" ,dp_70);
        MySharedPreference.setDp(activity, "dp_H70",dp_H70);
        MySharedPreference.setDp(activity,"dp_10" ,dp_10 );
        MySharedPreference.setDp(activity,"dp_40" ,dp_40);

    }
}
