package com.example.improvedhouse.houses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.improvedhouse.models.HouseProperties;
import com.example.improvedhouse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity handles the Houses tab. In this, we call the "HousesList" DB and get all the
 * houses that are available and display then in this tab.
 *
 */
public class HousesActivity extends Fragment implements TextWatcher {
    HousesCardViewAdapter housesCardViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.houses_tab, container, false);

        final List<HouseProperties> housesList = new ArrayList<>();

        // Creating the instance of the 'HousesList' DB
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("HousesList");
        // Getting the ListView UI element, in this ListView we set all the houses that are present in DB
        final ListView house_list = (ListView) rootView.findViewById(R.id.houseList);

        // For any new house that is getting added to the DB this below block will be be executed
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                housesList.clear();
                // Iterating through each of the house
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.d("house_list", "House: " + dataSnapshot1.getValue() + "\n");

                    // Extracting the fields of the house and parsing them with HouseProperties class.
                    final HouseProperties keyValue = dataSnapshot1.getValue(HouseProperties.class);
                    housesList.add(keyValue);
                }
                // Converts the list of HouseProperties to CardViews and displays them
                housesCardViewAdapter = new HousesCardViewAdapter(getContext(), housesList);
                house_list.setAdapter(housesCardViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        housesCardViewAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
