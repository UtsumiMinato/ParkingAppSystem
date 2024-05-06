package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Model.FindSpecific;

public class MainActivity extends AppCompatActivity {
    private Button finBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化按鈕
        finBtn = findViewById(R.id.findBtn);

        // 設置按鈕點擊監聽器
        finBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在按鈕點擊時執行的操作
                FindSpecific findSpecific = new FindSpecific();
                findSpecific.querySpot("南港路");
            }
        });
    }
}
