package com.projects.questmanager;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton menuButton;
    private RecyclerView recyclerView;

    private List<QuestInfo> partyNameList = new ArrayList<>();
    private MyAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private String questLocation;

    //todo:create class
//    String questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription,usersLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        setTitle("");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        questName=newPartyName.getText().toString();
        //todo: create static fields final
//        adminName=(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        userPass=setEntryPass.getText().toString();
//        adminPass=setAdminPass.getText().toString();
//        usersLimit=Integer.parseInt(setLimit.getText().toString());
//        questDescription=description.getText().toString();
//
        // Create a new quest with a first and last name
        Map<String, Object> quest = new HashMap<>();
//        quest.put("questName", questName);
//        quest.put("adminName", adminName);
//        quest.put("adminPass", adminPass);
//        quest.put("userPass", userPass);
//        quest.put("usersLimit", usersLimit);
//        quest.put("urlImage", 1815);
//        quest.put("isConfirmedByHQ", true);
//        quest.put("questDescription", questDescription);


        //

        db.collection("Quests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            partyNameList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  getResult
                                  String questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription,usersLimit;

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                questName = document.getData().get("questName").toString();
                                adminName = document.getData().get("adminName").toString();
                                adminPass = document.getData().get("adminPass").toString();
                                userPass = document.getData().get("userPass").toString();
                                usersLimit = document.getData().get("usersLimit").toString();
                                urlImage = document.getData().get("urlImage").toString();
                                isConfirmedByHQ = document.getData().get("isConfirmedByHQ").toString();
                                questDescription = document.getData().get("questDescription").toString();
                                String questLocation = document.getData().get("questLocation").toString();

                                QuestInfo questInfo=new QuestInfo(questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription,usersLimit,questLocation);
                                partyNameList.add(questInfo);

                            }

//                            partyNameList.add("j o p a a a");
//        partyNameList.add("biba");
//        partyNameList.add("Dina");

                            adapter = new MyAdapter(MainMenuActivity.this, partyNameList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        adapter = new MyAdapter(MainMenuActivity.this, partyNameList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

        recyclerView.setAdapter(adapter);
//        partyNameList.add(questName);
//        partyNameList.add("j o p a a a");
//        partyNameList.add("biba");
//        partyNameList.add("Dina");

//        adapter = new MyAdapter(MainMenuActivity.this, partyNameList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

//        recyclerView.setAdapter(adapter);


    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(MainMenuActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Three")) {
                    Toast.makeText(MainMenuActivity.this, "J O P A", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        popupMenu.show();
    }
}