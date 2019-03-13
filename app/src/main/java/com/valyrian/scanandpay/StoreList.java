package com.valyrian.scanandpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StoreList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        Intent intent = getIntent();
        String storeDist1 = intent.getStringExtra("storeDist1");
        String storeDist2 = intent.getStringExtra("storeDist2");
        String storeDist3 = intent.getStringExtra("storeDist3");
        String storeDist4 = intent.getStringExtra("storeDist4");

        String[] myArray = {storeDist1, storeDist2, storeDist3, storeDist4};
        Arrays.sort(myArray);

        Log.d("storeDist1",myArray[0]);
        Log.d("storeDist2",myArray[1]);
        Log.d("storeDist3",myArray[2]);
        Log.d("storeDist4",myArray[3]);

        Map< String,String> hm =
                new HashMap< String,String>();
        hm.put(storeDist1,"A");
        hm.put(storeDist2,"B");
        hm.put(storeDist3,"C");
        hm.put(storeDist4,"D");

        TextView store1 = (TextView) findViewById(R.id.store1);
        TextView store2 = (TextView) findViewById(R.id.store2);
        TextView store3 = (TextView) findViewById(R.id.store3);
        TextView store4 = (TextView) findViewById(R.id.store4);

        store1.setText("  Store "+hm.get(myArray[0])+":                    "+myArray[0]+" Km");
        store2.setText("  Store "+hm.get(myArray[1])+":                    "+myArray[1]+" Km");
        store3.setText("  Store "+hm.get(myArray[2])+":                    "+myArray[2]+" Km");
        store4.setText("  Store "+hm.get(myArray[3])+":                    "+myArray[3]+" Km");

    }
}