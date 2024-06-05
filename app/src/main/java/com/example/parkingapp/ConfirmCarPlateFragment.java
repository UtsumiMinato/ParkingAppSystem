package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmCarPlateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmCarPlateFragment extends Fragment {
    private TextView textViewResult;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmCarPlateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment confirm_carplate.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmCarPlateFragment newInstance(String param1, String param2) {
        ConfirmCarPlateFragment fragment = new ConfirmCarPlateFragment();
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
        View view = inflater.inflate(R.layout.fragment_confirm_carplate, container, false);
        textViewResult = view.findViewById(R.id.carPlate_View);

        // 從arguments取得數據
        Bundle args = getArguments();
        if (args != null) {
            String data = args.getString("data");
            textViewResult.setText(data);
        }

        return view;
    }
}