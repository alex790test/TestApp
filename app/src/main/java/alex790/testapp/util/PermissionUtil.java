package alex790.testapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Утилитарный класс работы с разрешениями
 *
 * Created by alex790 on 08.06.2018.
 */

public class PermissionUtil {


    public static final int REQUEST_ALL_PERMISSIONS = 0;


    public static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    /**
     * проверка наличия разрешений для работы приложения
     * @param context контекст приложения
     * @return установлено ли разрешение
     */
    public static boolean checkPermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * запрос разрешений
     * @param activity на котором отобразится диалог подтверждения
     */
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_ALL_PERMISSIONS);
    }

}
