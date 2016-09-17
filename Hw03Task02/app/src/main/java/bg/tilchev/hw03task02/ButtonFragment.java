package bg.tilchev.hw03task02;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ButtonFragment extends Fragment implements View.OnClickListener {

    public interface ButtonHandler {
        void onCityButtonClick(City city);
    }

    private ButtonHandler callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.button_fragment_layout, container, false);
        Button buttonShopenhagen = this.getShopenhagenButton(view);
        Button buttonSvishtokio = this.getSvishtokioButton(view);
        Button buttonHiustanovo = this.getHiustanovoButton(view);
        if (buttonShopenhagen != null) {
            buttonShopenhagen.setOnClickListener(this);
        }
        if (buttonSvishtokio != null) {
            buttonSvishtokio.setOnClickListener(this);
        }
        if (buttonHiustanovo != null) {
            buttonHiustanovo.setOnClickListener(this);
        }
        return view;
    }

    private Button getShopenhagenButton(View rootView) {
        if (rootView == null) {
            return null;
        }
        View view = rootView.findViewById(R.id.button_shopenhagen);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private Button getSvishtokioButton(View rootView) {
        if (rootView == null) {
            return null;
        }
        View view = rootView.findViewById(R.id.button_svishtokio);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private Button getHiustanovoButton(View rootView) {
        if (rootView == null) {
            return null;
        }
        View view = rootView.findViewById(R.id.button_hiustanovo);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ButtonHandler) {
            this.callback = (ButtonHandler) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    @Override
    public void onClick(View v) {
        if (this.callback == null) {
            return;
        }
        if (!(v instanceof Button)) {
            return;
        }
        Button cityButton = (Button) v;
        String cityName = cityButton.getText().toString();
        City city = new City(cityName);
        this.callback.onCityButtonClick(city);
    }
}
