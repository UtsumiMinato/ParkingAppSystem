package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private Button finBtn;
    private SearchView searchView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 SearchView
//        searchView = findViewById(R.id.editTextText);
//        if (searchView != null) {
//            searchView.clearFocus();
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
////                    filterList(query);
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        } else {
//            Toast.makeText(this, "SearchView not found in layout.", Toast.LENGTH_SHORT).show();
//        }

        // 初始化按钮
//        finBtn = findViewById(R.id.findBtn);
//        if (finBtn != null) {
//            finBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 在按钮点击时执行的操作
//                    FindSpecific findSpecific = new FindSpecific();
//                    findSpecific.queryParkingLots("南港路");
//                }
//            });
//        } else {
//            Toast.makeText(this, "Button not found in layout.", Toast.LENGTH_SHORT).show();
//        }
    }

//    private void filterList(String text) {
//        List<Item> filteredList = new ArrayList<>();
//        for (Item item : itemList) {
//            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//            }
//        }
//
//        if (filteredList.isEmpty()) {
//            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
//        } else {
//            // Update the UI with the filtered list
//        }
//    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請開始說話...");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                if (searchView != null) {
                    searchView.setQuery(spokenText, true);
                } else {
                    Toast.makeText(this, "SearchView not found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
