package com.example.educor_app.Authentications;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.educor_app.Assignment.AssignmentUpload;
import com.example.educor_app.Chat.Model.User;
import com.example.educor_app.Chat.StartActivity;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.Notifications.Notification;
import com.example.educor_app.R;
import com.example.educor_app.Screenrecorder.C;
import com.example.educor_app.jitsi.jitsi_room;
import com.example.educor_app.quiz.Attempt_Quiz_Section.Tests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    public TextView classname;
    public TextView classkey;
    public ImageView todo;
    public ImageView zoom_button;
    private ImageView file_button;
    private ImageView recorder;
    private ImageView event_button;
   // private ImageView chat_button;
    private ImageView quiz_button;
  //  private ImageView pdf_scanner;
    CircleImageView circle;
    FirebaseAuth mAuth;
    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference databaseReference;
    public String UserName;
    public String status;
    public String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.homepage);
        circle=findViewById(R.id.circle);
        classname=findViewById(R.id.Classname);
        classkey=findViewById(R.id.key);
        zoom_button=findViewById(R.id.zoom_button);
        file_button=findViewById(R.id.file_button);
        recorder=findViewById(R.id.report);
        event_button=findViewById(R.id.event_button);
        // chat_button=findViewById(R.id.chat_button);
        quiz_button=findViewById(R.id.quiz_button);
        // pdf_scanner=findViewById(R.id.pdf_scanner);



        todo=findViewById(R.id.todo);

        String Classname = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userid = firebaseUser.getUid();


        classname.setText(Classname);
        classkey.setText(key);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();



        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                imageUrl=user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(circle);
            }else{
                imageUrl="default";
            }
        }

        databaseReference= FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserName= Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //chat
        status="offline";

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent=new Intent(Dashboard.this,ProfileActivity.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        return true;
                    case R.id.chat:
                        final User infos=new User(user.getUid(),UserName,imageUrl,status,UserName,Classname);

                        FirebaseDatabase.getInstance().getReference("Users").child(Classname).child(userid)
                                .setValue(infos).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent=new Intent(Dashboard.this, StartActivity.class);
                                intent.putExtra("name",Classname);
                                intent.putExtra("key",key);
                                intent.putExtra("Teacher",Teacher);
                                startActivity(intent);
                            }
                        });
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        Intent lntents=new Intent(Dashboard.this,Event_page.class);
                        lntents.putExtra("name",Classname);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        Intent lntentss=new Intent(Dashboard.this,Notification.class);
                         lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classname);
                         lntentss.putExtra("Teacher", Teacher);
                         startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });







        //ends



                circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,ProfileActivity.class);
                intent.putExtra("name",Classname);
                intent.putExtra("key",key);
                intent.putExtra("Teacher",Teacher);
                startActivity(intent);
            }
        });

      /*  chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final User infos=new User(user.getUid(),UserName,imageUrl,status,UserName,Classname);

                FirebaseDatabase.getInstance().getReference("Users").child(Classname).child(userid)
                        .setValue(infos).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent=new Intent(Dashboard.this, StartActivity.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("key",key);
                        intent.putExtra("Teacher",Teacher);
                        startActivity(intent);
                    }
                });

            }
        });*/

        zoom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, jitsi_room.class);
                startActivity(intent);
            }
        });

        event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, Event_page.class);
                intent.putExtra("name",Classname);
                intent.putExtra("key",key);
                intent.putExtra("Teacher",Teacher);
                startActivity(intent);
            }
        });


        recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, C.class);
                startActivity(intent);
            }
        });

        quiz_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, Tests.class);
                intent.putExtra("name",Classname);
                startActivity(intent);
            }
        });

        file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, com.example.educor_app.Notes.notes_upload.class);
                intent.putExtra("name",Classname);
                intent.putExtra("Teacher",Teacher);
                startActivity(intent);
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, AssignmentUpload.class);
                intent.putExtra("name",Classname);
                intent.putExtra("Teacher",Teacher);
                startActivity(intent);
            }
        });

    }
}