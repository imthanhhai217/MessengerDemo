package com.jaroid.messengerdemo;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.MessageViewHolder> {

    private String mCurrentUid;
    ArrayList<ChatMessage> mListChatMessages;

    public ChattingAdapter(ArrayList<ChatMessage> mListChatMessages, String currentUid) {
        this.mListChatMessages = mListChatMessages;
        this.mCurrentUid = currentUid;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage chatMessage = mListChatMessages.get(position);
        holder.tvMessage.setText(chatMessage.getMessage());
        if (chatMessage.getUid().equals(mCurrentUid)) {
            holder.llMessage.setBackgroundResource(R.drawable.bg_message_self);
            holder.llMessageRow.setGravity(Gravity.RIGHT);
        } else {
            holder.llMessage.setBackgroundResource(R.drawable.bg_message_friend);
            holder.llMessageRow.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return mListChatMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llMessageRow)
        LinearLayout llMessageRow;
        @BindView(R.id.llMessage)
        LinearLayout llMessage;
        @BindView(R.id.tvMessage)
        TextView tvMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
