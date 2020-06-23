package com.example.casher.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.casher.view.activity.MainActivity;
import com.example.casher.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Planning extends Fragment {

    public Planning() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.planning));
        ((MainActivity) getActivity()).setBellVisibility(false);
        ((MainActivity) getActivity()).getSupportActionBar().setElevation(20);

        return inflater.inflate(R.layout.fragment_planning, container, false);
    }
}
