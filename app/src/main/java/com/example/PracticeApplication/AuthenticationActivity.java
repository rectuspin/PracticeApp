package com.example.PracticeApplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AuthenticationActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button loginButton;
    private Button signupButton;
    private TextView tv;

    GoogleSignInClient mGoogleSignInClient;

    TextView displayText;
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email=(EditText)findViewById(R.id.emailAddress);
        password=(EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);
        signupButton = (Button)findViewById(R.id.signup);
        tv=(TextView)findViewById(R.id.display_text);

        signInButton = findViewById(R.id.sign_in_button);

        displayText=(TextView)findViewById(R.id.display_text);

        mAuth=FirebaseAuth.getInstance();


///////////////////////////////////////Google Sign in
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("894007355671-7ptkra78hhdognsnr18jnf21tab5ud84.apps.googleusercontent.com").requestEmail().build();


        // Build a GoogleSignInClient with the options specified by gso.
       mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
/////////////////////////////////


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString());
                tv.setText(email.getText().toString());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString());
                FirebaseUser currentUser=mAuth.getCurrentUser();
                if(currentUser!=null) {
                    tv.setText(email.getText().toString());
                    Intent intent=new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }//onCreate ends

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
            Toast.makeText(AuthenticationActivity.this, currentUser.getDisplayName(),Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


            }

    }
    @Override
    protected void onPause() {
        super.onPause();

//        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("Login", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("Login", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        // ...
//                    }
//                });
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if(account!=null)
                displayText.setText("Loser");
            else
                displayText.setText("Loser");

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("AUTH", "Google Auth Failed!! \nsignInResult:failed code=" + e.getStatusCode());

        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                                Toast.makeText(AuthenticationActivity.this, user.getDisplayName(),Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH", "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




}