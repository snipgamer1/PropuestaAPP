package com.example.propuesta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link-Fragment} subclass.
 * Use the {@link-MainActivityAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityAdmin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main_admin, container, false);
        return  view;
    }
}