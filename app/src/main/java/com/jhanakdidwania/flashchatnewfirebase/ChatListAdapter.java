package com.jhanakdidwania.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter{

    private Activity mActivity;
    private String mDisplayName;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mChatSnapshot;

    private ChildEventListener mChildEventListener= new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mChatSnapshot.add(dataSnapshot);  //in json format
            notifyDataSetChanged(); //notify our list view that the data is changed in database
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    };

    public ChatListAdapter(Activity activity, String displayName, DatabaseReference ref){
        mActivity = activity;
        mDisplayName = displayName;
        mDatabaseReference = ref.child("Messages");
        ref.addChildEventListener(mChildEventListener);
        mChatSnapshot = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mChatSnapshot.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot= mChatSnapshot.get(position);
        return snapshot.getValue(InstantMessage.class); //converts the json into instant msg object
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder viewHolder= new ViewHolder();
            viewHolder.authorName = (TextView) convertView.findViewById(R.id.author);
            viewHolder.body = (TextView) convertView.findViewById(R.id.messageInput);
            viewHolder.params = (LinearLayout.LayoutParams) viewHolder.authorName.getLayoutParams();
            convertView.setTag(viewHolder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        boolean isMe= message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe, viewHolder);

        String author = message.getAuthor();
        viewHolder.authorName.setText(author);

        String message1= message.getMessage();
        viewHolder.body.setText(message1);

        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder viewHolder){
        if(isItMe){
            //if message belongs to the user itself
            viewHolder.params.gravity = Gravity.END;
            viewHolder.authorName.setTextColor(Color.GREEN);
            viewHolder.body.setBackgroundResource(R.drawable.bubble2);
        }else{
            viewHolder.params.gravity = Gravity.LEFT;
            viewHolder.authorName.setTextColor(Color.BLUE);
            viewHolder.body.setBackgroundResource(R.drawable.bubble1);
        }
        viewHolder.body.setLayoutParams(viewHolder.params);
        viewHolder.authorName.setLayoutParams(viewHolder.params);
    }

    //when the app is no more in the foreground, then no need to listen to the events!
    public void cleanup(){
        mDatabaseReference.removeEventListener(mChildEventListener);
    }
}
