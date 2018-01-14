package ru.samsung.itschool.dbgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper{
	/*
	 * TABLES:
	 *
	 * RESULTS _id INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, SCORE INTEGER
	 *
	 * USERS _id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT,  PIC TEXT
	 */
	private static final String DB_NAME = "game.db";
	private static final int  DB_VERSION = 1;

	private SQLiteDatabase db;

	public DBManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		db = this.getWritableDatabase();
	}

	void addResult(String username, int score) {
		int userid = getPlayerIDByName(username);
		if (userid == -1) {
			// если пользователь новый
			// добавляем его в таблицу
			db.execSQL("INSERT INTO USERS (NAME) VALUES ('" + username + "');");
		}
		// теперь он точно есть
		userid = getPlayerIDByName(username);
		// добавляем данные в таблицу
		db.execSQL("INSERT INTO RESULTS (USERID, SCORE) VALUES ('" + userid + "', '" + score
				+ "');");
	}

	ArrayList<Result> getAllResults() {

		ArrayList<Result> data = new ArrayList<Result>();
		Cursor cursor = db
				.rawQuery(
						"SELECT USERS.NAME AS USERNAME, RESULTS.SCORE AS SCORE FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS._id",
						null);
		boolean hasMoreData = cursor.moveToFirst();

		while (hasMoreData) {
			String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
			int score = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("SCORE")));
			data.add(new Result(name, score));
			hasMoreData = cursor.moveToNext();
		}
		return data;
	}

	Cursor getAll() {

		return db.rawQuery(
						"SELECT USERS._id AS _id, USERS.NAME AS USERNAME, RESULTS.SCORE AS SCORE FROM RESULTS INNER JOIN USERS ON RESULTS.USERID = USERS._id",
						null);


	}

	int getPlayerIDByName(String username) {
		Cursor cursor = db.rawQuery("SELECT _id FROM USERS WHERE NAME='"
				+ username + "'", null);
		if (!cursor.moveToFirst()) {
			return -1;
		}
		return cursor.getInt(cursor.getColumnIndex("_id"));
	}

	void userUpdate(int userid, String username, String pic)
	{
		db.execSQL("UPDATE USERS SET NAME = '"+ username +"', PIC = '"+ pic + "' WHERE _id = '" + userid + "';");
	}

	String getUserName(int userid) {
		Cursor cursor = db.rawQuery("SELECT NAME FROM USERS WHERE _id='"
				+ userid + "'", null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}

	String getUserPic(int userid) {
		Cursor cursor = db.rawQuery("SELECT PIC FROM USERS WHERE _id='"
				+ userid + "'", null);
		cursor.moveToFirst();
		//Если нет фото - возвращаем пустую строку
		if (cursor.isNull(0)) return "";
		else return cursor.getString(0);
	}

	private void createTablesIfNeedBe() {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE RESULTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, SCORE INTEGER);");
		db.execSQL("CREATE TABLE USERS   (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PIC TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
