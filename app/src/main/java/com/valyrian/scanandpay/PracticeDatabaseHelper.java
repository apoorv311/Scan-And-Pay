package com.valyrian.scanandpay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class PracticeDatabaseHelper  extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "cupboardTest.db";
    private static final int DATABASE_VERSION = 1;

    public PracticeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        cupboard().register(Code.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();

        /* Creation of Database */

        Code codeObj = new Code("6204579921","Apple",2.0,0);
        long id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579922","Banana",4.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579923","Mango",1.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579924","Orange",2.5,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579925","Tomato",20.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579926","Onion",21.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579927","Potato",12.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579928","Capsicum",3.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579929","Beans",32.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579930","Carrot",42.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579931","Cabbage",12.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579932","Cauliflower",23.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579933","Beetroot",23.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579934","Lady Finger",12.0,0);
        id = cupboard().withDatabase(db).put(codeObj);
        codeObj = new Code("6204579935","Litchi",20.0,0);
        id = cupboard().withDatabase(db).put(codeObj);

        /* End of Creation of Database */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

}
