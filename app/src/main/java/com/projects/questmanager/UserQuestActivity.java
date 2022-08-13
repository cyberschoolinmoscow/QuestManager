package com.projects.questmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserQuestActivity extends AppCompatActivity {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        private TextView userName;
        private TextView newPartyName;
        private EditText entryPass;
        private Button entranceButton;
        private TextView description;
        private TextView location;
        private ImageView showImage;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_quest);
            setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");

            userName = findViewById(R.id.userName);
            newPartyName = findViewById(R.id.newPartyName);
            entryPass = findViewById(R.id.entryPass);
            entranceButton = findViewById(R.id.joinButton);
            description = findViewById(R.id.questDescription);
            location = findViewById(R.id.questLocation);
            showImage = findViewById(R.id.showImage);

            try {
                Picasso.get().load(PlayerPreferences.currentQuest.getUrlImage()).into(showImage);
            }
            catch (Exception e){

            }
//        userName.setText(PlayerPrefs.playerName);
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            newPartyName.setText(PlayerPreferences.currentQuest.getQuestName());

            entryPass.addTextChangedListener(new TextWatcher() {
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

                        activateButton();
                    } else {

                    }
                }
            });

            entranceButton.setOnClickListener(v-> joinQuest());

        }

        private void joinQuest() {


//        Intent intent=new Intent(CreateQuestActivity.this,TaskManagementActivity.class);
//        startActivity(intent);
        }

        private void activateButton() {

                entranceButton.setEnabled(true);
                entranceButton.setTextColor(Color.WHITE);

        }
    }
