package com.example.zugzwang.data;

/**
 * Answer from a Firestore call. Firestore works in the background, so every
 * DatabaseService function returns right away and calls one of these later,
 * on the main thread (safe to update the screen from here).
 *
 * @param <T> what the call gives back; Void for calls that only write or delete.
 */
public interface DatabaseCallback<T> {

    void onSuccess(T result);

    void onFailure(Exception e);
}
