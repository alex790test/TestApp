package alex790.testapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by alex790 on 07.06.2018.
 */

@Entity
public class PhotoRecord implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private long id;

    private String path;
    private String tags;


    public PhotoRecord(String path, String tags) {
        this.path = path;
        this.tags = tags;
    }


    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }


    public String getPath() {
        return path;
    }


    public String getTags() {
        return tags;
    }

}
