package com.example.improvedhouse.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.improvedhouse.R;
import com.example.improvedhouse.models.UserDetails;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for displaying the complete conversation b/w two users
 */
public class Chats extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2,reference3,reference4;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        final TextView chatWithUser = findViewById(R.id.chatWith);
        chatWithUser.setText(UserDetails.chatWith);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://improved-house-default-rtdb.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://improved-house-default-rtdb.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        reference3 = new Firebase("https://improved-house-default-rtdb.firebaseio.com/Users/" + UserDetails.username+"/messages");
        reference4 = new Firebase("https://improved-house-default-rtdb.firebaseio.com/Users/" + UserDetails.chatWith+"/messages");


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    reference3.child(UserDetails.chatWith).child("time").setValue(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                    reference3.child(UserDetails.chatWith).child("msg").setValue(" ");
                    reference4.child(UserDetails.username).child("time").setValue(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                    reference4.child(UserDetails.username).child("msg").setValue(" - new message");
                    flag=1;

                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)) {
                    addMessageBox(message, 1);
                } else {
                    addMessageBox(message, 2);
                }
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        final TextView textView = new TextView(Chats.this);
        textView.setTextColor(Color.BLACK);
        textView.append(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            lp.setMargins(500, 0, 0, 10);

        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            lp.setMargins(0, 0, 500, 10);
        }
        textView.setLayoutParams(lp);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}