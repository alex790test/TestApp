package alex790.testapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import alex790.testapp.R;
import alex790.testapp.adapters.holders.PhotoHolder;
import alex790.testapp.interfaces.OnPhotoItemLongClickListener;
import alex790.testapp.models.PhotoRecord;
import alex790.testapp.util.TagsUtil;

/**
 * Created by alex790 on 07.06.2018.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> implements Filterable {


    private List<PhotoRecord> originalItems;
    private List<PhotoRecord> filteredItems;
    private OnPhotoItemLongClickListener onLongClickListener;
    private PhotoFilter mFilter;


    public PhotoAdapter(List<PhotoRecord> recordList) {
        originalItems = recordList;
        filteredItems = recordList;
        mFilter = new PhotoFilter();
    }


    public void setItems(List<PhotoRecord> items) {
        this.originalItems = items;
        this.filteredItems = items;
        notifyDataSetChanged();
    }


    public void setOnLongClickListener(OnPhotoItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoHolder(v, onLongClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        PhotoRecord record = filteredItems.get(position);
        holder.bind(record);
    }


    @Override
    public int getItemCount() {
        return filteredItems.size();
    }


    @Override
    public Filter getFilter() {
        return mFilter;
    }



    private class PhotoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            List<PhotoRecord> items;

            if (filterString.isEmpty()){
                items = originalItems;
            }
            else {
                items = new ArrayList<>();

                for (PhotoRecord record : originalItems) {
                    String recordString = record.getTags().toLowerCase();

                    if (TagsUtil.hasTags(recordString, filterString)){
                        items.add(record);
                    }
                }
            }

            results.values = items;
            results.count = items.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (List<PhotoRecord>) results.values;
            notifyDataSetChanged();
        }
    }
}
