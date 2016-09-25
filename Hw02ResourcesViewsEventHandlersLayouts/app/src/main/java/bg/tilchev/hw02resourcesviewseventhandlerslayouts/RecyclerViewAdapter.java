package bg.tilchev.hw02resourcesviewseventhandlerslayouts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import bg.tilchev.hw02resourcesviewseventhandlerslayouts.interfaces.RecyclerViewSelectedElement;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SquareViewHolder> {

    public static class SquareViewHolder extends RecyclerView.ViewHolder {

        private SquareView mSquareView;
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public SquareViewHolder(View itemView) {
            super(itemView);
            this.mSquareView = (SquareView) itemView.findViewById(R.id.square);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(SquareViewHolder.this.position);
                }
            });
        }
    }

    private byte[] mGridValues;
    public static RecyclerViewSelectedElement mListener;

    public RecyclerViewAdapter(RecyclerViewSelectedElement listener) {
        this.mGridValues = new byte[9];
        mListener = listener;
    }

    public void setValueAt(int position, byte value) {
        this.mGridValues[position] = value;
        this.notifyItemChanged(position);
    }

    public byte[] getGridValues() {
        return Arrays.copyOf(this.mGridValues, this.getItemCount());
    }

    @Override
    public SquareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.square_layout, parent, false);
        SquareViewHolder squareViewHolder = new SquareViewHolder(view);
        return squareViewHolder;
    }

    @Override
    public void onBindViewHolder(SquareViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        holder.mSquareView.setMark(this.mGridValues[position]);
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return this.mGridValues.length;
    }
}
