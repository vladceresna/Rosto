package com.rula.rosto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    TextView name, id, brosto;
    LinearLayout message;
    ScrollView view;
    DatabaseReference reference;
    boolean searchres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        brosto = findViewById(R.id.brosto);
        message = findViewById(R.id.message);
        view = findViewById(R.id.view);

        reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchres = false;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.child("id").getValue().equals(getIntent().getStringExtra("id"))){
                        name.setText(snapshot1.child("name").getValue().toString());
                        searchres = true;
                    }
                }
                if (!searchres){
                    view.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "Error getting snapshot");
            }
        });
        id.setText(getIntent().getStringExtra("id"));

        brosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setData(Uri.parse(getIntent().getStringExtra("id"))).setClassName("com.rula.rosto","com.rula.rosto.WriteActivity"));
            }
        });



    }

}