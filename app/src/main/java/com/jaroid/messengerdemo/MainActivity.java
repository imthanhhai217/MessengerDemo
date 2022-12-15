package com.jaroid.messengerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.edtUserName)
    EditText edtUserName;
    @BindView(R.id.edtPasswords)
    EditText edtPasswords;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.tvRegister)
    TextView tvRegister;


    private FirebaseAuth mAuth;
    private boolean isSignIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //go to chating
            goToChattingActivity(currentUser);
        }
    }

    private void initView() {
        ButterKnife.bind(this);

        tvRegister.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                if (isSignIn) {
                    showRegister();
                } else {
                    showSignUp();
                }
                break;
            case R.id.btnSignIn:
                signIn();
                break;
            case R.id.btnRegister:
                registerAccount();
                break;
        }
    }

    private void signIn() {
        String email = edtUserName.getText().toString();
        String password = edtPasswords.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                goToChattingActivity(user);
            }
        }).addOnFailureListener(e -> Toast.makeText(MainActivity.this,
                "ERROR : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void goToChattingActivity(FirebaseUser user) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra(Global.USER_KEY, user);
        startActivity(intent);
    }

    private void registerAccount() {
        String email = edtUserName.getText().toString();
        String password = edtPasswords.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                edtUserName.setText("");
                edtPasswords.setText("");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
                Toast.makeText(this, "Register success !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " ERROR : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                ;
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "registerAccount: " + e.getMessage());
        });
    }

    private void updateUI(FirebaseUser user) {
        edtUserName.setText(user.getEmail());
    }

    private void showSignUp() {
        isSignIn = true;
        btnSignIn.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);

        tvRegister.setText(getResources().getString(R.string.text_register));
    }

    private void showRegister() {
        isSignIn = false;
        btnSignIn.setVisibility(View.GONE);
        btnRegister.setVisibility(View.VISIBLE);

        tvRegister.setText(getResources().getString(R.string.text_sign_in));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}