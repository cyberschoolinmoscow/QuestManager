package com.projects.questmanager.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.R;
import com.projects.questmanager.adapters.MyAdapter;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;

import java.util.ArrayList;
import java.util.List;

public class MyQuestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton menuButton;
    private RecyclerView recyclerView;

    List<String> questIds = new ArrayList<>();
    private List<QuestInfo> partyNameList = new ArrayList<>();
    private MyAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        setTitle("My quests");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  getResult
                                String userName = document.getData().get("userName").toString();
                                String questID = document.getData().get("questID").toString();
                                String taskAnswer = document.getData().get("taskAnswer").toString();
                                String taskID = document.getData().get("taskID").toString();
                                if (userName.equals(PlayerPreferences.playerName)) {
                                    if (!questIds.contains(questID)) {
                                        questIds.add(questID);
                                    }
                                }
                            }
                        }
                    }
                });

        db.collection("Quests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            partyNameList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  getResult
                                String questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription, usersLimit;

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                questName = document.getData().get("questName").toString();
                                adminName = document.getData().get("adminName").toString();
                                adminPass = document.getData().get("adminPass").toString();
                                userPass = document.getData().get("userPass").toString();
                                usersLimit = document.getData().get("usersLimit").toString();
                                String questID = document.getId().toString();


                                try {
                                    urlImage = document.getData().get("urlImage").toString();


                                } catch (Exception e) {
                                    urlImage = "";

                                }
                                isConfirmedByHQ = document.getData().get("isConfirmedByHQ").toString();
                                questDescription = document.getData().get("questDescription").toString();
                                String questLocation = document.getData().get("questLocation").toString();

                                QuestInfo questInfo = new QuestInfo(questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription, usersLimit, questLocation, questID);

                                MyUtils.updateQuestInfo(questInfo, questID);

                                for (String userInfo : questIds) {

                                    if (userInfo.equals(questID)) {
                                        partyNameList.add(questInfo);
                                    }
                                }


                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter = new MyAdapter(MyQuestActivity.this, partyNameList);
                        recyclerView.setAdapter(adapter);
                    }
                });
        adapter = new MyAdapter(MyQuestActivity.this, partyNameList);
        recyclerView.setAdapter(adapter);
    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(MyQuestActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "My quests":
                        Intent intentMyQuests = new Intent(MyQuestActivity.this, MyQuestActivity.class);
                        startActivity(intentMyQuests);
                        break;

                    case "Create quest":
                        Intent intentCreate = new Intent(MyQuestActivity.this, CreateQuestActivity.class);
                        startActivity(intentCreate);
                        break;
                    case "All quests":
                        Intent intentMain = new Intent(MyQuestActivity.this, MainMenuActivity.class);
                        startActivity(intentMain);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

}
