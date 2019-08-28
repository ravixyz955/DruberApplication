package com.example.user.druberapplication;

import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;

import com.example.user.druberapplication.network.model.NamesList;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;

public class ChipsActivity extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Chip chip;
    private ChipsInput chipsInput;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chips_thirdparty);

        chipsInput = findViewById(R.id.chips_input);
        names = new ArrayList<>();

        chipsInput.setFilterableList(getList());

        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
//                Toast.makeText(ChipsActivity.this, "Added: " + chip.getLabel(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(ChipsActivity.this, "AddedSize: " + newSize, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
//                Toast.makeText(ChipsActivity.this, "Removed: " + chip.getLabel(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(ChipsActivity.this, "Removedsize: " + newSize, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence text) {

            }
        });

        /*
        //this is used with activity_chips.xml

        chipGroup = findViewById(R.id.chipGroup);
        chip = findViewById(R.id.chip);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    Toast.makeText(ChipsActivity.this, "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Close is Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private List<? extends ChipInterface> getList() {
        ArrayList<NamesList> namesLists = new ArrayList<>();
        namesLists.add(new NamesList("1", null, "Abyssinian", "1234"));
        namesLists.add(new NamesList("2", null, "Beetle", "1234"));
        namesLists.add(new NamesList("3", null, "Chinchilla", "1234"));
        namesLists.add(new NamesList("4", null, "Discus", "1234"));
        namesLists.add(new NamesList("5", null, "Entlebucher", "1234"));
        namesLists.add(new NamesList("6", null, "Flounder", "1234"));
        namesLists.add(new NamesList("7", null, "Galapagos", "1234"));
        namesLists.add(new NamesList("8", null, "Hummingbird", "1234"));
        namesLists.add(new NamesList("9", null, "Impala", "1234"));
        namesLists.add(new NamesList("10", null, "Jaguar", "1234"));
        namesLists.add(new NamesList("11", null, "Kingfisher", "1234"));
        namesLists.add(new NamesList("12", null, "Leopard", "1234"));
        namesLists.add(new NamesList("13", null, "Mongoose", "1234"));
        namesLists.add(new NamesList("14", null, "Nightingale", "1234"));
        namesLists.add(new NamesList("15", null, "Opossum", "1234"));
        return namesLists;
    }
}