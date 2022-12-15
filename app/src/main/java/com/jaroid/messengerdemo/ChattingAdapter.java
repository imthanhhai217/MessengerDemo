package com.jaroid.messengerdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.MessageViewHolder> {

    ArrayList<ChatMessage> mListChatMessages;

    public ChattingAdapter(ArrayList<ChatMessage> mListChatMessages) {
        this.mListChatMessages = mListChatMessages;
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

        holder.tvUserName.setText(chatMessage.getUserName());
        holder.tvMessage.setText(chatMessage.getMessage());
        holder.tvTime.setText(chatMessage.getTime());
    }

    @Override
    public int getItemCount() {
        return mListChatMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(itemView);
        }
    }
}
