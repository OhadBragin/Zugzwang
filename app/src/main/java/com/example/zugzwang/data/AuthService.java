package com.example.zugzwang.data;

import android.content.Context;

import com.example.zugzwang.R;
import com.example.zugzwang.models.User;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

/** Sign up, log in and log out with Firebase Authentication (email and password). */
public final class AuthService {

    private AuthService() {
    }

    private static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    /** True if someone is logged in on this phone. Firebase remembers the login between app launches. */
    public static boolean isLoggedIn() {
        return auth().getCurrentUser() != null;
    }

    /** The logged-in player's uid, or null if nobody is logged in. */
    public static String currentUid() {
        FirebaseUser user = auth().getCurrentUser();
        return user == null ? null : user.getUid();
    }

    /** The logged-in player's email, or null if nobody is logged in. */
    public static String currentEmail() {
        FirebaseUser user = auth().getCurrentUser();
        return user == null ? null : user.getEmail();
    }

    /**
     * Creates the account and saves the player's profile in Firestore.
     * If the profile can't be saved, the account is removed again so the player can simply retry.
     */
    public static void signUp(String email, String password, String username, DatabaseCallback<User> callback) {
        auth().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    User user = new User(firebaseUser.getUid(), username, email);
                    DatabaseService.createUser(user, new DatabaseCallback<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onSuccess(user);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            firebaseUser.delete();
                            callback.onFailure(e);
                        }
                    });
                })
                .addOnFailureListener(callback::onFailure);
    }

    public static void logIn(String email, String password, DatabaseCallback<Void> callback) {
        auth().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public static void logOut() {
        auth().signOut();
    }

    /** Turns a Firebase error into a short message that can be shown to the player. */
    public static String errorMessage(Context context, Exception e) {
        if (e instanceof FirebaseNetworkException) {
            return context.getString(R.string.error_network);
        }
        if (e instanceof FirebaseAuthUserCollisionException) {
            return context.getString(R.string.error_email_taken);
        }
        if (e instanceof FirebaseAuthWeakPasswordException) {
            return context.getString(R.string.error_password_short);
        }
        if (e instanceof FirebaseAuthInvalidUserException
                || e instanceof FirebaseAuthInvalidCredentialsException) {
            return context.getString(R.string.error_wrong_login);
        }
        return context.getString(R.string.error_generic);
    }
}
