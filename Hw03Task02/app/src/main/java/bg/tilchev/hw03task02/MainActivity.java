package bg.tilchev.hw03task02;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ButtonFragment.ButtonHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);
    }

    @Override
    public void onCityButtonClick(City city) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ItemFragment.CITY_KEY, city);
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(bundle);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_start, itemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
