package alex790.testapp.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import alex790.testapp.R;
import alex790.testapp.interfaces.OnPhotoItemLongClickListener;
import alex790.testapp.models.PhotoRecord;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex790 on 07.06.2018.
 */

public class PhotoHolder extends RecyclerView.ViewHolder {

    private OnPhotoItemLongClickListener onLongClickListener;
    protected PhotoRecord record;

    @BindView(R.id.iv_photo)
    protected ImageView ivPhoto;

    @BindView(R.id.tv_tegs)
    protected TextView tvTegs;


    public PhotoHolder(View view, OnPhotoItemLongClickListener onLongClickListener) {
        super(view);
        this.onLongClickListener = onLongClickListener;
        ButterKnife.bind(this, view);

        view.setOnLongClickListener(longClick);
    }


    public void bind(PhotoRecord record) {
        this.record = record;

        Picasso.get().load("file://" + record.getPath())
                .placeholder(R.mipmap.ic_launcher)
                .resizeDimen(R.dimen.image_size, R.dimen.image_size)
                .centerInside()
                .into(ivPhoto);

        tvTegs.setText(record.getTags());
    }


    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            if (onLongClickListener != null) {
                onLongClickListener.onClick(record);

                return true;
            }

            return false;
        }
    };



}

