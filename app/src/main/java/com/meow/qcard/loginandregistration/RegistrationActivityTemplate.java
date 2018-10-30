package com.meow.qcard.loginandregistration;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.meow.qcard.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Used as a template for any login and registration based activities
 * Any class involving user authentication changes should extend this.
 * TODO turn from activity to fragment
 * TODO figure out how to login with google account
 */
public abstract class RegistrationActivityTemplate extends AppCompatActivity {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    FirebaseAuth mAuth;

    // UI references
    private View mProgressView;
    private View mFormView;
    AutoCompleteTextView mEmailView;

    /**
     * Initialize variables and email autocomplete after setting content view
     *
     * @param formViewId     form view id
     * @param progressViewId progress view id
     */
    void initialize(int formViewId, int progressViewId) {
        // Get FireBase auth instance
        mAuth = FirebaseAuth.getInstance();

        mEmailView = findViewById(R.id.email);
        mFormView = findViewById(formViewId);
        mProgressView = findViewById(progressViewId);

        mEmailView.setAdapter(getEmailsToAutoComplete());

        // Set mEmailView text if already available
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEmailView.setText(bundle.getString("email", ""));
        }
    }

    /**
     * Returns adapter to tell AutoCompleteTextView what to show in its dropdown list
     *
     * @return adapter with emails
     */
    private ArrayAdapter<String> getEmailsToAutoComplete() {
        Account[] accounts = AccountManager.get(this).getAccounts();
        Set<String> emailSet = new HashSet<>();
        for (Account account : accounts)
            if (EMAIL_PATTERN.matcher(account.name).matches())
                emailSet.add(account.name);

        return new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(emailSet));
    }

    boolean isInvalidEmail(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    boolean isValidDisplayName(String displayName) {
        return displayName.length() >= 3 && displayName.length() <= 16;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
