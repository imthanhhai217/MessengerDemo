package com.jaroid.messengerdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
//                runTestCrash();
            }
        });
    }

//    private void runTestCrash() {
//        throw new RuntimeException("ERROR ");
//    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage();

        String email = mUser.getEmail();
        chatMessage.setUserName(email.substring(0, email.indexOf("@")));

        chatMessage.setMessage(edtMessage.getText().toString());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        chatMessage.setTime(date.toString());

        chatMessage.setUid(mUser.getUid());

        mDatabaseReference.push().setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    edtMessage.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            Log.d(TAG, "initData: " + mUser.getEmail());

            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance("https://android50-default-rtdb.asia-southeast1.firebasedatabase.app/");
            mDatabaseReference = mFirebaseDatabase.getReference(Global.ROOM_CHAT_REFERENCE);
            mDatabaseReference.addChildEventListener(messageListener);

            mListChatMessages = new ArrayList<>();
            mListChatMessages.clear();
            mChattingAdapter = new ChattingAdapter(mListChatMessages, mUser.getUid());
        }
    }

    private ChildEventListener messageListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Log.d(TAG, "onChildAdded: " + previousChildName);
            ChatMessage newMessage = snapshot.getValue(ChatMessage.class);
            if (newMessage != null) {
                addNewMessage(newMessage, previousChildName);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //Sử dụng previousChildName để kiểm tra xem tin nhắn trước nó có phải là cùng 1 người nhắn không
    private void addNewMessage(ChatMessage newMessage, String previousChildName) {
        ChatMessage chatMessage = newMessage;
        if (mUser.getUid().equals(newMessage.getUid())) {
            chatMessage.setMessageType(ChattingAdapter.MESSAGE_SELF);
        } else {
            chatMessage.setMessageType(ChattingAdapter.MESSAGE_FRIEND);
        }


        mListChatMessages.add(0, chatMessage);
        mChattingAdapter.notifyItemInserted(0);
        rvMessage.smoothScrollToPosition(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mAuth != null) {
            mAuth.signOut();
        }
    }
}