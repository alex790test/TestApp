package alex790.testapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import alex790.testapp.db.dao.PhotoRecordDao;
import alex790.testapp.interfaces.I_AppDataBase;
import alex790.testapp.models.PhotoRecord;

/**
 * Created by alex790 on 08.06.2018.
 */


@Database(entities = {PhotoRecord.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase implements I_AppDataBase {

    public static String neme = "appdatabase";
    public static String nemeTest = "appdatabaseTest";

    @Override
    public abstract PhotoRecordDao recordDao();
}
