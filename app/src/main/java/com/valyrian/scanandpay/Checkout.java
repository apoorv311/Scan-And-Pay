package com.valyrian.scanandpay;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Checkout extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        String totalamt = intent.getStringExtra("totalamt");
        String qty = intent.getStringExtra("totalitems");
        Log.d("totalamt",totalamt);
        Log.d("totalqty",qty);
        TextView price_qty = (TextView) findViewById(R.id.qtyPrice);
        String s="Items: "+qty+"\n"+"Bill Amount: $"+ totalamt;
        price_qty.setText(s);
        Log.d("string",s);
        PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor codes = cupboard().withDatabase(db).query(Code.class).orderBy( "_id DESC").getCursor();
        try {
            if (codes.getCount() > 0) {
                QueryResultIterable<Code> itr = cupboard().withCursor(codes).iterate(Code.class);
                for (Code bunny : itr) {
                    bunny.qty = 0;
                    cupboard().withDatabase(db).put(bunny);
                }

            }
        }finally {
            codes.close();
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
