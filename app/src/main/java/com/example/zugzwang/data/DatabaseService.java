package com.example.zugzwang.data;

import com.example.zugzwang.models.GameRecord;
import com.example.zugzwang.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Every read, write and delete the app does in Firestore goes through here, so the
 * screens never touch Firestore directly.
 *
 * <pre>
 * users/{uid}                 a player's profile and win/loss/draw counters  (User)
 * users/{uid}/games/{gameId}  that player's finished games                    (GameRecord)
 * </pre>
 *
 * All functions return immediately; the answer arrives later through the callback.
 */
public final class DatabaseService {

    private static final String USERS = "users";
    private static final String GAMES = "games";

    /** Firestore allows at most 500 writes in one batch; stay a little under. */
    private static final int BATCH_LIMIT = 450;

    private DatabaseService() {
    }

    private static FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }

    private static DocumentReference userRef(String uid) {
        return db().collection(USERS).document(uid);
    }

    private static CollectionReference gamesRef(String uid) {
        return userRef(uid).collection(GAMES);
    }

    // ---------------------------------------------------------------- profile

    /** Saves a new profile with zero wins, losses and draws. Called by AuthService.signUp. */
    public static void createUser(User user, DatabaseCallback<Void> callback) {
        run(userRef(user.getUid()).set(user), callback);
    }

    /** Gets a profile. Gives null if there is no profile for this uid. */
    public static void getUser(String uid, DatabaseCallback<User> callback) {
        userRef(uid).get()
                .addOnSuccessListener(doc -> callback.onSuccess(doc.exists() ? doc.toObject(User.class) : null))
                .addOnFailureListener(callback::onFailure);
    }

    public static void updateUsername(String uid, String username, DatabaseCallback<Void> callback) {
        run(userRef(uid).update("username", username), callback);
    }

    // ---------------------------------------------------------------- game history

    /**
     * Adds a finished game to the player's history and adds 1 to their wins, losses or
     * draws, both in one write so the history and the counters can never disagree.
     */
    public static void saveGame(String uid, GameRecord game, DatabaseCallback<Void> callback) {
        WriteBatch batch = db().batch();
        batch.set(gamesRef(uid).document(), game);
        batch.update(userRef(uid), counterFor(game.getResult()), FieldValue.increment(1));
        run(batch.commit(), callback);
    }

    /** Gets the player's finished games, newest first. Gives an empty list if there are none. */
    public static void getGames(String uid, DatabaseCallback<List<GameRecord>> callback) {
        gamesRef(uid).orderBy("finishedAt", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(snapshot -> {
                    List<GameRecord> games = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        games.add(doc.toObject(GameRecord.class));
                    }
                    callback.onSuccess(games);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** Removes one game from the history and takes 1 off the matching counter. */
    public static void deleteGame(String uid, GameRecord game, DatabaseCallback<Void> callback) {
        WriteBatch batch = db().batch();
        batch.delete(gamesRef(uid).document(game.getId()));
        batch.update(userRef(uid), counterFor(game.getResult()), FieldValue.increment(-1));
        run(batch.commit(), callback);
    }

    /** Deletes the player's whole history and sets wins, losses and draws back to zero. */
    public static void clearGames(String uid, DatabaseCallback<Void> callback) {
        gamesRef(uid).get()
                .addOnSuccessListener(snapshot -> {
                    List<Task<Void>> commits = new ArrayList<>();
                    WriteBatch batch = db().batch();
                    int inBatch = 0;
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        batch.delete(doc.getReference());
                        if (++inBatch == BATCH_LIMIT) {
                            commits.add(batch.commit());
                            batch = db().batch();
                            inBatch = 0;
                        }
                    }
                    Map<String, Object> zero = new HashMap<>();
                    zero.put("wins", 0);
                    zero.put("losses", 0);
                    zero.put("draws", 0);
                    batch.update(userRef(uid), zero);
                    commits.add(batch.commit());
                    run(Tasks.whenAll(commits), callback);
                })
                .addOnFailureListener(callback::onFailure);
    }

    /** The User field that counts games with this result. */
    private static String counterFor(String result) {
        if (GameRecord.RESULT_WIN.equals(result)) return "wins";
        if (GameRecord.RESULT_LOSS.equals(result)) return "losses";
        if (GameRecord.RESULT_DRAW.equals(result)) return "draws";
        throw new IllegalArgumentException("Unknown game result: " + result);
    }

    /** Reports a write or delete that has no result of its own. */
    private static void run(Task<Void> task, DatabaseCallback<Void> callback) {
        task.addOnSuccessListener(callback::onSuccess).addOnFailureListener(callback::onFailure);
    }
}
