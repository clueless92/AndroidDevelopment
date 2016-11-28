package bg.tilchev.loadimagetest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecycleViewSelectedElement mListener;

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

        private ImageView getImageView(View rootView) {
            if (rootView == null) {
                return null;
            }
            View view = rootView.findViewById(R.id.image_view);
            if (view instanceof ImageView) {
                return (ImageView) view;
            }
            return null;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    Log.d(TAG, "Element " + position + " clicked.");
                    ViewHolder.this.mListener.onSongSelected(position);
                }
            });
        }

        public void setListener(RecycleViewSelectedElement listener) {
            this.mListener = listener;
        }

        public void setTextViewValue(CharSequence text) {
            TextView textView = this.getTextView(this.itemView);
            if (textView != null) {
                textView.setText(text);
            }
        }

        public void setImageViewBitmap(Bitmap bitmap) {
            ImageView textView = this.getImageView(this.itemView);
            if (textView != null) {
                textView.setImageBitmap(bitmap);
            }
        }
    }

    private List<Item> mItems;
    public RecycleViewSelectedElement mListener;

    public RecyclerViewAdapter(List<Item> items, RecycleViewSelectedElement listener) {
        this.mItems = items;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        Log.d(TAG, "Element " + position + " set.");
        holder.setListener(this.mListener);
        Item item = this.mItems.get(position);
        String songName = item.getSongName();
        holder.setTextViewValue(songName);
        byte[] decodedString = Base64.decode(item.getImgSrc(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.setImageViewBitmap(decodedBitmap);
    }
}
