package bg.tilchev.hw03task01;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    public interface ItemSelected {

        void onItemSelected(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSelected mListener;

        private TextView getTextView(View rootView) {
            if (rootView == null) {
                return null;
            }
            View view = rootView.findViewById(R.id.text_view);
            if (view instanceof TextView) {
                return (TextView) view;
            }
            return null;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ViewHolder.this.mListener == null) {
                        return;
                    }
                    int position = ViewHolder.this.getAdapterPosition();
                    ViewHolder.this.mListener.onItemSelected(position);
                }
            });
        }

        public void setListener(ItemSelected listener) {
            this.mListener = listener;
        }

        public void setTextViewValue(CharSequence text) {
            TextView textView = this.getTextView(this.itemView);
            if (textView != null) {
                textView.setText(text);
            }
        }
    }

    private List<Item> mData;
    private ItemSelected mListener;

    public RecyclerViewAdapter(List<Item> data, ItemSelected listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public Item getItem(int position) {
        return this.mData.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        if (holder == null) {
            return;
        }
        holder.setListener(this.mListener);
        Item item = this.mData.get(position);
        String itemText = item.getText();
        holder.setTextViewValue(itemText);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }
}
