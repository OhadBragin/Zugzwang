package com.example.zugzwang.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * One finished game in a player's history, stored at {@code users/{uid}/games/{gameId}}.
 * Each row of the History screen is one GameRecord.
 */
public class GameRecord {

    public static final String MODE_AI = "ai";
    public static final String MODE_LOCAL = "local";

    public static final String RESULT_WIN = "win";
    public static final String RESULT_LOSS = "loss";
    public static final String RESULT_DRAW = "draw";

    public static final String REASON_CHECKMATE = "checkmate";
    public static final String REASON_RESIGN = "resign";
    public static final String REASON_STALEMATE = "stalemate";

    @DocumentId
    private String id;
    /** One of the MODE_ constants. */
    private String mode;
    /** One of the RESULT_ constants, from this player's point of view. */
    private String result;
    /** Why the game ended: one of the REASON_ constants. */
    private String reason;
    private int moveCount;
    /** Computer level (1 easy, 2 medium, 3 hard) for MODE_AI games, 0 otherwise. */
    private int aiLevel;
    @ServerTimestamp
    private Date finishedAt;

    /** Required by Firestore to turn a document back into a GameRecord. */
    public GameRecord() {
    }

    public GameRecord(String mode, String result, String reason, int moveCount, int aiLevel) {
        this.mode = mode;
        this.result = result;
        this.reason = reason;
        this.moveCount = moveCount;
        this.aiLevel = aiLevel;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getMoveCount() { return moveCount; }
    public void setMoveCount(int moveCount) { this.moveCount = moveCount; }

    public int getAiLevel() { return aiLevel; }
    public void setAiLevel(int aiLevel) { this.aiLevel = aiLevel; }

    public Date getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Date finishedAt) { this.finishedAt = finishedAt; }
}
