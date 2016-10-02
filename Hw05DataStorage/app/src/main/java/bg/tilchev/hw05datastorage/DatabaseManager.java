package bg.tilchev.hw05datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grocery_store";
    private static final String TABLE_ITEMS = "items";
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";
    private static final String KEY_ITEM_IMG = "img";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDataBaseSQL = "CREATE TABLE " + TABLE_ITEMS + " (" +
                KEY_ITEM_ID + " INT PRIMARY KEY, " +
                KEY_ITEM_TEXT + " VARCHAR(50), " +
                KEY_ITEM_IMG + " MEDIUMTEXT);";
        db.execSQL(createDataBaseSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        this.onCreate(db);
    }

    public void addItem(Item item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ID, item.getId());
        values.put(KEY_ITEM_TEXT, item.getText());
        values.put(KEY_ITEM_IMG, item.getBase64ImgStr());
        database.insert(TABLE_ITEMS, null, values);
        database.close();
    }

    public Item getItem(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                TABLE_ITEMS,
                new String[] { KEY_ITEM_ID, KEY_ITEM_TEXT, KEY_ITEM_IMG },
                KEY_ITEM_ID + " =? ",
                new String[] { Integer.toString(id) },
                null,
                null,
                null,
                null);
        if(cursor == null) {
            database.close();
            return null;
        }
        cursor.moveToFirst();
        int currID = Integer.parseInt(cursor.getString(0));
        String currText = cursor.getString(1);
        String currImg = cursor.getString(2);
        Item currItem = new Item(currID, currText, currImg);
        cursor.close();
        database.close();
        return currItem;
    }

    public List<Item> getAllItems() {
        SQLiteDatabase database = this.getReadableDatabase();
        String rawSQL = "SELECT * FROM " + TABLE_ITEMS;
        Cursor allItemsCursor = database.rawQuery(rawSQL, null);
        if(allItemsCursor == null) {
            database.close();
            return null;
        }
        List<Item> items = new ArrayList<>();
        while (allItemsCursor.moveToNext()) {
            int currID = Integer.parseInt(allItemsCursor.getString(0));
            String currText = allItemsCursor.getString(1);
            String currImg = allItemsCursor.getString(2);
            Item currItem = new Item(currID, currText, currImg);
            items.add(currItem);
        }
        allItemsCursor.close();
        database.close();
        return items;
    }

    public void updateItem(Item item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ID, item.getId());
        values.put(KEY_ITEM_TEXT, item.getText());
        values.put(KEY_ITEM_IMG, item.getBase64ImgStr());
        String id = Integer.toString(item.getId());
        database.update(TABLE_ITEMS, values, KEY_ITEM_ID + " =? ", new String[]{id});
        database.close();
    }

    public int getItemCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        String rawSQL = "SELECT * FROM " + TABLE_ITEMS;
        Cursor allItemsCursor = database.rawQuery(rawSQL, null);
        int count = 0;
        if(allItemsCursor == null) {
            database.close();
            return count;
        }
        count = allItemsCursor.getCount();
        allItemsCursor.close();
        database.close();
        return count;
    }

    public void deleteContact(Item contact) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        database.close();
    }
}
