package com.camant.moneycrab.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sreng on 11/13/2016.
 */

public class MoneyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "money.db";
    private static final int VERSION = 4;
    public static final String TABLE_CURRENCIES = "currencies";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_CATEGORY_TYPES = "category_types";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_ICONS = "icons";
    private Context context;

    public MoneyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        buildStructure(sqLiteDatabase);
        createDefaults(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //context.getAssets().list("");

        dropStructure(sqLiteDatabase);
        buildStructure(sqLiteDatabase);
        createDefaults(sqLiteDatabase);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropStructure(db);
        buildStructure(db);
        createDefaults(db);
    }

    private void buildStructure(SQLiteDatabase db){
        db.execSQL("CREATE TABLE `accounts` ( " +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` TEXT, " +
                "`currency` INTEGER DEFAULT 1, " +
                "`alt` TEXT, " +
                "`icon` TEXT, " +
                "`created` INTEGER, " +
                "`modified` INTEGER )");
        db.execSQL("CREATE TABLE `categories` ( " +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` TEXT, " +
                "`ctype` INTEGER DEFAULT 1, " +
                "`icon` TEXT, " +
                "`alt` TEXT, " +
                "`created` INTEGER, " +
                "`modified` INTEGER )");
        db.execSQL("CREATE TABLE `currencies` ( " +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`name` TEXT, " +
                "`alt` TEXT, " +
                "`created` INTEGER, " +
                "`modified` INTEGER )");
        db.execSQL("CREATE TABLE `category_types` ( " +
                "`id` INTEGER PRIMARY KEY, " +
                "`name` TEXT, " +
                "`created` INTEGER, " +
                "`modified` INTEGER )");
        db.execSQL("CREATE TABLE `transactions` ( " +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`note` TEXT, " +
                "`t_in` REAL, " +
                "`t_out` REAL, " +
                "`rate` REAL, " +
                "`t_date` TEXT, " +
                "`created` INTEGER, " +
                "`modified` INTEGER," +
                "`category_id` INTEGER," +
                "`account_id` INTEGER )");
        db.execSQL("CREATE TABLE `icons`(" +
                " `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `name` TEXT," +
                " `alt` TEXT," +
                " `path` TEXT," +
                " `created` INTEGER," +
                " `modified` INTEGER)");
    }
    private void dropStructure(SQLiteDatabase db){
        db.execSQL("drop table `accounts`;");
        db.execSQL("drop table `categories`;");
        db.execSQL("drop table `currencies`;");
        db.execSQL("drop table `category_types`;");
        db.execSQL("drop table `transactions`;");
        db.execSQL("drop table `icons`;");
    }
    private void createDefaults(SQLiteDatabase db){
        db.execSQL("INSERT INTO category_types values" +
                "(1, 'EXPENSES','2016-11-13','2016-11-13')" +
                ", (2, 'INCOMES','2016-11-13','2016-11-13')");
        db.execSQL("INSERT INTO categories values" +
                "(1, 'Food',1,'icon.jpg','Food for living','2016-11-16','2016-11-16')" +
                ", (2, 'Sport',1,'icon.jpg','Sport for health','2016-11-16','2016-11-16')"+
                ", (3, 'Saving',2,'icon.jpg','Saving from some expenses','2016-11-16','2016-11-16')" +
                ", (4, 'Salary',2,'icon.jpg','Salary from working','2016-11-16','2016-11-16')");
        db.execSQL("INSERT INTO currencies values" +
                "(1, 'RIEL','Khmer Riel','2016-11-16','2016-11-16')" +
                ", (2, 'USD','USA Dollar','2016-11-16','2016-11-16')");
        db.execSQL("INSERT INTO accounts values" +
                "(1, 'Home',1,'Khmer Riel','icon.jpg','2016-11-16','2016-11-16')" +
                ", (2, 'CamAnt',1,'USA Dollar','icon.jpg','2016-11-16','2016-11-16')");
        db.execSQL("INSERT INTO icons VALUES" +
                " (1, 'Food', 'Food for home use', 'food.png','2016-11-29', '2016-11-29')" +
                ", (2, 'Car', 'Own car or motor', 'car.png','2016-11-29', '2016-11-29')" +
                ", (3, 'Sport', 'Sports', 'football.png','2016-11-29', '2016-11-29')" +
                ", (4, 'Taxi', 'Taxi and motodup', 'taxi.png','2016-11-29', '2016-11-29')" +
                ", (5, 'Party', 'Party', 'party.png','2016-11-29', '2016-11-29')" );
    }
}
