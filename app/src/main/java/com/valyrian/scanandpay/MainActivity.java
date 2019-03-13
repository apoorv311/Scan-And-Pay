package com.valyrian.scanandpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,MyAdapter.ListItemClickListener{
    IntentIntegrator scan;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ListItem> listItems;
    private Toast mToast;
    double totalamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String storeName = intent.getStringExtra("storeName");
        if(storeName!=null)
        Toast.makeText(this, "Welcome to " +storeName +"!",
                Toast.LENGTH_LONG).show();

        final PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean beep = sharedPref.getBoolean("beep",true);
        Boolean frontCamera = sharedPref.getBoolean("frontCamera",false);
        int camId;
        totalamt = 0.0;
        if(frontCamera == false)
            camId = 0;
        else
            camId = 1;
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        adapter = new MyAdapter(listItems, this,this);
        recyclerView.setAdapter(adapter);
        CardView cardView = (CardView)findViewById(R.id.cardView);

        Cursor codes = cupboard().withDatabase(db).query(Code.class).orderBy( "_id DESC").getCursor();
        try {

            QueryResultIterable<Code> itr = cupboard().withCursor(codes).iterate(Code.class);
            for (Code bunny : itr) {

                Log.d("p on create name",bunny.pname);
                Log.d("p on create name",Integer.toString(bunny.qty));
                if(bunny.qty > 0) {
                    totalamt+=bunny.qty*bunny.price;
                    ListItem listItem = new ListItem(bunny._id, bunny.name, bunny.pname, bunny.price, bunny.qty);
                    listItems.add(listItem);
                    adapter = new MyAdapter(listItems, this,this);
                    recyclerView.setAdapter(adapter);
                }
            }
        } finally {
            codes.close();
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                totalamt =0.0;
                final int position = viewHolder.getAdapterPosition();
                ListItem item=listItems.get(position);

                Cursor codes = cupboard().withDatabase(db).query(Code.class).orderBy( "_id DESC").getCursor();
                String pnum=item.getName();
                QueryResultIterable<Code> itr = cupboard().withCursor(codes).iterate(Code.class);
                for (Code bunny : itr) {
                    if(bunny.name.equals(pnum)) {
                        bunny.qty = 0;
                        cupboard().withDatabase(db).put(bunny);
                    }
                    else if(bunny.qty>0){
                        totalamt+=bunny.qty*bunny.price;
                    }
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,listItems.size());
                Button totalPrice = (Button) findViewById(R.id.totalPrice);
                String buttontext = "Total Price : " + Double.toString(totalamt);
                totalPrice.setText(buttontext);
            }
        }).attachToRecyclerView(recyclerView);
        scan = new IntentIntegrator(this);
        scan.setBeepEnabled(beep);
        scan.setCameraId(camId);
        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan.initiateScan();
            }
        });
        Button totalPrice = (Button) findViewById(R.id.totalPrice);
        String buttontext = "Total Price : " + Double.toString(totalamt);
        totalPrice.setText(buttontext);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Button totalPrice = (Button) findViewById(R.id.totalPrice);
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if (id == R.id.action_clearAll) {
            totalamt = 0.0;
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
                    //  cupboard().withDatabase(db).delete(Code.class, null);
                    listItems.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    return true;
                }
            }finally {
                codes.close();
            }
            String buttontext = "Total Price : " + Double.toString(totalamt);
            totalPrice.setText(buttontext);
        }
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(this, About.class);
            startActivity(aboutIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Button totalPrice = (Button) findViewById(R.id.totalPrice);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinate), "No Item Scanned", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {

                totalamt = 0.0;
                String pnum = result.getContents();
                // Code codeObj = new Code(result.getContents(),result.getFormatName());
                PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //long id = cupboard().withDatabase(db).put(codeObj);
                listItems.clear();
                adapter.notifyDataSetChanged();
                Cursor codes = cupboard().withDatabase(db).query(Code.class).orderBy( "_id DESC").getCursor();
                boolean ok = true;
                try {
                    QueryResultIterable<Code> itr = cupboard().withCursor(codes).iterate(Code.class);
                    for (Code bunny : itr) {
                        if(bunny.name.equals(pnum)) {
                            bunny.qty = bunny.qty + 1;
                            cupboard().withDatabase(db).put(bunny);
                            ok = false;
                        }
                        if(bunny.qty > 0) {
                            totalamt+=bunny.qty*bunny.price;
                            ListItem listItem = new ListItem(bunny._id, bunny.name, bunny.pname, bunny.price, bunny.qty);
                            listItems.add(listItem);
                            adapter = new MyAdapter(listItems, this,this);
                            recyclerView.setAdapter(adapter);
                        }
                        Log.d("p name",bunny.pname);
                        Log.d("p name",Integer.toString(bunny.qty));
                    }
                } finally {
                    if(ok)
                        Toast.makeText(this, "Invalid Item", Toast.LENGTH_SHORT).show();
                    codes.close();
                }
            }
            String buttontext = "Total Price : " + Double.toString(totalamt);
            totalPrice.setText(buttontext);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void cardClick(View card){
        TextView textView = (TextView)findViewById(R.id.textViewCode);
        String code= textView.getText().toString();
        Log.d("p name value for share",code) ;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, code);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("beep")) {
            scan.setBeepEnabled(sharedPreferences.getBoolean(key,true));
        }
        if (key.equals("frontCamera")) {
            int camId;
            if(sharedPreferences.getBoolean(key,false)== false)
                camId = 0;
            else
                camId = 1;
            scan.setCameraId(camId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex,int flag) {

        Button totalPrice = (Button) findViewById(R.id.totalPrice);
        totalamt = 0.0;
        String pnum = listItems.get(clickedItemIndex).getPname();
        // Code codeObj = new Code(result.getContents(),result.getFormatName());
        PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //long id = cupboard().withDatabase(db).put(codeObj);
        listItems.clear();
        adapter.notifyDataSetChanged();
        Cursor codes = cupboard().withDatabase(db).query(Code.class).orderBy( "_id DESC").getCursor();
        Log.d("p name","1");
        Log.d("p name","2");
        try {
            QueryResultIterable<Code> itr = cupboard().withCursor(codes).iterate(Code.class);
            Log.d("p name","3");
            Log.d("p name","4");
            for (Code bunny : itr) {
                if(bunny.pname.equals(pnum)) {
                    if(flag==0)
                        bunny.qty = bunny.qty + 1;
                    else
                        bunny.qty = bunny.qty - 1;

                    cupboard().withDatabase(db).put(bunny);
                }
                if(bunny.qty==0)
                    continue;
                if(bunny.qty > 0) {
                    totalamt+=bunny.qty*bunny.price;
                    ListItem listItem = new ListItem(bunny._id, bunny.name, bunny.pname, bunny.price, bunny.qty);
                    listItems.add(listItem);
                    adapter = new MyAdapter(listItems, this,this);
                    recyclerView.setAdapter(adapter);
                }
                Log.d("p name",bunny.pname);
                Log.d("p name",Integer.toString(bunny.qty));
            }
            String buttontext = "Total Price : " + Double.toString(totalamt);
            totalPrice.setText(buttontext);
        } finally {
            codes.close();
        }

        if (mToast != null) {
            mToast.cancel();
        }


    }
    public void checkout(View view) {
        Log.d("debug","pahcuha");
        if(listItems.size()>0)
        {
            finish();
            Intent intent = new Intent(this, Checkout.class);
            String total1=Double.toString(totalamt);
            String qty1=Integer.toString(listItems.size());
            intent.putExtra("totalamt", total1);
            intent.putExtra("totalitems", qty1);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "No items in your Cart !",
                    Toast.LENGTH_LONG).show();
        }

    }
}