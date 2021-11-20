package com.example.improvedhouse.houses;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.improvedhouse.R;
import com.example.improvedhouse.chat.Chats;
import com.example.improvedhouse.models.HouseProperties;
import com.example.improvedhouse.models.UserDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for converting the list of Houses into a card view.
 * This is invoked by the HousesActivity
 */
public class HousesCardViewAdapter extends BaseAdapter implements Filterable {
    Context c;
    List<HouseProperties> housePropertiesList;
    ArrayList<HouseProperties> originalArray,tempArray;
    DatabaseReference dbr;
    CustomFilter cs;

    public HousesCardViewAdapter(Context c, List<HouseProperties> housePropertiesList) {
        this.c=c;
        this.housePropertiesList = housePropertiesList;
        this.originalArray = new ArrayList<>();
        this.originalArray.addAll(housePropertiesList);
        this.tempArray = new ArrayList<>();
        this.tempArray.addAll(housePropertiesList);
    }

    @Override
    public int getCount() {
        return this.originalArray.size();
    }

    @Override
    public Object getItem(int i) {
        return originalArray.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // In this method we are creating the each card view for a house and returning back to HousesActivity
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View houseCardView = convertView;
        if (houseCardView == null) {
            houseCardView = LayoutInflater.from(c).inflate(R.layout.houselist_card, parent, false);
        }

        CardView cardView =  houseCardView.findViewById(R.id.houseCardView);
        cardView.setRadius(5.0f);
        cardView.setCardElevation(20.0f);
        cardView.setBackgroundResource(R.drawable.house_rounded_corner);

        // Setting all the text fields present in the House Card View
        final TextView houseName=(TextView)houseCardView.findViewById(R.id.tv_houseName);
        final TextView houseType=(TextView)houseCardView.findViewById(R.id.tv_typeofhouse);
        final TextView address=(TextView)houseCardView.findViewById(R.id.tv_address);
        final TextView rentAmount=(TextView)houseCardView.findViewById(R.id.tv_rentamount);
        final TextView typeOfSale=(TextView)houseCardView.findViewById(R.id.tv_lease_rent_sale);
        final TextView email=(TextView)houseCardView.findViewById(R.id.tv_youremail);
        final Button chat =(Button)houseCardView.findViewById(R.id.btn_email);
        ImageView imageview=(ImageView)houseCardView.findViewById(R.id.personImage);
        dbr = FirebaseDatabase.getInstance().getReference("HousesList");

        houseName.setText(/*"HOUSE NAME     :"+*/originalArray.get(position).getHousename());
        houseType.setText(/*"TYPEOF HOUSE :"+*/originalArray.get(position).getHousetype());
        address.setText(/*"HOUSE ADDRESS     :"+*/originalArray.get(position).getHouseaddress());
        rentAmount.setText(/*"HOUSE RENT AMOUNT :"+*/originalArray.get(position).getRentamount());
        typeOfSale.setText(/*"LEASE/RENT/SALE :"+*/originalArray.get(position).getLease_rent_sale());
        email.setText(/*"YOUR EMAIL:"+*/originalArray.get(position).getYouremail());

        final String image = originalArray.get(position).getImage();
        Glide.with(c).load(image).into(imageview);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId= originalArray.get(position).getYouremail();
                final String split_first = emailId.substring(0,emailId.indexOf("@"));
                UserDetails.chatWith =  split_first;

                Intent i = new Intent(c, Chats.class);
                c.startActivity(i);
            }
        });

        return houseCardView;
    }

    @Override
    public Filter getFilter() {
        if(cs==null){
            cs=new CustomFilter();
        }
        return cs;
    }


    class CustomFilter extends Filter{
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();
                ArrayList<HouseProperties> filters=new ArrayList<>();
                for(int i=0;i<tempArray.size();i++)
                {
                    if(tempArray.get(i).getHousetype().toUpperCase().contains(constraint))
                    {
                        HouseProperties singleRow =new HouseProperties(tempArray.get(i).getHousename(),tempArray.get(i).getHousetype(),
                                tempArray.get(i).getHouseaddress(),tempArray.get(i).getRentamount(),tempArray.get(i).getLease_rent_sale(),
                                tempArray.get(i).getYouremail(),tempArray.get(i).getImage());

                        filters.add(singleRow);
                    }

                    else if(tempArray.get(i).getHouseaddress().toUpperCase().contains(constraint)){
                        HouseProperties singleRow =new HouseProperties(tempArray.get(i).getHousename(),tempArray.get(i).getHousetype(),
                                tempArray.get(i).getHouseaddress(),tempArray.get(i).getRentamount(),tempArray.get(i).getLease_rent_sale(),
                                tempArray.get(i).getYouremail(),tempArray.get(i).getImage());

                        filters .add(singleRow);
                    }

                    else if(tempArray.get(i).getRentamount().toUpperCase().contains(constraint)){
                        HouseProperties singleRow =new HouseProperties(tempArray.get(i).getHousename(),tempArray.get(i).getHousetype(),
                                tempArray.get(i).getHouseaddress(),tempArray.get(i).getRentamount(),tempArray.get(i).getLease_rent_sale(),
                                tempArray.get(i).getYouremail(),tempArray.get(i).getImage());
                        filters .add(singleRow);

                    }
                    else if(tempArray.get(i).getLease_rent_sale().toUpperCase().contains(constraint)){
                        HouseProperties singleRow =new HouseProperties(tempArray.get(i).getHousename(),tempArray.get(i).getHousetype(),
                                tempArray.get(i).getHouseaddress(),tempArray.get(i).getRentamount(),tempArray.get(i).getLease_rent_sale(),
                                tempArray.get(i).getYouremail(),tempArray.get(i).getImage());
                        filters .add(singleRow);

                    }
                }
                results.count=filters.size();
                results.values=filters;

            }
            else{

                results.count=tempArray.size();
                results.values=tempArray;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            originalArray=(ArrayList<HouseProperties>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}
