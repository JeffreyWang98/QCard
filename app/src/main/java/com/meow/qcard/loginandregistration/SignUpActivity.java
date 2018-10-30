package com.meow.qcard.loginandregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.meow.qcard.MainActivity;
import com.meow.qcard.R;

import java.util.Objects;

/**
 * A login screen that offers login via email/password or google login.
 */
public class SignUpActivity extends RegistrationActivityTemplate {

    // UI references.
    private EditText mDisplayNameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize(R.id.sign_up_form, R.id.sign_up_progress);

        // Set up the login form.
        mDisplayNameView = findViewById(R.id.display_name);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        Button resetPasswordButton = findViewById(R.id.btn_reset_password);
        resetPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, ResetPasswordActivity.class);
                Bundle args = new Bundle();
                args.putString("email", mEmailView.getText().toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        Button signInButton = findViewById(R.id.btn_login);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                Bundle args = new Bundle();
                args.putString("email", mEmailView.getText().toString());
                intent.putExtras(args);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        // Reset errors.
        mDisplayNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String displayName = mDisplayNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid display name
        if (TextUtils.isEmpty(displayName)) {
            mDisplayNameView.setError(getString(R.string.error_field_required));
            focusView = mDisplayNameView;
            cancel = true;
        } else if (!isValidDisplayName(displayName)) {
            mDisplayNameView.setError(getString(R.string.error_invalid_display_name));
            focusView = mDisplayNameView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isValidPassword(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (isInvalidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    showProgress(false);

                    if (!task.isSuccessful()) {
                        //Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, "Registration failed. This email may already be associated with an account.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(new UserProfileChangeRequest.Builder().setDisplayName("meow").build());
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }
    }
}
