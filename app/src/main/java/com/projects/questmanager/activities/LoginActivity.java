package com.projects.questmanager.activities;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.projects.questmanager.utils.PlayerPreferences;
import com.projects.questmanager.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    Button btn_login, btn_logout,go_btn;
    TextView text;
    ImageView image;
    ProgressBar progressBar,progressBar2;
    GoogleSignInClient mGoogleSignInClient;
    private Button btnStart;
    private Handler h;
    private boolean isStarted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.login_btn);
        btn_logout = findViewById(R.id.logout_btn);
        go_btn = findViewById(R.id.login_btn2);

        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        progressBar = findViewById(R.id.progress_circular);
        progressBar2 = findViewById(R.id.progressBar);


        btnStart = (Button) findViewById(R.id.btnStart);
        Context context=this;
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView
                progressBar2.setProgress(msg.what);
                if (msg.what == 100) {
//                   btnStart.setEnabled(true);
                    isStarted=true;
                    progressBar2.setVisibility(View.INVISIBLE);
                }


                if(isStarted) {
                    isStarted=false;
                    progressBar.setVisibility(View.INVISIBLE);
                    mAuth = FirebaseAuth.getInstance();

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("1098344294693-0qpr4j0bpvacu5d3nepuefv72q0ahrr4.apps.googleusercontent.com")
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

                    btn_login.setOnClickListener(v -> SignInGoogle());
                    btn_logout.setOnClickListener(v -> Logout());
                    go_btn.setOnClickListener(v -> GoNext());

                    if (mAuth.getCurrentUser() != null) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                }
            };
        };
//
//if(isStarted) {
//    progressBar.setVisibility(View.INVISIBLE);
//    mAuth = FirebaseAuth.getInstance();
//
//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("1098344294693-0qpr4j0bpvacu5d3nepuefv72q0ahrr4.apps.googleusercontent.com")
//            .requestEmail()
//            .build();
   // mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//    Context context=this;
 //   mGoogleSignInClient = GoogleSignIn.getClient(context,
//
//    btn_login.setOnClickListener(v -> SignInGoogle());
//    btn_logout.setOnClickListener(v -> Logout());
//    go_btn.setOnClickListener(v -> GoNext());
//
//    if (mAuth.getCurrentUser() != null) {
//        FirebaseUser user = mAuth.getCurrentUser();
//        updateUI(user);
//    }
//}
    }
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                btnStart.setVisibility(View.INVISIBLE);


                Thread t = new Thread(new Runnable() {
                    public void run() {
                        for (int i = 1; i <= 100; i++) {
                            // долгий процесс
                            downloadFile();
                            h.sendEmptyMessage(i);
                            // пишем лог
                            Log.d("TAG", "i = " + i);
                        }
                    }
                });
                t.start();
                break;

            default:
                break;
        }
    }
    void downloadFile() {
        // пауза - 1 секунда
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void GoNext() {
        Intent intent=new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        //? registerForActivityResult(signInIntent,GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
//                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d("TAG", "signInWithCredential:success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
//                        progressBar.setVisibility(View.INVISIBLE);

                        Log.w("TAG", "signInWithCredential:failure", task.getException());

//                        Toast.makeText(MainActivity.this, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);    Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        //progressBar.setVisibility(View.INVISIBLE);
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

            text.append("Info : \n");
            text.append(name + "\n");
            text.append(email);
//            Picasso.get().load(photo).into(image);
            btn_logout.setVisibility(View.VISIBLE);
            go_btn.setVisibility(View.VISIBLE);

            btn_login.setVisibility(View.INVISIBLE);

            PlayerPreferences.playerName=name;


        } else {
            text.setText("Firebase Login \n");
//            Picasso.get().load(R.drawable.ic_firebase_logo).into(image);
            btn_logout.setVisibility(View.INVISIBLE);

            go_btn.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.VISIBLE);  Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }
}