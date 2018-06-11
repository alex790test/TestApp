package alex790.testapp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import alex790.testapp.R;


/**
 * Утилитарный класс работы с файлами
 *
 * Created by alex790 on 07.06.2018.
 */

public class FileUtil {


    private final static String DATA = "_data";
    private final static String CONTENT = "content";
    private final static String FILE = "file";


    /**
     * @param context
     * @param uri
     * @return результат преобразования uri в путь расположения файла
     */
    public static Result getFilePath(Context context, Uri uri) {

        String filePath = getPath(context, uri);

        if (filePath == null) {
            int error = R.string.error_get_photo_path;
            return new Result(error, null);
        }

        String extenssion = getFileExtension(filePath).toLowerCase();

        if (isPicture(extenssion)) {
            return new Result(Result.NO_ERROR, filePath);
        }
        else {
            int error = R.string.invalid_file_extension;
            return new Result(error, null);
        }
    }


    /**
     * Определение типа файла по расширению
     * @param extension расширение
     * @return является ли изображением
     */
    public static boolean isPicture(String extension) {
        return extension.endsWith("jpg") || extension.endsWith("jpeg") || extension.endsWith("png") || extension.endsWith("tiff")
                || extension.endsWith("tif") || extension.endsWith("psd")  || extension.endsWith("bmp");
    }


    /**
     * @param context
     * @param uri
     * @return путь расположения файла
     */
    public static String getPath(Context context, final Uri uri) {

        // MediaStore
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri);
        }
        // File
        else if (FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }



    /**
     * @param context
     * @param uri
     * @return путь расположения файла
     */
    public static String getDataColumn(Context context, Uri uri) {

        Cursor cursor = null;
        final String column = DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                for (String s : cursor.getColumnNames()) {
                    final int column_index = cursor.getColumnIndexOrThrow(s);
                    cursor.getString(column_index);
                }

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }


    /**
     * @param fileName имя файла или путь
     * @return расширение
     */
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "";
    }
}
