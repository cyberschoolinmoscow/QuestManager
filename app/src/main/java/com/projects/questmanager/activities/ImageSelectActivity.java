package com.projects.questmanager.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;
import com.projects.questmanager.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;


public class ImageSelectActivity extends AppCompatActivity {
    private static final int PICK_IMG=1,PICK_PHOTO=2;
    private static final int PICK_IMAGE_REQUEST = 22;
    Button btn_gallery,btn_camera,btn_upload;
    ImageView imageView;
    static    Uri imgUri;
    Uri ur;
    FirebaseAuth auth;
    private StorageReference mStorageRef;
    FloatingActionButton closeBtn;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageButton menuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());


        closeBtn=(FloatingActionButton) findViewById(R.id.closepbtn);
        closeBtn.setOnClickListener(v-> closeForm());
        btn_gallery =(Button)findViewById(R.id.btn);
        btn_camera =(Button)findViewById(R.id.btn_c);
        btn_upload =(Button)findViewById(R.id.btn_upload);
        imageView=(ImageView) findViewById(R.id.iv);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Выбери фото"),PICK_IMG);
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera=new Intent();
                camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if(camera.resolveActivity(getPackageManager())!=null){

                    File imgfile=null;
                    try {
                        String name="myphoto"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        File img_storage= getCacheDir();
                        imgfile=File.createTempFile(name,".jpg",img_storage);
                        if(imgfile!=null){

                            imgUri= FileProvider.getUriForFile(ImageSelectActivity.this,"myauthorityK",imgfile);
                            Toast.makeText(ImageSelectActivity.this, imgfile.toString()+" OK", Toast.LENGTH_LONG).show();
                            camera.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                            //  startActivityForResult(camera,PICK_PHOTO);
                            startActivityIfNeeded(camera,PICK_PHOTO);
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        });

        // get the Firebase storage reference
        storage = FirebaseStorage.getInstance("gs://questmanager-b70bf.appspot.com");
        storageReference = storage.getReference();

        btn_upload.setOnClickListener(v->uploadImage());

//        DatabaseReference myph= FirebaseDatabase.getInstance().getReference().child("myph");
//
//        myph.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ur=Uri.parse(dataSnapshot.getValue().toString());
//                Picasso.get().load(ur).into(imageView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//


//        auth=FirebaseAuth.getInstance();
//        mStorageRef = FirebaseStorage.getInstance().getReference();


//        filePath=ur;


//                new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseUser user=auth.getCurrentUser();
//                String userID=user.getUid();

//                String name=imgUri.toString();
//                StorageReference riversRef = mStorageRef.child(PlayerPreferences.questName+".jpg");
//                riversRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        if (taskSnapshot.getMetadata() != null) {
//                            if (taskSnapshot.getMetadata().getReference() != null) {
//                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String imageUrl = uri.toString();
//                                        //createNewPost(imageUrl);
//                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Dishes");
//
//                                        myRef.child(PlayerPreferences.questName).child("profilePic").setValue(imageUrl);
//                                        Toast.makeText(ImageSelectActivity.this,  imageUrl+" OK", Toast.LENGTH_LONG).show();
//
//                                    }
//                                });
//                            }
//                        }}
//                });
//            }});

    }
    
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(ImageSelectActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            //Do what you want with the url
                                            Log.println(Log.DEBUG,"bbb",uri.toString());
                                            PlayerPreferences.urlLink=uri.toString();
                                            CreateParty();

                                        }

                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ImageSelectActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");

                                    if(progress==100.0){
                                        closeForm();
                                    }
                                }

                            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMG&&resultCode==RESULT_OK){
            imgUri=data.getData();
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            try {
                Picasso.get().load(imgUri).into(imageView);
            }catch (Exception e){e.printStackTrace();}
        }
        else if(requestCode==PICK_PHOTO&&resultCode==RESULT_OK){
            try {
                Picasso.get().load(imgUri).into(imageView);
            }catch (Exception e){e.printStackTrace();}
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    //CreateQuestActivity transition may be changed to another activity
   public   void closeForm(){
        Intent intent=new Intent(this, CreateQuestActivity.class);
        startActivity(intent);
    }


    private void CreateParty() {
        String questName, adminName, adminPass, userPass,  isConfirmedByHQ, questDescription,usersLimit,questLocation;
        questName="default";
        //todo: create static fields final
        adminName=(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userPass="setEntryPass";
        adminPass="setAdminPass";
        usersLimit="10";
        questDescription="description";
        questLocation="location";
        String  questID="default";
//        urlImage="";
//      PlayerPreferences.urlLink=urlImage;
        // Create a new quest with a first and last name
//        Map<String, Object> quest = new HashMap<>();
//        quest.put("questName", questName);
//        quest.put("adminName", adminName);
//        quest.put("adminPass", adminPass);
//        quest.put("userPass", userPass);
//        quest.put("usersLimit", usersLimit);
//        quest.put("urlImage", "1815");
//        quest.put("isConfirmedByHQ", "true");
//        quest.put("questDescription", questDescription);

        QuestInfo quest=new QuestInfo(questName, adminName, adminPass,  userPass,  PlayerPreferences.urlLink,  "isConfirmedByHQ",  questDescription, usersLimit, questLocation, questID);
//if( db.collection("Quests").)

//        try {
//            MyUtils.updateQuestInfo(quest,PlayerPreferences.currentQuest.getQuestID());
//            Intent intentManagement = new Intent(CreateQuestActivity.this, TaskManagementActivity.class);
//            startActivity(intentManagement);
//        }catch (Exception e) {
// Add a new document with a generated ID
            db.collection("Quests")
                    .add(quest)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                        db.collection("").getId(documentReference.getId().toString()).
//                                document.getData().put("questID",questID);

                            PlayerPreferences.currentQuest=quest;
                            PlayerPreferences.currentQuest.setQuestID(documentReference.getId());
                            MyUtils.updateQuestInfo(quest,PlayerPreferences.currentQuest.getQuestID());
                            Intent intentManagement = new Intent(ImageSelectActivity.this, CreateQuestActivity.class);
                            startActivity(intentManagement);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
//        Intent intent=new Intent(CreateQuestActivity.this,TaskManagementActivity.class);
//        startActivity(intent);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.popupmenu, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.one:
//                Intent intentMyQuests = new Intent(ImageSelectActivity.this, MyQuestActivity.class);
//                startActivity(intentMyQuests);
//
//                return true;
//            case R.id.two:
//                Intent intentCreate = new Intent(ImageSelectActivity.this, ImageSelectActivity.class);
//                startActivity(intentCreate);
//
//                return true;
//            case R.id.three:
//                Intent intentMain = new Intent(ImageSelectActivity.this, MainMenuActivity.class);
//                startActivity(intentMain);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(ImageSelectActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "My quests":
                        Intent intentMyQuests = new Intent(ImageSelectActivity.this, MyQuestActivity.class);
                        startActivity(intentMyQuests);
                        break;

                    case "Create quest":
                        Intent intentCreate = new Intent(ImageSelectActivity.this, ImageSelectActivity.class);
//                        Intent intentCreate = new Intent(MainMenuActivity.this, CreateQuestActivity.class);
                        startActivity(intentCreate);
                        break;
                    case "All quests":
                        Intent intentMain = new Intent(ImageSelectActivity.this, MainMenuActivity.class);
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
