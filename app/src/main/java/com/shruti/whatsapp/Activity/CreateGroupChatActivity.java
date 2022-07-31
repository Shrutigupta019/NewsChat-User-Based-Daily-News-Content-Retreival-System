package com.shruti.whatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shruti.whatsapp.Models.MessageModel;
import com.shruti.whatsapp.Models.Users;
import com.shruti.whatsapp.R;
import com.shruti.whatsapp.databinding.ActivityCreateGroupChatBinding;
import com.shruti.whatsapp.databinding.ActivityGroupChatBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class CreateGroupChatActivity extends AppCompatActivity {

    ActivityCreateGroupChatBinding binding;
    private Toolbar mToolbar;
    private ImageView SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,GroupNameRef,GroupMessageKeyRef;


    private String currentGroupName,currentUserID, currentUserName, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_create_group_chat);

     //  getSupportActionBar().hide(); // Hide a Actionbar from Top in our app

        // Retrieve the current group Name
        currentGroupName = getIntent().getExtras().get("Current Group Name").toString();
        //Toast.makeText(CreateGroupChatActivity.this,currentGroupName,Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Create Groups").child(currentGroupName);

        InitialiseFields();//Call Method

        GetUserInfo();

        // Button save message to database
        SendMessageButton.setOnClickListener(view -> {

            SaveMessageInfoToDatabase();
            userMessageInput.setText(""); // After sending message to user TextField get set to NULL
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }
    //Show messages on Screen
    @Override
    protected void onStart()
    {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void InitialiseFields() {
        mToolbar = findViewById(R.id.create_group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Set Back Arrow
        getSupportActionBar().setDisplayShowHomeEnabled(true);
     //   getSupportActionBar().setTitle("Group Name");
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageView) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
        displayTextMessages = (TextView) findViewById(R.id.create_group_chat_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }

    private void GetUserInfo()
    {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("userName").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SaveMessageInfoToDatabase()
    {
        String message = userMessageInput.getText().toString();
        String messagekEY = GroupNameRef.push().getKey(); // create a unique key for each message

        // Set Error if u not fill Message for Send
        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();

        }
        else
        {
//            // Set Date
//            Calendar calForDate = Calendar.getInstance();
//            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
//            currentDate = currentDateFormat.format(calForDate.getTime());
//
//            //Set Time
//            Calendar calForTime = Calendar.getInstance();
//            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");// a will give AM or PM
//            currentTime = currentTimeFormat.format(calForTime.getTime());

            //Update message
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messagekEY);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
           // messageInfoMap.put("date", currentDate);
           // messageInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }
    //Display message on Screen
    private void DisplayMessages(DataSnapshot dataSnapshot)
    {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
          //  String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
          //  String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displayTextMessages.append(chatName + " :\n" + chatMessage + "\n\n\n");//"\n" + chatTime + "     " + chatDate + "\n\n\n");

            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}