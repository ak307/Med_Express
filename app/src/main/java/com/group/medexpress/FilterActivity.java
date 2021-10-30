package com.group.medexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private Chip surgical, alcohol, testKit;
    private Button applyBtn;
    private ArrayList<String> selectedChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        surgical = (Chip) findViewById(R.id.surgicalChip);
        alcohol = (Chip) findViewById(R.id.alcoholChip);
        testKit = (Chip) findViewById(R.id.testKitChip);
        applyBtn = (Button) findViewById(R.id.filterApplyBtn);

        selectedChips = new ArrayList<>();


        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedChips.add(buttonView.getText().toString());
                }
                else {
                    selectedChips.remove(buttonView.getText().toString());
                }
            }
        };

        surgical.setOnCheckedChangeListener(checkedChangeListener);
        alcohol.setOnCheckedChangeListener(checkedChangeListener);
        testKit.setOnCheckedChangeListener(checkedChangeListener);


        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedChipData", selectedChips);
                setResult(101, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("selectedChipData", selectedChips.add("~"));
        setResult(101, intent);
        finish();
        super.onBackPressed();

    }
}