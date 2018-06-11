package alex790.testapp.activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import alex790.testapp.App;
import alex790.testapp.R;
import alex790.testapp.adapters.PhotoAdapter;
import alex790.testapp.dialogs.RemovePhotoDialog;
import alex790.testapp.dialogs.TagsDialog;
import alex790.testapp.interfaces.I_AppManager;
import alex790.testapp.interfaces.OnPhotoItemLongClickListener;
import alex790.testapp.models.PhotoRecord;
import alex790.testapp.util.FileUtil;
import alex790.testapp.util.PermissionUtil;
import alex790.testapp.util.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;


public class MainActivity extends AppCompatActivity {


    private final static int REQUEST_CODE_OPEN_IMAGE_FILE = 1;
    private static final String HAS_PERMISSION_REQUEST = "hasPermissionRequest";
    private static final String SEARCH_TAGS = "searchTags";
    private static final String EMPTY_SEARC_TAG = "";

    private I_AppManager appManager;
    private PhotoAdapter adapter;

    private boolean hasPermissionRequest;
    private LinearLayoutManager layoutManager;
    private String searchTags = EMPTY_SEARC_TAG;
    private PublishSubject<String> subject;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;


    // Наблюдатель изменения списка фотографий
    private Observer<List<PhotoRecord>> recordObserver = new Observer<List<PhotoRecord>>() {
        @Override
        public void onChanged(@Nullable List<PhotoRecord> recordList) {

            adapter.setItems(recordList);

            if (!searchTags.isEmpty()){
                adapter.getFilter().filter(searchTags);
            }

            int scrollPosition = recordList.size() - 1;

            if(scrollPosition != RecyclerView.NO_POSITION){
                layoutManager.scrollToPosition(scrollPosition);
            }
        }
    };

    // слушатель долгого нажатия на элемент списка
    private OnPhotoItemLongClickListener longClickListener = new OnPhotoItemLongClickListener() {
        @Override
        public void onClick(PhotoRecord record) {

            Bundle arguments = new Bundle();
            arguments.putSerializable(RemovePhotoDialog.RECORD_KEY, record);

            RemovePhotoDialog dialog = new RemovePhotoDialog();
            dialog.setArguments(arguments);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            dialog.show(transaction, RemovePhotoDialog.TAG);
        }
    };

    // слушатель поля ввода поиска по тегам
    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {return false;}
        @Override
        public boolean onQueryTextChange(final String searcText) {
            subject.onNext(searcText);
            searchTags = searcText;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        appManager = App.getAppManager();

        adapter = new PhotoAdapter(new ArrayList<PhotoRecord>());
        adapter.setOnLongClickListener(longClickListener);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // весь паттерн работы с разрешениями реализовывать не будем, т.к работоспособнсть приложения сохраняется
        // проверяем установлены ли разрешения
        boolean permissionGranted = PermissionUtil.checkPermission(this);

        if (!permissionGranted && !hasPermissionRequest) {
            PermissionUtil.requestPermission(this);
        }
        else{
            adapter.notifyDataSetChanged();
        }
        // подписываемся на получение списка фотографий
        appManager.getMainPresenter().setRecordObserver(this, recordObserver);

        // реализация задержки фильтрации в процессе ввода искомых тегов
        subject = PublishSubject.create();
        subject.debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        adapter.getFilter().filter(result);
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        subject.onComplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);

        if (!searchTags.isEmpty()){
            menuItem.expandActionView();
        }

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_by_tags));
        searchView.setOnQueryTextListener(searchListener);
        searchView.setQuery(searchTags, true);

        return true;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            hasPermissionRequest = savedInstanceState.getBoolean(HAS_PERMISSION_REQUEST, false);
            searchTags = savedInstanceState.getString(SEARCH_TAGS, EMPTY_SEARC_TAG);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(HAS_PERMISSION_REQUEST, hasPermissionRequest);
        outState.putString(SEARCH_TAGS, searchTags);
    }


    @OnClick(R.id.fab)
    protected void onClickFab() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*");

        Intent chooser = Intent.createChooser(intent, getString(R.string.select_app));
        startActivityForResult(chooser, REQUEST_CODE_OPEN_IMAGE_FILE);
    }


    @Override
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        if(resultCode == RESULT_OK && request == REQUEST_CODE_OPEN_IMAGE_FILE) {
            // получаем данные о фото
            Uri uri = data.getData();
            Result result = FileUtil.getFilePath(this, uri);

            // если результат обработки данных фотографии содержит ошибки
            if (result.getErrorCode() != Result.NO_ERROR) {
                showToast(result.getErrorCode());
            }
            else {
                // показываем диалог ввода тегов
                showDialog(result.getValue());
            }
        }
    }


    // показать диалог ввода тегов
    private void showDialog(String pathFile) {

        Bundle arguments = new Bundle();
        arguments.putString(TagsDialog.PATH_FILE, pathFile);

        final TagsDialog dialog = new TagsDialog();
        dialog.setArguments(arguments);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        dialog.show(transaction, TagsDialog.TAG);
    }


    public void showToast(final int res) {
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }


    // решение пользователя по разрешениям
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        hasPermissionRequest = true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // говорим что выходим
        appManager.exit();
    }


}
