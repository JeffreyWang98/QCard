package com.meow.qcard.loginandregistration;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.meow.qcard.R;

public class ResetPasswordActivity extends RegistrationActivityTemplate {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Get FireBase auth instance
        mAuth = FirebaseAuth.getInstance();

        mEmailView = findViewById(R.id.email);
        populateAutoComplete();

        // Set mEmailView text if already available
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEmailView.setText(bundle.getString("email", ""));
        }

        Button resetButton = findViewById(R.id.btn_reset_password);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset errors.
                mEmailView.setError(null);

                // Store values at time of reset attempt
                String email = mEmailView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for valid email address.
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
                    // Email was not valid, don't attempt login and focus
                    focusView.requestFocus();
                } else {
                    // Show progress spinner and kick off background task to
                    // perform reset password attempt;
                    showProgress(true);

                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showProgress(false);
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Please check your email for instructions on how to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initProgressView(findViewById(R.id.reset_password_progress));
        initFormView(findViewById(R.id.reset_password_form));
    }
}
