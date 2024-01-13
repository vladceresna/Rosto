package com.rula.rosto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rula.rosto.adapters.LetterListAdapter;
import com.rula.rosto.obj.Letter;

import java.util.ArrayList;

public class PrimaryActivity extends AppCompatActivity {
    MaterialToolbar topAppBar;
    FloatingActionButton floatbut;
    ArrayList<Letter> letters;
    DatabaseReference reference;
    ListView listView;
    LetterListAdapter adapter;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        floatbut = findViewById(R.id.floatbut);
        topAppBar = findViewById(R.id.topAppBar);
        listView = findViewById(R.id.listView);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        letters = new ArrayList<Letter>();
        reference.child("users").child(user.getUid()).child("id").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String rid = String.valueOf(task.getResult().getValue());
                    reference.child("rosto").child("mailboxs").child(rid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Letter letter = snapshot.getValue(Letter.class);
                            letters.add(letter);
                            adapter =  new LetterListAdapter(PrimaryActivity.this, letters);
                            listView.setAdapter(adapter);
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("getPost", "loadPost:onCancelled", error.toException());
                        }
                    });

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(PrimaryActivity.this, LetterActivity.class)
                        .putExtra("rid",letters.get(i).getId())
                        .putExtra("time",letters.get(i).getTime())
                        .putExtra("title",letters.get(i).getTitle())
                        .putExtra("text",letters.get(i).getText()));
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(PrimaryActivity.this, ProfileActivity.class));
                        return true;
                    case R.id.checkupd:
                        startActivity(new Intent(PrimaryActivity.this,CheckingUpdatesActivity.class));
                        return true;
                    case R.id.abouti:
                        startActivity(new Intent(PrimaryActivity.this,AboutActivity.class));
                        return true;
                    case R.id.exitacc:
                        new MaterialAlertDialogBuilder(PrimaryActivity.this)
                                .setTitle("Really?")
                                .setMessage("Вы выйдете из аккаунта только в этом сервисе. Чтобы все сервисы Rula работали корректно, нужно чтобы во всех них был выполнен вход под одним и тем же аккаунтом. Вы действительно хотите выйти из вашего аккаунта?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        finishAffinity();
                                    }
                                })
                                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {}
                                }).show();
                }
                return false;
            }
        });
        floatbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrimaryActivity.this,WriteActivity.class).setData(Uri.parse("")));
            }
        });
    }
}