package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarPlateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarPlateFragment extends Fragment {
    private EditText editTextFirst, editTextSecond;
    private Button btnConfirm;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarPlateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarPlate.
     */
    // TODO: Rename and change types and number of parameters
    public static CarPlateFragment newInstance(String param1, String param2) {
        CarPlateFragment fragment = new CarPlateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_plate, container, false);
        editTextFirst = view.findViewById(R.id.editTextFirst);
        editTextSecond = view.findViewById(R.id.editTextSecond);
        btnConfirm = view.findViewById(R.id.confirm_button);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.enterCarPlateAutoComplete);

        String[] schools = {"Group 5 Parking Lot", "04_Parking Lot_4", "03_Parking Lot_3", "02_Parking Lot_2", "01_Parking Lot_1"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, schools);
        autoCompleteTextView.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        return view;
    }

    private void submitData() {
        String amount = editTextFirst.getText().toString();
        String description = editTextSecond.getText().toString();
        String fullMessage = amount + " - " + description;

        Bundle bundle = new Bundle();
        bundle.putString("data", fullMessage);

        ConfirmCarPlateFragment confirmCarPlateFragment = new ConfirmCarPlateFragment();
        confirmCarPlateFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_test, confirmCarPlateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}