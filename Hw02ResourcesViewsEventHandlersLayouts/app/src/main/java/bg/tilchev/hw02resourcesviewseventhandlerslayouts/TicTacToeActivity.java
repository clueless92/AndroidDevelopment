package bg.tilchev.hw02resourcesviewseventhandlerslayouts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bg.tilchev.hw02resourcesviewseventhandlerslayouts.interfaces.RecyclerViewSelectedElement;

public class TicTacToeActivity extends Activity implements RecyclerViewSelectedElement {

    private static final int GRID_SIDE = 3;
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private byte lastPlayerValue;
    private byte moveCount;
    private boolean isGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tic_tac_toe);
        this.mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        this.mLayoutManager = new GridLayoutManager(this, GRID_SIDE);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new RecyclerViewAdapter(this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.lastPlayerValue = PLAYER_1;
        this.moveCount = 0;
        this.isGameOver = false;
    }

    // TODO: refactor method
    @Override
    public void onItemSelected(int position) {
        if (this.isGameOver) {
            this.resetGame();
            return;
        }
        if (this.mAdapter.getGridValues()[position] != 0) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.lastPlayerValue == PLAYER_1) {
            this.lastPlayerValue = PLAYER_2;
        } else {
            this.lastPlayerValue = PLAYER_1;
        }
        this.moveCount++;
        this.mAdapter.setValueAt(position, this.lastPlayerValue);
        List<Integer> winCoords = this.checkForWin();
        if (winCoords == null) {
            if (this.moveCount >= this.mAdapter.getItemCount()) {
                Toast.makeText(this, "Game tied!", Toast.LENGTH_LONG).show();
                this.isGameOver = true;
            }
            return;
        }
        String msg = String.format("Win for Slayer %d on coords: %s",
                this.lastPlayerValue, winCoords.toString());
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        this.isGameOver = true;
    }

    // TODO: extract game logic in separate class

    // TODO: implement some AI, game is currently only human vs human

    private void resetGame() {
        this.moveCount = 0;
        this.isGameOver = false;
        for (int i = 0; i < this.mAdapter.getItemCount(); i++) {
            this.mAdapter.setValueAt(i, (byte) 0);
        }
    }

    // TODO: implement better algorithm, so to not always check whole grid
    private List<Integer> checkForWin() {
        byte[] grid = this.mAdapter.getGridValues();
        List<Integer> winCoords = this.checkCols(grid);
        if (winCoords != null) {
            return winCoords;
        }
        winCoords = this.checkRows(grid);
        if (winCoords != null) {
            return winCoords;
        }
        winCoords = this.checkDiags(grid);
        if (winCoords != null) {
            return winCoords;
        }
        return null;
    }

    private List<Integer> checkDiags(byte[] grid) {
        List<Integer> winCoords = new ArrayList<>();
        if (grid[4] == 0) {
            return null;
        }
        if (grid[0] == grid[4] && grid[4] == grid[8]) {
            winCoords.add(0);
            winCoords.add(4);
            winCoords.add(8);
        } else if (grid[2] == grid[4] && grid[4] == grid[6]) {
            winCoords.add(2);
            winCoords.add(4);
            winCoords.add(6);
        } else {
            winCoords = null;
        }
        return winCoords;
    }

    private List<Integer> checkRows(byte[] grid) {
        List<Integer> winCoords = new ArrayList<>();

        for (int c = 0; c < 9; c += 3) {
            byte lastVal = grid[c];
            for (int r = 0; r < 3; r++) {
                if (lastVal == 0 || lastVal != grid[c + r]) {
                    winCoords.clear();
                    break;
                }
                else {
                    winCoords.add(c + r);
                }
            }
            if (winCoords.size() == 3) {
                return winCoords;
            }
        }
        return null;
    }

    private List<Integer> checkCols(byte[] grid) {
        List<Integer> winCoords = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            byte lastVal = grid[r];
            for (int c = 0; c < 9; c += 3) {
                if (lastVal == 0 || lastVal != grid[r + c]) {
                    winCoords.clear();
                    break;
                }
                else {
                    winCoords.add(r + c);
                }
            }
            if (winCoords.size() == 3) {
                return winCoords;
            }
        }
        return null;
    }
}
