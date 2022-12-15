package com.jaroid.messengerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.edtMessage)
    EditText edtMessage;
    @BindView(R.id.ibSend)
    ImageButton ibSend;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ChattingAdapter mChattingAdapter;
    private ArrayList<ChatMessage> mListChatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initData();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        rvMessage.setAdapter(mChattingAdapter);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage();

        String email = mUser.getEmail();
        chatMessage.setUserName(email.substring(0, email.indexOf("@")));

        chatMessage.setMessage(edtMessage.getText().toString());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        chatMessage.setTime(date.toString());

        chatMessage.setUid(mUser.getUid());


    }

    private void initData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "initData: " + mUser.getEmail());

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://android50-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mFirebaseDatabase.getReference(Global.ROOM_CHAT_REFERENCE);

        //up data to realtime database
//        mDatabaseReference.setValue("Hello, World!");

        DatabaseReference room1 = mDatabaseReference.child("room1");
        room1.setValue("hello im "+mUser.getEmail());

        mListChatMessages = new ArrayList<>();
        mListChatMessages.clear();
        mChattingAdapter = new ChattingAdapter(mListChatMessages);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d(TAG, "onDataChange: " + snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
    }
}