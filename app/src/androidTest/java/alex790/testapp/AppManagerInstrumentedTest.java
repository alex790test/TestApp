package alex790.testapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import alex790.testapp.db.AppDataBase;
import alex790.testapp.interfaces.I_AppManager;
import alex790.testapp.models.PhotoRecord;
import alex790.testapp.presenters.MainPresenter;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by alex790 on 10.06.2018.
 */

@RunWith(AndroidJUnit4.class)
public class AppManagerInstrumentedTest {


    private Context context;
    private AppDataBase appDataBase;


    // инициализируем компоненты
    @Before
    public void init() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        appDataBase = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.nemeTest).build();
    }


    @Test
    public void testScenario() throws Exception {
        // проверяем создание презентера
        mainPresenterCreate();
        // перд проверкой встаки/удаления чистим тестовую бвзу (на случай незапланированных записей)
        clearDb();
        // проверяем добавление записи в бд
        bdInsert();
        // проверяем удаление записи из бд
        bdDelete();
        // проверяем добавление фоторграфии в презентере
        mainPresenterAddPhoto();
        // проверяем удаление фоторграфии в презентере
        mainPresenterRemovePhoto();
    }


    // проверяем создание презентера
    public void mainPresenterCreate() throws Exception {
        I_AppManager appManager = new AppManager(context);
        assertNotNull(appManager.getMainPresenter());
        appManager.exit();
    }


    // перд проверкой встаки/удаления в презентере чистим тестовую бвзу (на случай незапланированных записей)
    public void clearDb() throws Exception {
        appDataBase.recordDao().deleteAll();
        List<PhotoRecord> records = appDataBase.recordDao().getAllRecord();

        assertEquals(0, records.size());
    }


    // проверяем добавление записи в бд
    public void bdInsert() throws Exception {
        appDataBase.recordDao().insert(new PhotoRecord("path", "tag"));
        List<PhotoRecord> records = appDataBase.recordDao().getAllRecord();

        assertEquals(1, records.size());
    }


    // проверяем удаление записи из бд
    public void bdDelete() throws Exception {
        List<PhotoRecord> records = appDataBase.recordDao().getAllRecord();
        appDataBase.recordDao().delete(records.get(0));
        records = appDataBase.recordDao().getAllRecord();

        assertEquals(0, records.size());
    }


    // проверяем добавление фоторграфии в презентере
    public void mainPresenterAddPhoto() throws Exception {
        String path = "path";
        String tag = "tag, tag2";

        // получаем экземпляр класса презентера
        MainPresenter mainPresenter = new MainPresenter(appDataBase);
        // добавляем данные о фото
        mainPresenter.addPhoto(path, tag);

        // т.к. метод добавления асинхронный, останавливаем текущий поток, т.к. мы не в UI, а в потоке выполнения тестов, а значит, можем себе это позволить
        Thread.sleep(200);

        // получаем список записей в БД
        List<PhotoRecord> records = appDataBase.recordDao().getAllRecord();
        // проверяем, что запись в БД есть и что она содержит те поля, которые мы передали при сохранении фото
        assertEquals(1, records.size());
        assertEquals(path, records.get(0).getPath());
        assertEquals(tag, records.get(0).getTags());
    }


    // проверяем удаление фоторграфии в презентере
    public void mainPresenterRemovePhoto() throws Exception {
        // получаем экземпляр класса презентера
        MainPresenter mainPresenter = new MainPresenter(appDataBase);
        // получаем список записей в БД
        List<PhotoRecord> records = appDataBase.recordDao().getAllRecord();
        // удаляем запись о фото (запись сформирована в mainPresenterAddPhoto())
        mainPresenter.removePhoto(records.get(0));

        // т.к. метод удаления асинхронный, останавливаем текущий поток, т.к. мы не в UI, а в потоке выполнения тестов, а значит, можем себе это позволить
        Thread.sleep(200);

        // повторно получаем список записей в БД
        records = appDataBase.recordDao().getAllRecord();
        // проверяем что записи нет
        assertEquals(0, records.size());
    }


    // после всех действий закрываем базу
    @After
    public void closeDb() throws Exception {
        appDataBase.close();
    }

}
