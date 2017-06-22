package com.example.hetelservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{

	public static final String CREATE_Information = "create table information("
			+"id integer primary key autoincrement,"
			+"room text,"
			+"name text,"
			+"gender text,"
			+"ID_number text,"
			+"date text,"
			+"note text)";
	
	private Context mContext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory 
			factory,int version)
	{
		super(context, name, factory, version);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_Information);//调用execSQL()方法执行建表语句
		//Toast.makeText(mContext, "create success", Toast.LENGTH_SHORT).show(); //建表
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)  //very important for upgrading data
	{
		//db.execSQL("drop table if exists Book");
		//db.execSQL("drop table if exists Category");//这种更新数据的方法并不是最好的的方法 过于机械
		//onCreate(db);
		
		//执行 时 版本号 >1;
	}
			
}