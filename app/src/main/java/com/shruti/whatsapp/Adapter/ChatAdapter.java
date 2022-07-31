package com.shruti.whatsapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shruti.whatsapp.Models.MessageModel;
import com.shruti.whatsapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    // Generate Constructor
    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    // Generate Constructor
    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }


    // As we have 2 View Holder so to identify View Type we use getItemViewType!!
    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    // Implemented Methods that come from RecyclerView.Adapter!!
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return  new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    // As we have 2 View Holder so to identify View Type we use getItemViewType!!


    // Set Sender & Receiver Message!!
    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);

        // Show up PopUp Reaction on Chat
//        ReactionsConfig config = new ReactionsConfigBuilder(context)
//                .withReactions(new int[]{
//                        R.drawable.ic_fb_like,
//                        R.drawable.ic_fb_love,
//                        R.drawable.ic_fb_laugh,
//                        R.drawable.ic_fb_wow,
//                        R.drawable.ic_fb_sad,
//                        R.drawable.ic_fb_angry
//                })
//                .build();
//        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
//            return true; // true is closing popup, false is requesting a new selection
//        });


        // Set Alert Dialog Box for delete a message
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return false;
            }
        }); //till here

        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());

            //Show up PopUp Reaction Sender on chat
//            ((SenderViewHolder)holder).senderMsg.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v,event);
//                    return false;
//                }
//            });
        }
        else{
            ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());

            //Show up PopUp Reaction on Receiver chat
//            ((ReceiverViewHolder)holder).receiverMsg.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v,event);
//                    return false;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    // Use 2 View Holder.....
    // 1. ReceiverViewHolder
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg;
        TextView receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.txtReceiverText);
            receiverTime = itemView.findViewById(R.id.txtReceiverTime);
        }
    }

    // 2. SenderViewHolder
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg;
        TextView senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.txtSenderText);
            senderTime = itemView.findViewById(R.id.txtSenderTime);
        }
    }
}
