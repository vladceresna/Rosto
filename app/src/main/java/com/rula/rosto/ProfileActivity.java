package com.rula.rosto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    boolean changename;
    boolean changemail;
    boolean changerid;

    TextView name,id,mail;

    LinearLayout lchangename;
    ImageButton butstartchangename, butconfchangename;
    TextView tname, tlname;
    EditText echangename;
    TextView alertsname;

    LinearLayout lchangemail;
    ImageButton butstartchangemail, butconfchangemail;
    TextView tmail, tlmail;
    EditText echangemail;
    TextView alertsmail;

    LinearLayout lchangerid;
    ImageButton butstartchangerid, butconfchangerid;
    TextView trid, tlrid;
    EditText echangerid;
    TextView alertsrid;

    TextView butexit, butdel;

    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        id = findViewById(R.id.id);

        lchangename = findViewById(R.id.lchangename);
        butstartchangename = findViewById(R.id.butstartchangename);
        butconfchangename = findViewById(R.id.butconfchangename);
        tname = findViewById(R.id.tname);
        tlname = findViewById(R.id.tlname);
        echangename = findViewById(R.id.echangename);
        alertsname = findViewById(R.id.alertsname);

        lchangemail = findViewById(R.id.lchangemail);
        butstartchangemail = findViewById(R.id.butstartchangemail);
        butconfchangemail = findViewById(R.id.butconfchangemail);
        tmail = findViewById(R.id.tmail);
        tlmail = findViewById(R.id.tlmail);
        echangemail = findViewById(R.id.echangemail);
        alertsmail = findViewById(R.id.alertsmail);

        lchangerid = findViewById(R.id.lchangerid);
        butstartchangerid = findViewById(R.id.butstartchangerid);
        butconfchangerid = findViewById(R.id.butconfchangerid);
        trid = findViewById(R.id.trid);
        tlrid = findViewById(R.id.tlrid);
        echangerid = findViewById(R.id.echangerid);
        alertsrid = findViewById(R.id.alertsrid);

        butexit = findViewById(R.id.butexit);
        butdel = findViewById(R.id.butdel);

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        tname.setText(user.getDisplayName());
        name.setText(user.getDisplayName());
        echangename.setText(user.getDisplayName());
        tmail.setText(user.getEmail());
        mail.setText(user.getEmail());
        echangemail.setText(user.getEmail());
        reference.child("users").child(user.getUid()).child("id").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            trid.setText(String.valueOf(task.getResult().getValue()));
                            id.setText(String.valueOf(task.getResult().getValue()));
                            echangerid.setText(String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
        
        butstartchangename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butstartchangename.setVisibility(View.GONE);
                tname.setVisibility(View.GONE);
                tlname.setVisibility(View.GONE);

                lchangename.setVisibility(View.VISIBLE);

                changename = true;

            }
        });
        butconfchangename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference.child("users").child(user.getUid()).child("name").setValue(echangename.getText().toString());
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(echangename.getText().toString()).build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("firebase", "User profile updated.");
                                    Toast.makeText(ProfileActivity.this, "Готово", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                tname.setText(echangename.getText());

                butstartchangename.setVisibility(View.VISIBLE);
                tname.setVisibility(View.VISIBLE);
                tlname.setVisibility(View.VISIBLE);

                lchangename.setVisibility(View.GONE);

                changename = false;

            }
        });

        butstartchangemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butstartchangemail.setVisibility(View.GONE);
                tmail.setVisibility(View.GONE);
                tlmail.setVisibility(View.GONE);

                lchangemail.setVisibility(View.VISIBLE);

                changemail = true;

            }
        });
        butconfchangemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);

                alert.setTitle("Подтверждение");
                alert.setMessage("Сначала подтвердите что это ваш аккаунт. Ввведите пароль:");

                // Set an EditText view to get user input
                final EditText input = new EditText(ProfileActivity.this);
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        // Get auth credentials from the user for re-authentication
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), input.getText().toString()); // Current Login Credentials \\
                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("firebase", "User re-authenticated.");
                                        //Now change your email address
                                        //Code for Changing Email Address
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.updateEmail(echangemail.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("firebase", "User email address updated.");
                                                            Toast.makeText(ProfileActivity.this, "Готово", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                        tmail.setText(echangemail.getText());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

                butstartchangemail.setVisibility(View.VISIBLE);
                tmail.setVisibility(View.VISIBLE);
                tlmail.setVisibility(View.VISIBLE);

                lchangemail.setVisibility(View.GONE);

                changemail = false;

            }
        });

        butstartchangerid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butstartchangerid.setVisibility(View.GONE);
                trid.setVisibility(View.GONE);
                tlrid.setVisibility(View.GONE);

                lchangerid.setVisibility(View.VISIBLE);

                changerid = true;

            }
        });
        butconfchangerid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Really?")
                        /**.setMessage("Поиск и изменение подписей всех ваших действий может занять очень много времени, вы согласны изменить RulaID сейчас?")**/
                        .setMessage("Если вы поменяете свой RulaID, то никакие ссылки на ваш аккаунт не сохранятся. Вы начнете работать как будто на новом аккаунте. Вы готовы?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                reference.child("users").child(user.getUid()).child("id").setValue(echangerid.getText().toString());

                                trid.setText(echangerid.getText());

                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .show();



                butstartchangerid.setVisibility(View.VISIBLE);
                trid.setVisibility(View.VISIBLE);
                tlrid.setVisibility(View.VISIBLE);

                lchangerid.setVisibility(View.GONE);

                changerid = false;

            }
        });

        butexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
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
        });
        butdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Really?")
                        .setMessage("Вы действительно хотите удалить ваш аккаунт навсегда?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                reference.child("users").child(user.getUid()).removeValue();
                                user.delete();
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        }).show();

            }
        });

    }

    @Override
    public void onBackPressed(){
        if(changename){
            butstartchangename.setVisibility(View.VISIBLE);
            tname.setVisibility(View.VISIBLE);
            tlname.setVisibility(View.VISIBLE);

            lchangename.setVisibility(View.GONE);

            changename = false;
        }else if(changemail){
            butstartchangemail.setVisibility(View.VISIBLE);
            tmail.setVisibility(View.VISIBLE);
            tlmail.setVisibility(View.VISIBLE);

            lchangemail.setVisibility(View.GONE);

            changemail = false;
        }else if(changerid){
            butstartchangerid.setVisibility(View.VISIBLE);
            trid.setVisibility(View.VISIBLE);
            tlrid.setVisibility(View.VISIBLE);

            lchangerid.setVisibility(View.GONE);

            changerid = false;
        }else{
            super.onBackPressed();
        }
    }
}