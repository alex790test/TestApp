package alex790.testapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import alex790.testapp.db.AppDataBase;
import alex790.testapp.interfaces.I_AppDataBase;
import alex790.testapp.interfaces.I_AppManager;
import alex790.testapp.presenters.MainPresenter;

/**
 * Класс является интерфейсом работы с бизнес логикой
 *
 * Created by alex790 on 07.06.2018.
 */

public class AppManager implements I_AppManager {


    private Context context;
    private MainPresenter mainPresenter;
    private AppDataBase appDataBase;


    public AppManager(Context context) {
        this.context = context;
        // запускаем базу данных
        appDataBase = Room.databaseBuilder(context, AppDataBase.class, AppDataBase.neme).build();
        // создаём презентер для работы с фотографиями
        mainPresenter = new MainPresenter(appDataBase);
    }


    /**
     * @return перзентер работы с фотографиями
     */
    @Override
    public MainPresenter getMainPresenter() {
        return mainPresenter;
    }


    /**
     * Выход из приложения
     */
    @Override
    public void exit() {
        // закрываем работу с БД
        appDataBase.close();
        // останавливаем приложение
        System.exit(0);
    }



}
