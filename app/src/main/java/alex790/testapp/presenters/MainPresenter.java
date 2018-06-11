package alex790.testapp.presenters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.List;

import alex790.testapp.interfaces.I_AppDataBase;
import alex790.testapp.models.PhotoRecord;
import io.reactivex.Completable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Класс предоставляет доступ к данным медиа котента.
 *
 * Created by alex790 on 09.06.2018.
 */

public class MainPresenter {


    private MutableLiveData<List<PhotoRecord>> recordLiveData;
    private I_AppDataBase appDataBase;

    // consumer работы с базой данных (сюда поступают события при обновлении базы данных)
    private Consumer<List<PhotoRecord>> consumer = new Consumer<List<PhotoRecord>>() {
        @Override
        public void accept(List<PhotoRecord> photoRecords) throws Exception {
            recordLiveData.postValue(photoRecords);
        }
    };


    public MainPresenter(I_AppDataBase appDataBase) {
        this.appDataBase = appDataBase;
        recordLiveData = new MutableLiveData<>();
        // подписываем consumer на работу с БД
        appDataBase.recordDao().getAll().subscribe(consumer);
    }


    /**
     * Добвить фото в список
     * @param pathFile путь к файлу
     * @param tags сисок тегов
     */
    public void addPhoto(final String pathFile, final String tags) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                PhotoRecord record = new PhotoRecord(pathFile, tags);
                appDataBase.recordDao().insert(record);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * Удалить фото из списка
     * @param record обьект списка
     */
    public void removePhoto(final PhotoRecord record) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                appDataBase.recordDao().delete(record);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * Установит визуальный компонент в качестве наблюдателя изменения списка фотогрвфий
     * @param lifecycleOwner обьект реализующий интерфейс LifecycleOwner
     * @param observer наблюдатель изменения списка
     */
    public void setRecordObserver(@NonNull LifecycleOwner lifecycleOwner, Observer<List<PhotoRecord>> observer) {
        recordLiveData.observe(lifecycleOwner, observer);
    }

}
