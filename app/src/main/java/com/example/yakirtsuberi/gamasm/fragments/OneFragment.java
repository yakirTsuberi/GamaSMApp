package com.example.yakirtsuberi.gamasm.fragments;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yakirtsuberi.gamasm.ItemListActivity;
import com.example.yakirtsuberi.gamasm.Managers.FourDigitCardFormatWatcher;
import com.example.yakirtsuberi.gamasm.R;



public class OneFragment extends Fragment {

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        EditText clientCreditCardNumber = view.findViewById(R.id.clientCreditCardNumber);
        clientCreditCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());

        TextView next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ItemListActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


}