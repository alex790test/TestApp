package alex790.testapp.interfaces;

import alex790.testapp.presenters.MainPresenter;

/**
 * Created by alex790 on 07.06.2018.
 */

public interface I_AppManager {

    MainPresenter getMainPresenter();

    void exit();
}
