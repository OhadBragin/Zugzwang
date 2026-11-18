package com.example.zugzwang.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zugzwang.R;
import com.example.zugzwang.data.AuthService;
import com.example.zugzwang.data.DatabaseCallback;
import com.example.zugzwang.data.DatabaseService;
import com.example.zugzwang.models.GameRecord;
import com.example.zugzwang.views.GameAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final GameAdapter adapter = new GameAdapter();
    private MaterialToolbar toolbar;
    private View layoutEmpty;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        uid = AuthService.currentUid();
        layoutEmpty = findViewById(R.id.layoutEmpty);
        layoutEmpty.setVisibility(View.GONE); // until we know the list is empty

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.menu_history);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.actionClearHistory) {
                confirmClear();
                return true;
            }
            return false;
        });

        RecyclerView rvGames = findViewById(R.id.rvGames);
        rvGames.setAdapter(adapter);
        adapter.setOnGameLongClickListener(this::confirmDelete);

        loadGames();
    }

    private void loadGames() {
        DatabaseService.getGames(uid, new DatabaseCallback<List<GameRecord>>() {
            @Override
            public void onSuccess(List<GameRecord> games) {
                adapter.setGames(games);
                layoutEmpty.setVisibility(games.isEmpty() ? View.VISIBLE : View.GONE);
                toolbar.setSubtitle(games.isEmpty() ? null : getString(R.string.history_hint));
            }

            @Override
            public void onFailure(Exception e) {
                layoutEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(HistoryActivity.this, R.string.error_generic, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmDelete(GameRecord game) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.history_delete_title)
                .setMessage(R.string.history_delete_message)
                .setPositiveButton(R.string.delete, (d, w) ->
                        DatabaseService.deleteGame(uid, game, reloadAfterChange()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmClear() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.history_clear_title)
                .setMessage(R.string.history_clear_message)
                .setPositiveButton(R.string.clear, (d, w) ->
                        DatabaseService.clearGames(uid, reloadAfterChange()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private DatabaseCallback<Void> reloadAfterChange() {
        return new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loadGames();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(HistoryActivity.this, R.string.error_generic, Toast.LENGTH_LONG).show();
            }
        };
    }
}
