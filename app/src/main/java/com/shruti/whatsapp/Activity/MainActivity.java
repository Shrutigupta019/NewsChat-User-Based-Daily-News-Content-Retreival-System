package com.shruti.whatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shruti.whatsapp.Adapter.FragmentsAdapter;
import com.shruti.whatsapp.R;
import com.shruti.whatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // Firebase
    FirebaseAuth auth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Binding For TabLayout & ViewPager
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                auth.signOut(); // Just one line and you will sign out from your app
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.groupChat:
                Intent intent1 = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intent1);
                break;
            case R.id. news:
                Intent intent0 = new Intent(Intent.ACTION_MAIN);
                intent0.setComponent(new ComponentName("com.example.android.newsfeed","com.example.android.newsfeed.MainActivity"));
                startActivity(intent0);
                break;
//            case R.id.findFriends:
//                Intent intent3 = new Intent(MainActivity.this, FindFriendsActivity.class);
//                startActivity(intent3);
//                break;
            case R.id.createGroup:
//                Intent intent4 = new Intent(MainActivity.this, GroupChatActivity.class);
//                startActivity(intent4);
                  RequestNewGroup();
                  break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Alert Dialog Box Code for Create a New group
    private void RequestNewGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);//R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("E.g Developers..");//Group Hint
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();

                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"Please write Group Name....",Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(groupName);//method call
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    //Code for Creating a New Group
    private void CreateNewGroup(String groupName){
        database.child("Create Groups").child(groupName).setValue("")
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,groupName+" group is Created Successfully....",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Work " onBackPressed Button " !!
    @Override
    public void onBackPressed() {
        // Finish all activities in stack and app closes
        finishAffinity();
        finish();
    }
}