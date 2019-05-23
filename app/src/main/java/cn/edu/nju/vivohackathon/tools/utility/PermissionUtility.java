package cn.edu.nju.vivohackathon.tools.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class PermissionUtility {

    /**
     * @param activity   Activity who
     * @param permission Use Manifest.permission.xxx
     */
    public static void requestPermission(@NonNull Activity activity, @NonNull Context context, @NonNull String permission, @NonNull String promptMessage) {
        if (!isPermissionGranted(context, permission)) {
            Toast.makeText(context, promptMessage, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
        }
    }

    public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
