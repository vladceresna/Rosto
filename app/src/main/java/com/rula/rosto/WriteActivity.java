package com.rula.rosto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rula.rosto.obj.Letter;
import com.rula.rosto.obj.User;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference reference;
    Button send;
    EditText rulaidsedit, titleedit, lettertextedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        send = findViewById(R.id.send);
        rulaidsedit = findViewById(R.id.rulaidsedit);
        titleedit = findViewById(R.id.titleedit);
        lettertextedit = findViewById(R.id.lettertextedit);

        rulaidsedit.setText(getIntent().getData().toString());

        try {
            titleedit.setText(getIntent().getStringExtra("title"));
            lettertextedit.setText(getIntent().getStringExtra("text"));
        }catch (Exception e){
            Log.d("transport", e.getMessage());
            e.printStackTrace();
        }

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rulaidsedit.getText().toString().equals("")){
                    Toast.makeText(WriteActivity.this, "А кому же отправить?", Toast.LENGTH_SHORT).show();
                }else if(titleedit.getText().toString().equals("")){
                    Toast.makeText(WriteActivity.this, "А с каким заголовком отправить?", Toast.LENGTH_SHORT).show();
                }else if (lettertextedit.getText().toString().equals("")){
                    Toast.makeText(WriteActivity.this, "Вы серьёзно? Как сказал один человек, письмо без текста - это уже не письмо. Пожалуйста, введите текст", Toast.LENGTH_SHORT).show();
                }else {
                    reference.child("users").child(user.getUid()).child("id").get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        String rid = String.valueOf(task.getResult().getValue());
                                        String[] arrayrid = rulaidsedit.getText().toString().split(",");
                                        for (String s : arrayrid) {
                                            reference.child("rosto").child("mailboxs").child(s).push()
                                                    .setValue(new Letter(rid, titleedit.getText().toString(), lettertextedit.getText().toString(), System.currentTimeMillis()));
                                        }
                                        finish();
                                    }
                                }
                            });
                }


            }
        });
    }
}