package com.shruti.whatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shruti.whatsapp.Activity.ChatDetailActivity;
import com.shruti.whatsapp.Models.Users;
import com.shruti.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

// This class is user for sample_show_user Recycler view!!
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    // In onCreateViewHolder we give sample_show_user.Now in this app all the users who will signup,they will all shown here...
   // User's-> lastMessage, profile pic, username will shown in this form. For this we use sample_show_user.This work is done AUTOMATICALLY!!
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    // In onBindViewHolder we give obtain the values for sample_show_user.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Users users = list.get(position);

//      String senderId = FirebaseAuth.getInstance().getUid();
//      String senderRoom = senderId + users.getUserId();
//      FirebaseDatabase.getInstance().getReference()
//              .child("chats")
//              .child(senderRoom)
//              .addValueEventListener(new ValueEventListener() {
//                  @Override
//                  public void onDataChange(@NonNull DataSnapshot snapshot) {
//                      if (snapshot.exists()) {
//                          String lastMsg = snapshot.child("lastMsg").getValue(String.class);
//                         // long time = snapshot.child("lastMsgTime").getValue(Long.class);
//                         // SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//                          holder.lastMessage.setText(lastMsg);
//                      } else {
//                          holder.lastMessage.setText("Tap to chat");
//                      }
//                  }
//                  @Override
//                  public void onCancelled(@NonNull DatabaseError error) {
//
//                  }
//              });

      //"Picasso" is use to upload an image from internet & "Placeholder" is use to set default image in your app until u will not load your image
      Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_profile).into(holder.image); // Set image

      holder.userName.setText(users.getUserName()); // Set Username

      // Set Last message
      FirebaseDatabase.getInstance().getReference().child("chats")
              .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
              .orderByChild("timestamp")
              .limitToLast(1)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull  DataSnapshot snapshot) {
                      if(snapshot.hasChildren()){
                          for (DataSnapshot snapshot1 : snapshot.getChildren()){
                              holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });

        // Set Intent for "ChatDetailActivity" after clicking on particular user(acc. to its position) we it will move to ChatDetailActivity
        // & its userid,profile pic,username will  be shown in ChatDetailActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilepic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName;
        TextView lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.txtUserName);
            lastMessage = itemView.findViewById(R.id.txtLastMessage);
        }
    }
}
