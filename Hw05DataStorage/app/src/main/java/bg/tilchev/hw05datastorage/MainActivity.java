package bg.tilchev.hw05datastorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.Listener, View.OnClickListener {

    private static final int AMOUNT_TO_ADD_ON_CLICK = 20;
    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseManager mDatabaseManager;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);

        Button addButton = this.getAddButton();
        if (addButton == null) {
            return;
        }
        addButton.setOnClickListener(this);
        Button refreshButton = this.getRefreshButton();
        if (refreshButton == null) {
            return;
        }
        refreshButton.setOnClickListener(this);

        List<Item> itemsFromDatabase = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
        recyclerView.setLayoutManager(layoutManager);
        this.mAdapter = new RecyclerViewAdapter(itemsFromDatabase, this);
        recyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onItemSelected(int position) {
        Log.d(TAG, "Item " + position + " clicked");
    }

    @Override
    public void onClick(View v) {
        if(v == null) {
            return;
        }
        if (v.getId() == R.id.button_add_to_db) {
            this.onAddToDbButtonClicked();
        } else if (v.getId() == R.id.button_refresh_rv) {
            this.onRefreshButtonClicked();
        }
    }

    private void onRefreshButtonClicked() {
        Log.d(TAG, "Refreshed RV with added items");
        DatabaseManager dataManager = this.getDatabaseManager();
        List<Item> itemsFromDB = dataManager.getAllItems();
        this.mAdapter.addItems(itemsFromDB);
    }

    private void onAddToDbButtonClicked() {
        Log.d(TAG, "Added " + AMOUNT_TO_ADD_ON_CLICK + " items to DB");
        DatabaseManager dataManager = this.getDatabaseManager();
        int currNewIndex = dataManager.getItemCount();
        int limit = currNewIndex + AMOUNT_TO_ADD_ON_CLICK;
        for (int i = currNewIndex; i < limit; i++) {
            Item item;
            if (i % 3 == 0) {
                item = new Item(i, "Even", this.getString(R.string.test_base64_img));
            } else if (i % 3 == 1) {
                item = new Item(i, "Odd", this.getString(R.string.laptop_base64_img));
            } else {
                item = new Item(i, "Odd", null);
            }
            dataManager.addItem(item);
        }
    }

    private Button getAddButton() {
        View view = this.findViewById(R.id.button_add_to_db);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private Button getRefreshButton() {
        View view = this.findViewById(R.id.button_refresh_rv);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }

    private DatabaseManager getDatabaseManager() {
        if (this.mDatabaseManager == null) {
            this.mDatabaseManager = new DatabaseManager(this);
        }
        return this.mDatabaseManager;
    }
}
