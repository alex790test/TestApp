package alex790.testapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import alex790.testapp.App;
import alex790.testapp.R;
import alex790.testapp.models.PhotoRecord;
import alex790.testapp.presenters.MainPresenter;

/**
 * Created by alex790 on 08.06.2018.
 */

public class RemovePhotoDialog extends DialogFragment {


    public static String RECORD_KEY = "record";
    public static String TAG = "RemovePhotoDialog";

    private MainPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = App.getAppManager().getMainPresenter();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final PhotoRecord record = (PhotoRecord) getArguments().getSerializable(RECORD_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_remove_photo)
                .setPositiveButton(R.string.Ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                        presenter.removePhoto(record);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.Cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }






}
