package bg.tilchev.loadimagetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecycleViewSelectedElement {

    private List<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);

        this.mItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Item item = new Item("Asd", this.getString(R.string.TEST_BASE64_IMG));
            this.mItems.add(item);
        }

        RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, 1, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapter(this.mItems, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSongSelected(int position) {
        // on click
    }
}
