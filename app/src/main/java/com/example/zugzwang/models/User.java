package com.example.zugzwang.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * A player's profile, stored in Firestore at {@code users/{uid}}.
 * The document id is the Firebase Auth uid, so each account has exactly one profile.
 */
public class User {

    @DocumentId
    private String uid;
    private String username;
    private String email;
    private int wins;
    private int losses;
    private int draws;
    @ServerTimestamp
    private Date createdAt;

    /** Required by Firestore to turn a document back into a User. */
    public User() {
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    @Exclude // worked out from the counters, not stored in Firestore
    public int getGamesPlayed() {
        return wins + losses + draws;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
