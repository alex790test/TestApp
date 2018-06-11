package alex790.testapp.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;
import alex790.testapp.models.PhotoRecord;
import io.reactivex.Flowable;

/**
 * Created by alex790 on 08.06.2018.
 */

@Dao
public interface PhotoRecordDao {


    @Query("SELECT * FROM PhotoRecord")
    Flowable<List<PhotoRecord>> getAll();


    @Query("SELECT * FROM PhotoRecord")
    List<PhotoRecord> getAllRecord();


    @Insert
    long insert(PhotoRecord record);


    @Delete
    void delete(PhotoRecord record);


    @Query("DELETE FROM PhotoRecord")
    void deleteAll();
}
