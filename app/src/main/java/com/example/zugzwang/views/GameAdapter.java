package com.example.zugzwang.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zugzwang.R;
import com.example.zugzwang.models.GameRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Fills the History list: one item_game row per finished game. */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    public interface OnGameLongClickListener {
        void onGameLongClick(GameRecord game);
    }

    private final List<GameRecord> games = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private OnGameLongClickListener longClickListener;

    public void setGames(List<GameRecord> newGames) {
        games.clear();
        games.addAll(newGames);
        notifyDataSetChanged();
    }

    public void setOnGameLongClickListener(OnGameLongClickListener listener) {
        longClickListener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameRecord game = games.get(position);
        Context context = holder.itemView.getContext();

        // Result chip: text and ice-and-ember colors
        int label, background, foreground;
        if (GameRecord.RESULT_WIN.equals(game.getResult())) {
            label = R.string.result_win_short;
            background = R.color.result_win;
            foreground = R.color.on_result_win;
        } else if (GameRecord.RESULT_LOSS.equals(game.getResult())) {
            label = R.string.result_loss_short;
            background = R.color.result_loss;
            foreground = R.color.on_result_loss;
        } else {
            label = R.string.result_draw_short;
            background = R.color.result_draw;
            foreground = R.color.on_result_draw;
        }
        holder.tvResult.setText(label);
        holder.tvResult.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, background)));
        holder.tvResult.setTextColor(ContextCompat.getColor(context, foreground));

        holder.tvOpponent.setText(GameRecord.MODE_AI.equals(game.getMode())
                ? context.getString(R.string.history_vs_ai, context.getString(levelName(game.getAiLevel())))
                : context.getString(R.string.history_vs_local));

        String moves = context.getResources().getQuantityString(
                R.plurals.move_count, game.getMoveCount(), game.getMoveCount());
        holder.tvDetails.setText(context.getString(R.string.history_details,
                moves, context.getString(reasonName(game.getReason()))));

        // finishedAt is filled in by the server, so it can be missing for a moment right after saving.
        holder.tvDate.setText(game.getFinishedAt() == null ? "" : dateFormat.format(game.getFinishedAt()));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) longClickListener.onGameLongClick(game);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    private static int levelName(int level) {
        if (level == 1) return R.string.level_easy;
        if (level == 3) return R.string.level_hard;
        return R.string.level_medium;
    }

    private static int reasonName(String reason) {
        if (GameRecord.REASON_CHECKMATE.equals(reason)) return R.string.reason_checkmate;
        if (GameRecord.REASON_STALEMATE.equals(reason)) return R.string.reason_stalemate;
        return R.string.reason_resign;
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        final TextView tvResult, tvOpponent, tvDetails, tvDate;

        GameViewHolder(View itemView) {
            super(itemView);
            tvResult = itemView.findViewById(R.id.tvResult);
            tvOpponent = itemView.findViewById(R.id.tvOpponent);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
