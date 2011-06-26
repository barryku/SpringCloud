package com.barryku.android.instapaper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static com.barryku.android.instapaper.InstapaperActivity.LOG_TAG;


public class UrlLinkDataManager extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "links.db" ;
	
	public static final String TABLE_NAME = "links";
	public static final String COLUMN_LINK = "link";
	public static final String COLUMN_UPDATED = "updated";
	
	public UrlLinkDataManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LINK
				+ " TEXT NOT NULL, " + COLUMN_UPDATED  
				+ " INTEGER);" );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
		// TODO Auto-generated method stub

	}
	
	public List<UrlLink> getLinks() {
		List<UrlLink> links = new ArrayList<UrlLink>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, new String[]{BaseColumns._ID, COLUMN_LINK, COLUMN_UPDATED}, null, 
					null, null, null, COLUMN_UPDATED);
			while (cursor.moveToNext()) {
				UrlLink link = new UrlLink();
				link.setId(cursor.getInt(0));
				link.setLink(cursor.getString(1));
				link.setLastUpdated(cursor.getInt(2));
				links.add(link);
			}
			cursor.close();
		} finally {
			close();
		}
		return links;
	}
	
	public void removeLink(UrlLink link) {
		Log.d(LOG_TAG, "removing link: " + link);
		removeLinkById(link.getId());
	}
	
	public void removeLinkById(int id) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE_NAME, BaseColumns._ID + "= ?", new String[]{String.valueOf(id)});
		}  catch (Exception e) {
			Log.d(LOG_TAG, e.getMessage());
		}
		finally {
			close();
		}
	}
	
	public void addLink(String link) {
		Log.d(LOG_TAG, "add link: " + link);
		try {
			SQLiteDatabase db = getWritableDatabase();
			Log.d(LOG_TAG, "add '" + link + "' into:" + db);
			ContentValues values = new ContentValues();
			values.put(COLUMN_UPDATED, System.currentTimeMillis());
			values.put(COLUMN_LINK, link);
			db.insertOrThrow(TABLE_NAME, null, values);
		}  catch (Exception e) {
			Log.d(LOG_TAG, e.getMessage());
		}
		finally {
			close();
		}
	}

}
