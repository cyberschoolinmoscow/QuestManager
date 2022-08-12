package com.projects.questmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class CreateQuestActivity extends AppCompatActivity {

    private TextView userName;
    private EditText newPartyName;
    private EditText setEntryPass;
    private EditText setAdminPass;
    private EditText setLimit;
    private Button creationConfirm;
    Editable setLimitVal;
    boolean a, b, c;
    int limit;
//    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");

        userName = findViewById(R.id.userName);
        newPartyName = findViewById(R.id.newPartyName);
        setEntryPass = findViewById(R.id.setEntryPass);
        setAdminPass = findViewById(R.id.setAdminPass);
        setLimit = findViewById(R.id.setLimit);
        creationConfirm = findViewById(R.id.creationConfirm);
//        userName.setText(PlayerPrefs.playerName);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        newPartyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 3) {
                    a = true;
                    activateButton();
                } else {
                    a = false;
                }
            }
        });

        setEntryPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 3) {
                    b = true;
                    activateButton();
                } else {
                    b = false;
                }
            }
        });
        setLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                setLimitVal = setLimit.getText();
                try {
                    limit = Integer.parseInt(String.valueOf(setLimitVal));
                }
                catch (NumberFormatException e) {
                    limit = 0;
                }

                if (limit >1) {
                    c = true;
                    activateButton();
                } else {
                    c = false;
                }
            }
        });

        creationConfirm.setOnClickListener(v->CreateParty());
    }
//    FirebaseDatabase database;

    private void CreateParty() {

//        PlayerPrefs.partyName=newPartyName.getText().toString();
//        PlayerPrefs.admin=PlayerPrefs.playerName;
//        PlayerPrefs.password=setEntryPass.getText().toString();
//        PlayerPrefs.adminPassword=setAdminPass.getText().toString();
//        PlayerPrefs.limit=Integer.parseInt(setLimit.getText().toString());
//
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.partyName);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.admin);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.password);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.adminPassword);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.limit.toString());
//
//        database=FirebaseDatabase.getInstance("https://multiplayergame-996aa-default-rtdb.europe-west1.firebasedatabase.app/");
//        roomRef=database.getReference().child("rooms");
//        roomRef.child(PlayerPrefs.partyName).child("admin").setValue(PlayerPrefs.admin);
//        roomRef.child(PlayerPrefs.partyName).child("password").setValue(PlayerPrefs.password);
//        roomRef.child(PlayerPrefs.partyName).child("adminPassword").setValue(PlayerPrefs.adminPassword);
//        roomRef.child(PlayerPrefs.partyName).child("limit").setValue(PlayerPrefs.limit);

        Intent intent=new Intent(CreateQuestActivity.this,TaskManagementActivity.class);
        startActivity(intent);
    }

    private void activateButton() {
        if (a && b && c) {
            creationConfirm.setEnabled(true);
            creationConfirm.setTextColor(Color.WHITE);
        }
        else {
            creationConfirm.setEnabled(false);
            creationConfirm.setTextColor(Color.GRAY);
        }
    }
}