package com.example.parkingapp;



import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Map;

public class SelectCreditFragment extends Fragment {
    private Map<Integer, String> paymentMethods;
    private LinearLayout paymentMethodView;

    public SelectCreditFragment() {
        // Required empty public constructor
    }

    public static SelectCreditFragment newInstance(String param1, String param2) {
        SelectCreditFragment fragment = new SelectCreditFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentMethods = ((MainActivity) getActivity()).getPaymentMethods();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_credit, container, false);
        paymentMethodView = view.findViewById(R.id.paymentMethodView);
        updatePaymentMethodsView(); // 更新UI

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

    @Override
    public void onResume() {
        super.onResume();
        updatePaymentMethodsView();
    }

    private void updatePaymentMethodsView() {
        paymentMethodView.removeAllViews(); // 清除所有現有頁面
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (Map.Entry<Integer, String> entry : paymentMethods.entrySet()) {
            View paymentView = inflater.inflate(R.layout.payment_method_item, paymentMethodView, false);
            Button button = paymentView.findViewById(R.id.paymentButton);
            button.setText(entry.getValue());
            // 動態設置按鈕的左邊圖片
            button.setCompoundDrawablesWithIntrinsicBounds(entry.getKey(), 0, R.drawable.baseline_arrow_forward_ios_24, 0);
            paymentMethodView.addView(paymentView);

            View divider = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            divider.setLayoutParams(layoutParams);
            divider.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
            paymentMethodView.addView(divider);
        }
    }
}