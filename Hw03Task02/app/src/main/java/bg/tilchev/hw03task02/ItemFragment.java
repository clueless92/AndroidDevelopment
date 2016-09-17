package bg.tilchev.hw03task02;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemFragment extends Fragment {

    public static final String CITY_KEY = "ItemFragment.CITY_KEY";

    private TextView getTextView(View rootView) {
        View view = rootView.findViewById(R.id.text_view);
        if (view instanceof TextView) {
            return (TextView) view;
        }
        return null;
    }

    private void setTextViewText(View rootView, CharSequence value) {
        if (rootView == null) {
            return;
        }
        TextView textView = this.getTextView(rootView);
        if (textView == null) {
            return;
        }
        textView.setText(value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_layout, container, false);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            City city = arguments.getParcelable(CITY_KEY);
            if (city != null) {
                String cityName = city.getName();
                this.setTextViewText(view, cityName);
            }
        }
        return view;
    }
}
