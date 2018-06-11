package alex790.testapp;

import android.content.Context;
import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import java.io.File;
import alex790.testapp.models.PhotoRecord;
import alex790.testapp.util.FileUtil;
import alex790.testapp.util.Result;

import static junit.framework.Assert.assertEquals;


/**
 * Created by alex790 on 09.06.2018.
 */

//@RunWith(MockitoJUnitRunner.class)
@RunWith(RobolectricTestRunner.class)
public class AppUnitTests {


    @Mock
    private Context context;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testPhotoRecord() throws Exception {

        String path = "path";
        String tag = "tag, teg2";

        PhotoRecord record = new PhotoRecord(path, tag);

        assertEquals(path, record.getPath());
        assertEquals(tag, record.getTags());
    }


    @Test
    public void testGetFilePath() {

        // проверяем получение пути для uri изображения
        Result resultJpg = FileUtil.getFilePath(context, bildUriGpj());
        assertEquals(Result.NO_ERROR, resultJpg.getErrorCode());

        // проверяем получение пути для uri файла другого типа
        Result resultTxt = FileUtil.getFilePath(context, bildUriTxt());
        assertEquals(R.string.invalid_file_extension, resultTxt.getErrorCode());

        // проверяем получение пути для не корректного uri
        Result result = FileUtil.getFilePath(context, bildUriUnKnow());
        assertEquals(R.string.error_get_photo_path, result.getErrorCode());
    }


    private Uri bildUriGpj() {
      return Uri.fromFile(new File("/storage/emulated/0/Android/data/alex790.testapp/files/test.jpg"));
    }


    private Uri bildUriTxt() {
        return Uri.fromFile(new File("/storage/emulated/0/Android/data/alex790.testapp/files/test.txt"));
    }


    private Uri bildUriUnKnow() {
        String authority = "com.android.externalstorage.documents";
        String content = "content";
        return Uri.parse("content://" + authority + "/" + content);
    }


}
