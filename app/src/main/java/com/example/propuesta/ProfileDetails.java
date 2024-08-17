package com.example.propuesta;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class ProfileDetails extends Fragment {

    Button btnlogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile_details, container, false);

       btnlogout = view.findViewById(R.id.btnlogout);
       btnlogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Logout();
           }
       });

       return view;
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent =new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        getActivity().finish();
    }
}