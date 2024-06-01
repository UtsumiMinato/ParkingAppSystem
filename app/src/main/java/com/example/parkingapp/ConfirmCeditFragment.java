package com.example.parkingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ConfirmCeditFragment extends Fragment {

    public ConfirmCeditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_credit, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 檢查該片段是否可以從返回堆疊中彈出
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();// 將頂部狀態從返回堆疊中彈出
                } else if (getActivity() != null) {
                    getActivity().finish();// 如果傳回堆疊中沒有更多條目，則結束活動
                }
            }
        });

        return view;
    }
}
