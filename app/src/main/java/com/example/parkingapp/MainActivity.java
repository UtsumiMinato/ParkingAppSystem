package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import Model.FindSpecific;

public class MainActivity extends AppCompatActivity {
    private Button finBtn;
    private SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListner(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                filterList(newText);
                return true;
            }
        });
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

    private void filterList(String Text){
        List<Item> filteredlist = new ArrayList<>();
        for (Item item : itemList){
            if (item.getItemName().toLowerCase.contains(text.toLowerCase())){
                filterList.add(item);
            }
        }

        if (filteredlist.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{

        }
    }
}
