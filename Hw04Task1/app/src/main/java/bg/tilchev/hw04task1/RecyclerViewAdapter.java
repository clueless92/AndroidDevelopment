package bg.tilchev.hw04task1;

import android.support.v7.widget.RecyclerView;
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

        public void setImageViewResID(int resID) {
            ImageView textView = this.getImageView(this.itemView);
            if (textView != null) {
                textView.setImageResource(resID);
            }
        }
    }

    private List<Song> mSongs;
    public RecycleViewSelectedElement mListener;

    public RecyclerViewAdapter(List<Song> songs, RecycleViewSelectedElement listener) {
        this.mSongs = songs;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return this.mSongs.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
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
        Song song = this.mSongs.get(position);
        String songName = song.getSongName();
        holder.setTextViewValue(songName);
        if (position % 2 == 0) {
            holder.setImageViewResID(R.drawable.download_cut);
        } else {
            holder.setImageViewResID(R.drawable.explicit_cut);
        }
    }
}
