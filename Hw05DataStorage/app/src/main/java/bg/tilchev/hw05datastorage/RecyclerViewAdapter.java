package bg.tilchev.hw05datastorage;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    public interface Listener {

        void onItemSelected(int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements AsyncImageLoader.Listener {

        private Listener mListener;

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

        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    Log.d(TAG, "Element " + position + " clicked.");
                    ViewHolder.this.mListener.onItemSelected(position);
                }
            });
        }

        private void setListener(Listener listener) {
            this.mListener = listener;
        }

        private void setTextViewValue(CharSequence text) {
            TextView textView = this.getTextView(this.itemView);
            if (textView != null) {
                textView.setText(text);
            }
        }

        private void setImageViewBitmap(Bitmap bitmap) {
            ImageView imageView = this.getImageView(this.itemView);
            if (imageView == null) {
                return;
            }
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onImageLoaded(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            this.setImageViewBitmap(bitmap);
            int currPosition = this.getAdapterPosition();
            Item item = RecyclerViewAdapter.this.mItems.get(currPosition);
            int id = item.getId();
            Map<Integer, Bitmap> bitmapCache = RecyclerViewAdapter.this.getLoadedBitmaps();
            bitmapCache.put(id, bitmap);
        }
    }

    private List<Item> mItems;
    private Map<Integer, Bitmap> mLoadedBitmaps;

    public Listener mListener;

    public RecyclerViewAdapter(List<Item> items, Listener listener) {
        this.mItems = items;
        this.mListener = listener;
    }

    public void setItems(List<Item> items) {
        this.mItems = items;
        this.notifyDataSetChanged();
    }

    private Map<Integer, Bitmap> getLoadedBitmaps() {
        if (this.mLoadedBitmaps == null) {
            this.mLoadedBitmaps = new WeakHashMap<>();
        }
        return this.mLoadedBitmaps;
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
        int id = item.getId();
        String text = item.getText();
        holder.setTextViewValue(id + ". " + text);
        Map<Integer, Bitmap> bitmapCache = this.getLoadedBitmaps();
        Bitmap bitmap = bitmapCache.get(id);
        if (bitmap == null) {
            AsyncImageLoader imageLoader = new AsyncImageLoader(holder);
            imageLoader.execute(item);
        } else {
            holder.setImageViewBitmap(bitmap);
        }
    }
}
