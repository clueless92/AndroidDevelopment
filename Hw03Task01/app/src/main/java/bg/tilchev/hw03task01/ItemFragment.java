package bg.tilchev.hw03task01;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends Fragment {

    public static final String ITEM_KEY = "ItemFragment.ITEM_KEY";

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

    public void setItem(Item item) {
        View rootView = this.getView();
        TextView textView = this.getTextView(rootView);
        if (textView == null) {
            return;
        }
        String text = null;
        if (item != null) {
            text = item.getText();
        }
        textView.setText(text);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_layout, container, false);
        return view;
    }
}
