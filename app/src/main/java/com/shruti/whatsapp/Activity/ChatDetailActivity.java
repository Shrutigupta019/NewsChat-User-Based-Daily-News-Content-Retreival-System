package com.shruti.whatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shruti.whatsapp.Adapter.ChatAdapter;
import com.shruti.whatsapp.Models.MessageModel;
import com.shruti.whatsapp.R;
import com.shruti.whatsapp.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide(); // Hide a Actionbar from Top in our app

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();// For sender chat
        String receiveId = getIntent().getStringExtra("userId"); // For receiver chat
        // Receive username & Profile pic from MainActivity to ChatDetailActivity
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName); // Set UserName of each user

        Picasso.get().load(profilePic).placeholder(R.drawable.ic_profile).into(binding.profileImage); // Set profile image of each user

        //Code For Back Arrow
        binding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Code for Message Sender & Receiver!!
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this, receiveId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        // Get Data from Firebase & show it on screen
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());// This line is add to "generate id" for delete a sender message
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Set onClick Listener on Send Button + Save our data into Firebase
        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Error if u not fill Message for Send
                if(binding.etMessage.getText().toString().isEmpty()){
                    binding.etMessage.setError("Enter message");
                    return;
                }
              //Date date = new Date();
              String message =  binding.etMessage.getText().toString();
              final MessageModel model = new MessageModel(senderId,message); // Take userid & message from MessageModel class &save it in database
              model.setTimestamp(new Date().getTime()); // set time & save it to database
              binding.etMessage.setText(""); // After message send make text field empty

              //Show Recent message in conversation list & set up it in database
//              HashMap<String, Object> lastMessageObject = new HashMap<>();
//              lastMessageObject.put("lastMsg",model.getMessage());
//              lastMessageObject.put("lastMsgTime",date.getTime());
//
//              database.getReference().child("chats").child(senderRoom).updateChildren(lastMessageObject);
//              database.getReference().child("chats").child(receiverRoom).updateChildren(lastMessageObject);

              // Create a child for particular Users(FOR SENDER & RECEIVER message) with its-> message,timestamp,userid
              database.getReference().child("chats")
                      .child(senderRoom)
                      .push()
                      .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {
                      database.getReference().child("chats")
                              .child(receiverRoom)
                              .push()
                              .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {

                          }
                      });
                  }
              });
            }
        });


    }
}