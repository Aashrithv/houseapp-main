package com.example.improvedhouse.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.improvedhouse.R;
import com.example.improvedhouse.models.UserDetails;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * This activity handles the Chats tab. In this, we will display all the list of users that
 * the user had conversation with. Upon selecting any user's chat, we call Chats.class for display
 * the complete conversation.
 *
 */
public class ChatActivity extends Fragment {

    ListView usersList;
    TextView noUsersText;
    final ArrayList<String> al = new ArrayList<>();
    final ArrayList<String> al1 = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Firebase reference1;
    String name;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.chats_tab, container, false);

        usersList = (ListView) rootView.findViewById(R.id.usersList);
        noUsersText = (TextView) rootView.findViewById(R.id.noUsersText);
        Log.d("onCreateView", noUsersText.getText().toString());

        Firebase.setAndroidContext(getContext());
        reference1 = new Firebase("https://improved-house-default-rtdb.firebaseio.com/Users/" + UserDetails.username + "/messages");

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        final String url = "https://improved-house-default-rtdb.firebaseio.com/Users/" + UserDetails.username + "/messages.json";
        Log.d("Success", "I am here ");


        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        final RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = al.get(position);
                UserDetails.chatWith = name.substring(0,name.indexOf(" "));
                Log.d("chatWith",UserDetails.chatWith);
                reference1.child(UserDetails.chatWith).child("msg").setValue(" ");
                startActivity(new Intent(getContext(), Chats.class));
            }
        });
        return rootView;
    }

    public void doOnSuccess(final String s) {
        try {
            al.clear();
            al1.clear();
            final JSONObject obj = new JSONObject(s);

            final Iterator j = obj.keys();
            String temp;

            while(j.hasNext()) {
              final String key = j.next().toString();
                temp=obj.getJSONObject(key).getString("time") + "/" + key + "  " + obj.getJSONObject(key).getString("msg");
                Log.d("message", temp);
                al1.add(temp);
            }
            Collections.sort(al1);
            for(int k=0;k<al1.size();k++) {
                temp = al1.get(k);
                al.add(temp.substring(temp.lastIndexOf("/")+1, temp.length()));
                totalUsers++;
                Log.d("message-name", al.get(k));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            Collections.reverse(al);
            usersList.setAdapter(new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
    }

}