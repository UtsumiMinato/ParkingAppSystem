package com.example.parkingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class ConfirmCeditFragment extends Fragment {
    private EditText cardNumberEditText;
    private Button saveButton;
    private Map<Integer, String> paymentMethods;

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

        cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        saveButton = view.findViewById(R.id.saveBtn);

        saveButton.setOnClickListener(v -> saveCardInfo());

        return view;
    }

    private void saveCardInfo() {
        String cardNumber = cardNumberEditText.getText().toString().trim();
        if (cardNumber.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid card number", Toast.LENGTH_SHORT).show();
            return;
        }

        // 确定卡片类型和格式化文本
        int drawableId;
        String cardText;
        if (cardNumber.startsWith("4")) {
            drawableId = R.drawable.visa;
            cardText = "VISA •••• " + cardNumber.substring(cardNumber.length() - 4);
        } else if (cardNumber.startsWith("5")) {
            drawableId = R.drawable.master;
            cardText = "MASTER •••• " + cardNumber.substring(cardNumber.length() - 4);
        } else {
            Toast.makeText(getContext(), "Unsupported card type", Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加到支付方法数据中，从 MainActivity 获取 Map 实例
        ((MainActivity) getActivity()).getPaymentMethods().put(drawableId, cardText);

        // 可能需要通知更新界面或者返回
        Toast.makeText(getContext(), "Card added successfully", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack(); // 返回上一个 Fragment
    }
}
