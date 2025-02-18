package com.eternity.bystro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BystroDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bystro.db";
    private static final int DATABASE_VERSION = 1;
    public static final String CREATE_PRODUCT_LIST_TABLE =
            "CREATE TABLE product_list ("+
                    "productid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "name VARCHAR(200), "+
                    "type VARCHAR(50), "+
                    "price VARCHAR(7), "+
                    "image INTEGER, "+
                    "description TEXT, "+
                    "stock INTEGER);";
    public static final String CREATE_ADDRESS_TABLE =
            "CREATE TABLE address ("+
                    "addressid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "username VARCHAR(50), "+
                    "useraddress TEXT);";
    public static final String CREATE_ORDER_TABLE =
            "CREATE TABLE orders ("+
                    "orderid INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "productid INTEGER, "+
                    "totalprice INTEGER, "+
                    "quantity INTEGER, "+
                    "delivery INTEGER, "+
                    "addressid INTEGER, "+
                    "payment VARCHAR(10), "+
                    "status VARCHAR(50), "+
                    "FOREIGN KEY (productid) REFERENCES product_list(productid),"+
                    "FOREIGN KEY (addressid) REFERENCES address(addressid));";

    public BystroDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_LIST_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS product_list");
        db.execSQL("DROP TABLE IF EXISTS address");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }
    public void addProduct(String[][] productlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        for (String[] product : productlist) {
            int productid = Integer.parseInt(product[0]);
            String name = product[1];
            String type = product[2];
            String price = product[3];
            int image = Integer.parseInt(product[4]);
            String desc = product[5];
            int quantity = Integer.parseInt(product[6]);

            ContentValues values = new ContentValues();
            values.put("productid", productid);
            values.put("name", name);
            values.put("type", type);
            values.put("price", price);
            values.put("image", image);
            values.put("description", desc);
            values.put("stock", quantity);

            db.insert("product_list", null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void addNewAddress(String username, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("useraddress",address);
        db.insert("address", null, values);
    }
    public void updateAddress(String addressusername, String addreses, int addressid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",addressusername);
        values.put("useraddress",addreses);
        db.update("address",values,"addressid = ?",new String[]{String.valueOf(addressid)});
    }
    public void deleteAddress(int addressid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("address","addressid = ?",new String[]{String.valueOf(addressid)});
    }
    public void selectAddress(int addressid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues temp = new ContentValues();
            temp.put("addressid", -1);
            db.update("address", temp, "addressid = ?", new String[]{String.valueOf(1)});

            ContentValues toTop = new ContentValues();
            toTop.put("addressid", 1);
            db.update("address", toTop, "addressid = ?", new String[]{String.valueOf(addressid)});

            ContentValues toOriginal = new ContentValues();
            toOriginal.put("addressid", addressid);
            db.update("address", toOriginal, "addressid = ?", new String[]{"-1"});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void makeOrder(int productid, int addressid, int quantity, String payment, int totalprice, int delivery, int prestock) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productid",productid);
        values.put("addressid",addressid);
        values.put("totalprice",totalprice);
        values.put("quantity",quantity);
        values.put("delivery",delivery);
        values.put("payment",payment);
        values.put("status","Processing Order");
        ContentValues updatestock = new ContentValues();
        int stock = prestock - quantity;
        updatestock.put("stock",stock);
        db.insert("orders",null,values);
        db.update("product_list",updatestock,"productid = ?",new String[]{String.valueOf(productid)});
    }
    public Cursor getAllTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT product_list.*, address.*, orders.orderid, orders.quantity, orders.payment, orders.status, orders.totalprice, orders.delivery "+
                "FROM product_list, address, orders "+
                "WHERE product_list.productid = orders.productid "+
                "AND address.addressid = orders.addressid;";
        return db.rawQuery(query,null);
    }
    public void cancelorders(int orderid, int productid, int stock, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatestock = new ContentValues();
        int prestock = stock + quantity;
        updatestock.put("stock",prestock);
        db.update("product_list",updatestock,"productid = ?",new String[]{String.valueOf(productid)});
        db.delete("orders","orderid = ?",new String[]{String.valueOf(orderid)});
    }
    public Cursor getTableValues(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        return db.rawQuery(query, null);
    }
    public void recreateTable(String tableName, String createTableQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL(createTableQuery);
    }
}
