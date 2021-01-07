package com.example.praciceapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ThirdActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private EditText key;
    private EditText text;
    private Button submitButton;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        key=(EditText)findViewById(R.id.third_activity_key);
        text=(EditText)findViewById(R.id.third_activity_text);

        databaseReference=firebaseDatabase.getReference();
//        databaseReference.child("name").child("hello").setValue("Chris");
//        databaseReference.child("test").setValue("lol");

        submitButton=(Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key_text=key.getText().toString();
                String text_text=text.getText().toString();
                databaseReference.child(key_text).setValue(text_text);

            }
        });


        tv=(TextView)findViewById(R.id.textView2);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv.setText(snapshot.getValue().toString());
//                for(DataSnapshot ds:snapshot.getChildren()){
//                    tv.setText(ds.getKey().toString());
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}