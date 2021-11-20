package com.example.improvedhouse.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.improvedhouse.R;
import com.example.improvedhouse.models.HouseProperties;
import com.example.improvedhouse.models.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for displaying all the houses that are uploaded by the user
 * under Profile tabs of the app
 */
public class ViewMyHouses extends AppCompatActivity {

    ListView myHousesLists;
    DatabaseReference dbr;
    List<HouseProperties> housePropertiesList;
    MyHousesCardViewAdapter mhouselistadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_houses);

        housePropertiesList=new ArrayList<>();
        myHousesLists = (ListView) findViewById(R.id.houseList);

        dbr= FirebaseDatabase.getInstance().getReference();
        Query query =dbr.child("Users").child(UserDetails.username).child("Houses");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                housePropertiesList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    HouseProperties keyValue=dataSnapshot1.getValue(HouseProperties.class);
                    keyValue.setkey(dataSnapshot1.getKey());

                    housePropertiesList.add(keyValue);
                }
                mhouselistadapter=new MyHousesCardViewAdapter(ViewMyHouses.this,housePropertiesList);
                myHousesLists.setAdapter(mhouselistadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
