package bg.tilchev.hw03task01;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemSelected {

    public static final String TAG = "MainActivity";
    private static final int DATASET_COUNT = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);

        RecyclerView recyclerView = this.getRecyclerView();
        if (recyclerView == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Item> data = this.createData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    private RecyclerView getRecyclerView() {
        View view = this.findViewById(R.id.recycler_view);
        if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        }
        return null;
    }

    private List<Item> createData() {
        List<Item> data = new ArrayList<>();

        for (int i = 0; i < DATASET_COUNT; i++) {
            Item item = new Item(Integer.toString(i));
            data.add(item);
        }
        return data;
    }

    @Override
    public void onItemSelected(int position) {
        Log.d(TAG, "Element " + position + " clicked.");
        FragmentManager fragmentManager = this.getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_item);
        if (!(fragment instanceof ItemFragment)) {
            return;
        }
        ItemFragment itemFragment = (ItemFragment) fragment;
        Item item = this.getItem(position);
        itemFragment.setItem(item);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_item, itemFragment);
        fragmentTransaction.commit();
    }

    private Item getItem(int position) {
        RecyclerView recyclerView = this.getRecyclerView();
        if (recyclerView == null) {
            return null;
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof RecyclerViewAdapter)) {
            return null;
        }
        RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
        Item item = recyclerViewAdapter.getItem(position);
        return item;
    }
}
