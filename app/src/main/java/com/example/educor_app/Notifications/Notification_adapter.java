package com.example.educor_app.Notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification_adapter extends RecyclerView.Adapter< Notification_adapter.MyViewHolder> {

    Context context;
    private String imageurl;
    public String isSeen;
    DatabaseReference reference;
    FirebaseUser users;
    public  String classname;
    public String key;
    public String Teacher;
    ArrayList<com.example.educor_app.Notifications.Notification_data> Notification_data;
    public  Notification_adapter(Context c, ArrayList<Notification_data> rm,String imageurl,String classname,String key,String Teacher){
        context=c;
        Notification_data=rm;
        this.imageurl = imageurl;
        this.classname=classname;
        this.key=key;
        this.Teacher=Teacher;
    }
    @NonNull
    @Override
    public Notification_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Notification_adapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_notification, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull Notification_adapter.MyViewHolder holder, int position) {

        final Notification_data user = Notification_data.get(position);
        holder.name.setText(Notification_data.get(position).getName());
        holder.time.setText(Notification_data.get(position).getTime());
        holder.message.setText(Notification_data.get(position).getMessage());
        isSeen=Notification_data.get(position).getIsSeen();
        users = FirebaseAuth.getInstance().getCurrentUser();
        if(isSeen.equals("true")){
            holder.relativeLayout_bg.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.relativeLayout_bg.setBackgroundColor(Color.parseColor("#abcdef"));
        }
        if (user.getImgUrl().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile_img);
        } else {
            Glide.with(context).load(Notification_data.get(position).getImgUrl()).into(holder.profile_image);
        }


        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Notification").child(users.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot1:dataSnapshot.getChildren()) {
                            Notification_data rm = snapshot1.getValue(Notification_data.class);
                            reference.child("isSeen").setValue(true);
                        }
                       
                       holder.relativeLayout_bg.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });*/

    }
    @Override
    public int getItemCount() {
        return Notification_data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,message,time;
        public ImageView profile_image;
        public RelativeLayout relativeLayout_bg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.User);
            time=(TextView)itemView.findViewById(R.id.Time);
            message=(TextView) itemView.findViewById(R.id.Post);
            profile_image = itemView.findViewById(R.id.profile_image);
            relativeLayout_bg=itemView.findViewById(R.id.cardview_bg);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();
            String refkey=Notification_data.get(position).getRefkey();
            Intent intent=new Intent(context, Event_page.class);
            String seen=Notification_data.get(position).getIsSeen();
            intent.putExtra("name",classname);
            intent.putExtra("key",key);
            intent.putExtra("Teacher",Teacher);
            if(seen.equals("false"))
            {
                reference = FirebaseDatabase.getInstance().getReference().child("Notification").child(users.getUid()).child(refkey);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            reference.child("isSeen").setValue("true");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
            context.startActivity(intent);

        }
    }
}
