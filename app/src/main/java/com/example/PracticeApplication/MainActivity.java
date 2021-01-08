package com.example.PracticeApplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //define any pre-variable here
    private Button mButton =null;
    private FloatingActionButton buttonFirebase=null;
    private Context context;
    TextView readingView;
    TextView appRestartView;

    private int numOfRun=0;
    private static final String numOfRunsKey="Number_Of_Times_Run_Key";
    private int defaultVal=0;

    private static final String DATA_FILE="my_saved_data";

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private TextView userinfo;
    ////////above is defining variable part
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       context= getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton first_button = findViewById(R.id.first_button);
        Dialog firstDialog=new Dialog(this);

        buttonFirebase=findViewById(R.id.go_to_third_activity);


        userinfo=(TextView)findViewById(R.id.userInfo);

        SwitchCompat toWebView=findViewById(R.id.to_web_view);

//        WebView myWebView = new WebView(this);


        ////notepad exercise starts
        readingView=(TextView) findViewById(R.id.displayView);
        appRestartView=(TextView) findViewById(R.id.appRestarts);

        SharedPreferences sharedPreferences=getPreferences(Context.MODE_PRIVATE);

        //read
        numOfRun=sharedPreferences.getInt(numOfRunsKey,defaultVal);
        //first time
        if(numOfRun++==0){
            Toast.makeText(this,"Welcome!",Toast.LENGTH_LONG).show();
        }

        //write
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(numOfRunsKey,numOfRun);
//        editor.commit();
        editor.apply();

        appRestartView.setText(String.valueOf(numOfRun));
        //notepad exercise ends


        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "first action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                firstDialog.setContentView(getLayoutInflater().inflate(R.layout.dummy,null));
//                firstDialog.show();


                Intent intent=new Intent(context,AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        toWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openSecondActivity();
//                setContentView(myWebView);
//                myWebView.loadUrl("https://www.naver.com");
            }
        });

        buttonFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ThirdActivity.class);
                startActivity(intent);
            }
        });
    }  //    onCreate ends //////////////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(currentUser!=null)
            userinfo.setText(currentUser.getDisplayName());
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        else if(account!=null)
            userinfo.setText(account.getDisplayName());

    }


//onPause starts////
    @Override
    protected void onPause() {
        super.onPause();
        saveTextFile(readingView.getText().toString());
    }
//onPause ends///
//    onResume starts//////

    @Override
    protected void onResume() {
        super.onResume();
        readingView.setText(getTextFile());
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser!=null)
            userinfo.setText(currentUser.getUid());
    }
//    onResume ends/////

    public void saveTextFile(String content){
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=openFileOutput(DATA_FILE,Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());
        } catch(FileNotFoundException e)  {
            Log.e("FILE","Couldn't find the file");
            e.printStackTrace();
        } catch (IOException e){
            Log.e("FILE","IO ERROR");
            e.printStackTrace();
        }finally {
            try {
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public String getTextFile(){
        FileInputStream fileInputStream=null;
        String fileData=null;
        try {
            fileInputStream=openFileInput(DATA_FILE);
            int size=fileInputStream.available();
            byte[] buffer=new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            fileData=new String(buffer,"UTF-8");

        } catch(FileNotFoundException e)  {
            Log.e("FILE","Couldn't find the file");
            e.printStackTrace();
        } catch (IOException e){
            Log.e("FILE","IO ERROR");
            e.printStackTrace();
        }finally {
            try {
                if(fileInputStream!=null){
                    fileInputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return fileData;
    }
    public void openSecondActivity(){
        Intent intent=new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}