package wb.pos.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class dbhelper extends SQLiteOpenHelper {

    public dbhelper(Context context) {
        super(context, "sale.db", null, 1);
    }
    String creat_table="create table sale (id integer primary key autoincrement,productname text,price integer,quantity integer,barcode text,email text);";
    String[] columns={"id","productname","price","quantity","barcode"};
    String[] pricecolumn={"Sum(price)"};
    String[] quantity_column={"quantity","price"};
    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(creat_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sale");
        onCreate(db);
    }
     public boolean insert(String name,int price,int quantity,String barcode,String email){
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values=new ContentValues();
         values.put("productname",name);
         values.put("price",price);
         values.put("quantity",quantity);
         values.put("barcode",barcode);
         values.put("email",email);
         long is_inserted=database.insert("sale",null,values);
         return is_inserted != -1;
     }
     public boolean check_if_product_already_exist(String barcode,String email){
         SQLiteDatabase database = this.getReadableDatabase();
         //Cursor products=database.query("sale",columns,"barcode = ? and email = ?",new String[]{barcode,email},null,null,null,null);
         Cursor products=database.rawQuery("SELECT * FROM sale where barcode = ? and email = ?",new String[]{barcode,email});
         //products.close();
         return products.getCount() != 0;
     }
     public int get_all_products_count(String username){
         SQLiteDatabase db= getReadableDatabase();
         Cursor products=db.query("sale",columns,"email = ?",new String[]{username},null,null,null,null);
         return products.getCount();
     }
    public Cursor get_all_products(String username){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("sale",columns,"email = ?",new String[]{username},null,null,null,null);
        //products.close();
        return products;
    }
    public int getTotalOfAmount(String Username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query("sale",pricecolumn,"email = ?",new String[]{Username},null,null,null,null);
        cur.moveToFirst();
        int i = cur.getInt(0);
        cur.close();
        return i;
    }
    public Integer delete(String id){
        SQLiteDatabase sdb = this.getReadableDatabase();
        return sdb.delete("sale", "barcode = ?", new String[] {id});
    }
    public Integer delete(String id,String username){
        SQLiteDatabase sdb = this.getReadableDatabase();
        return sdb.delete("sale", "barcode = ? and email = ?", new String[] {id,username});
    }
    public void delete_all(String Username){
        SQLiteDatabase sdb = this.getReadableDatabase();
        sdb.delete("sale", "email = ?", new String[] {Username});
    }
    }

