package com.example.improvedhouse.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.improvedhouse.Login;
import com.example.improvedhouse.R;
import com.example.improvedhouse.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;


/**
 * This activity handles the Profile tab. In this, user can choose any one of the below options:
 *      1. Add a new house for sale
 *      2. View all the houses added by the user
 *      3. Logout from the app
 *
 */
public class ProfileActivity extends Fragment {
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_tab, container, false);
        final TextView buttonLogout = rootView.findViewById(R.id.buttonLogout);
        final TextView addHouse = rootView.findViewById(R.id.addHouse);
        final TextView myHouse = rootView.findViewById(R.id.myHouse);
        final TextView Name = rootView.findViewById(R.id.Name);

        // Setting the name of the user on the Profile Page
        Name.setText(UserDetails.username);

        firebaseAuth= FirebaseAuth.getInstance();

        // If the users chooses to logout, we will sign-out the user and take them to login page
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        // If the users chooses add a new house, then we take them to HouseRegistration Page
        addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HouseRegistration.class));
            }
        });

        // If the user chooses to view their houses, we take them to ViewHouses Page
        myHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ViewMyHouses.class));
            }
        });

        return rootView;
    }
}
