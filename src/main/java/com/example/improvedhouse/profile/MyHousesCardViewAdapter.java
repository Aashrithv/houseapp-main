package com.example.improvedhouse.profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
 * This class is responsible for converting the list of Houses into a card view and display them
 * under the View My houses section of Profile tab.
 * This is invoked by the ViewMyHouses activity
 */
public class MyHousesCardViewAdapter extends BaseAdapter {

    private Activity activity;
    List<HouseProperties> HouselistBeenvalues;
    private ArrayList<HouseProperties> arraylist;
    DatabaseReference dbr;
    DatabaseReference dbr2;

    public MyHousesCardViewAdapter(Activity activity, List<HouseProperties> HouselistBeenvalues){
        this.activity=activity;
        this.HouselistBeenvalues=HouselistBeenvalues;

        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(HouselistBeenvalues);
    }

    @Override
    public int getCount() {
        return this.HouselistBeenvalues.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater li=activity.getLayoutInflater();
        View v=li.inflate(R.layout.houselist_card2,null,true);
        TextView houseName=(TextView)v.findViewById(R.id.tv_houseName);
        TextView typeofhouse=(TextView)v.findViewById(R.id.tv_typeofhouse);
        TextView address=(TextView)v.findViewById(R.id.tv_address);
        TextView rentamount=(TextView)v.findViewById(R.id.tv_rentamount);
        TextView leaserentsale=(TextView)v.findViewById(R.id.tv_lease_rent_sale);
        TextView youremail=(TextView)v.findViewById(R.id.tv_youremail);
        ImageView imageview=(ImageView)v.findViewById(R.id.personImage);
        Button delete =(Button)v.findViewById(R.id.btn_delete);

        dbr= FirebaseDatabase.getInstance().getReference();
        dbr2= FirebaseDatabase.getInstance().getReference();

        houseName.setText(/*"HOUSE NAME     :"+*/HouselistBeenvalues.get(position).getHousename());
        typeofhouse.setText(/*"TYPEOF HOUSE :"+*/HouselistBeenvalues.get(position).getHousetype());
        address.setText(/*"HOUSE ADDRESS     :"+*/HouselistBeenvalues.get(position).getHouseaddress());
        rentamount.setText(/*"HOUSE RENT AMOUNT :"+*/HouselistBeenvalues.get(position).getRentamount());
        leaserentsale.setText(/*"LEASE/RENT/SALE :"+*/HouselistBeenvalues.get(position).getLease_rent_sale());
        youremail.setText(/*"YOUR EMAIL :"+*/HouselistBeenvalues.get(position).getYouremail());
        String image=HouselistBeenvalues.get(position).getImage();

        Glide.with(activity).load(image).into(imageview);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedKey=HouselistBeenvalues.get(position).getkey();
                Query query1=dbr.child("HousesList").child(selectedKey);
                Query query2=dbr2.child("Users").child(UserDetails.username).child("Houses").child(selectedKey);

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                            Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            dataSnapshot1.getRef().removeValue();
                            //Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return v;
    }
}

