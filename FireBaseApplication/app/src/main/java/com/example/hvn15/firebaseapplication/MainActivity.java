package com.example.hvn15.firebaseapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button mFirebaseBtn;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseGet;

    private EditText mNameField;
    private EditText mEmailField;
    private TextView mNameView;
    private TextView mGetOneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseBtn = (Button) findViewById(R.id.firebase_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseGet = FirebaseDatabase.getInstance().getReference().child("testing");
        mNameField = (EditText) findViewById(R.id.name_field);
        mEmailField = (EditText) findViewById(R.id.email_field);
        mGetOneView = (TextView) findViewById(R.id.getOne_view);

        mDatabaseGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.getValue().toString();
                    mGetOneView.setText(name);
                } catch(Exception e){mGetOneView.setText("child: " + dataSnapshot.getKey() + " doesn't exist...");}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mGetOneView.setText(databaseError.toString());
            }
        });


        mFirebaseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //create child in root
                //assign value to that child
                String name = mNameField.getText().toString().toString();
                String email = mEmailField.getText().toString().toString();

                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("Name", name);
                dataMap.put("Email", email);
                mDatabase.child(email).setValue(dataMap);
            }
        });
    }

    public void goToAuthenticate(View view){
        Intent intent = new Intent(getBaseContext(), AuthenticationActivity.class);
        startActivity(intent);
    }
}
