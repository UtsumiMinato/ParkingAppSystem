package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Corrected import statement
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import Model.FindSpecific;

public class MainActivity extends AppCompatActivity {
    private Button finBtn;
    private SearchView searchView; // Fixed variable name
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // Corrected method name
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                findSpecific.queryParkingLots("南港路");
            }
        });
    }

    private void filterList(String text) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) { // Fixed method call
                filteredList.add(item); // Corrected method call
            }
        }

        if (filteredList.isEmpty()) { // Fixed variable name
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            // Update the UI with the filtered list
        }
    }

    // 啟動語音輸入
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請開始說話...");
        startActivityForResult(intent, 100); // Corrected method call
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Corrected method signature
        super.onActivityResult(requestCode, resultCode, data); // Corrected method call
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                searchView.setQuery(spokenText, true);
            }
        }
    }
}
