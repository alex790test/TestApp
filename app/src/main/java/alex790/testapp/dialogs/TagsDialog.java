package alex790.testapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alex790.testapp.App;
import alex790.testapp.R;
import alex790.testapp.presenters.MainPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex790 on 08.06.2018.
 */

public class TagsDialog extends DialogFragment implements DialogInterface.OnShowListener {


    public static String PATH_FILE = "pathfile";
    public static String TAG = "TagsDialog";

    private MainPresenter presenter;
    private String pathFile;

    @BindView(R.id.et_tags)
    protected EditText etTags;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = App.getAppManager().getMainPresenter();
        pathFile = getArguments().getString(PATH_FILE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.item_input_tags_line, null);
        ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_tags)
                .setView(view)
                .setPositiveButton(R.string.Ok, null)
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    @Override
    public void onShow(DialogInterface dialogInterface) {

        AlertDialog dialog = (AlertDialog) dialogInterface;
        Button positivebutton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags = etTags.getText().toString();

                if (tags.isEmpty()){
                    Toast.makeText(getActivity(), R.string.teg_required, Toast.LENGTH_SHORT).show();
                }
                else if (trimTags(tags).isEmpty()) {
                    Toast.makeText(getActivity(), R.string.invalid_teg, Toast.LENGTH_SHORT).show();
                }
                else{
                    presenter.addPhoto(pathFile, tags);
                    dismiss();
                }
            }
        });
    }


    private String trimTags(String tags) {
        String trimmed = tags.replace(",","");
        return trimmed.trim();
    }



}
