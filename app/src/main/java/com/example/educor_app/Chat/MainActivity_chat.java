package com.example.educor_app.Chat;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.educor_app.Chat.Adapter.OnItemClick;
import com.example.educor_app.Chat.Fragments.ChatsFragment;
import com.example.educor_app.Chat.Fragments.UsersFragment;
import com.example.educor_app.Chat.Model.Chat;
import com.example.educor_app.Chat.Model.User;
import com.example.educor_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_chat extends AppCompatActivity {

    //boolean doubleBackToExitPressedOnce = false;
    CircleImageView profile_image;
    TextView username;
    ProgressDialog dialog;
    Typeface MR,MRR;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    OnItemClick onItemClick;

    public String Classnames;
    public String key;
    public String Teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        Classnames = getIntent().getStringExtra("name");
        key = getIntent().getStringExtra("key");
        Teacher=getIntent().getStringExtra("Teacher");


        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image = findViewById(R.id.profile_image);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        username = findViewById(R.id.username);
        username.setTypeface(MR);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(Classnames).child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (firebaseUser != null) {
                    if (firebaseUser.getPhotoUrl() != null) {
                        Glide.with(MainActivity_chat.this)
                                .load(firebaseUser.getPhotoUrl())
                                .into(profile_image);
                    }
                } else {
                    //change this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Chats").child(Classnames);
        dialog = Utils.showLoader(MainActivity_chat.this);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }

                if (unread == 0){

                    Bundle data=new Bundle();
                    data.putString("name",Classnames);
                    //viewPagerAdapter.addFragment(ChatsFragment.newInstance(onItemClick), "Chats");
                    //ChatsFragment.newInstance(onItemClick).setArguments(data);
                    ChatsFragment fragment = ChatsFragment.newInstance(onItemClick);
                    fragment.setArguments(data);
                    viewPagerAdapter.addFragment(fragment, "Chats");

                } else {
                    Bundle data=new Bundle();
                    data.putString("name",Classnames);
                    ChatsFragment fragment = ChatsFragment.newInstance(onItemClick);
                    fragment.setArguments(data);
                    viewPagerAdapter.addFragment(fragment, "("+unread+") Chats");
                }

                Bundle data=new Bundle();
                data.putString("name",Classnames);
               // viewPagerAdapter.addFragment(UsersFragment.newInstance(onItemClick), "Users");
               // UsersFragment.newInstance(onItemClick).setArguments(data);
                UsersFragment fragments = UsersFragment.newInstance(onItemClick);
                fragments.setArguments(data);
                viewPagerAdapter.addFragment(fragments, "Users");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);
                if(dialog!=null){
                dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @NonNull
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(Classnames).child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
