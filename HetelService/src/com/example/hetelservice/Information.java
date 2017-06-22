package com.example.hetelservice;

import com.example.hetelservice.MyDatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Information extends Activity implements OnClickListener  {
	
	private TextView chosen_room;
	private EditText name;
	private Spinner myspinner;
	private EditText ID_number;
	private DatePicker date1;
	private DatePicker date2;
	private EditText note;
	
	private Button button1;
	private Button button2;
	
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public String gender_chose;
	public String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_information);
		
		chosen_room = (TextView)findViewById(R.id.number_room);
		name =(EditText)findViewById(R.id.edit_name);
		
		myspinner = (Spinner)findViewById(R.id.choose_gender);
		
		ID_number = (EditText)findViewById(R.id.edit_Id_number);
		date1 = (DatePicker)findViewById(R.id.datapick1);
		date2 = (DatePicker)findViewById(R.id.datapick2);
		note  = (EditText)findViewById(R.id.edit_note);
		
		button1 = (Button)findViewById(R.id.submit);
		button1.setOnClickListener(this);
		button2 = (Button)findViewById(R.id.finish);
		button2.setOnClickListener(this);
		
		
		myspinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?>parent, View view, int position,long id)
			{
				gender_chose = (String)myspinner.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
			
		});
		
		//时间改变监听
		
		SharedPreferences pref = getSharedPreferences("dataroom",MODE_PRIVATE);
		String room_chosen = pref.getString("room_chosen", "未选择房间");
		chosen_room.setText(room_chosen);
		
		//实例数据库helper 建立数据库
		dbHelper = new MyDatabaseHelper(this,"Information.db",null,1);
		db = dbHelper.getWritableDatabase();

	}
	
	@Override
	public void onClick(View v)
	{
		String temp;
		switch (v.getId())
		{
		case R.id.submit:
			//获取数据存入数据库
			ContentValues values = new ContentValues();
			
			temp = chosen_room.getText().toString();
			values.put("room", temp);
			
			temp = name.getText().toString();
			values.put("name", temp);
			
			//temp = gender_chose;
			temp = myspinner.getSelectedItem().toString(); 
			values.put("gender", temp);
			
			temp = ID_number.getText().toString();
			values.put("ID_number", temp);
			
			//temp = "Time";//waiting for improvement
			temp = Integer.toString(date1.getYear())+"."+Integer.toString(date1.getMonth())+"."
					+Integer.toString(date1.getDayOfMonth())+"—"
					+Integer.toString(date2.getYear())+"."+Integer.toString(date2.getMonth())+"."
					+Integer.toString(date2.getDayOfMonth());
			values.put("date", temp);
			
			temp = note.getText().toString();
			values.put("note", temp);
			
			db.insert("information",null,values);   //插入第一条数据
			values.clear();
			Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
			
			//弹出支付对话框
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("请扫描二维码支付");
            //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
            View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
            //设置我们自己定义的布局文件作为弹出框的Content
            builder.setView(view);
               
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
            	@Override
                public void onClick(DialogInterface dialog, int which)
                {
                   } 
             });
             builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
             {
                 @Override
                 public void onClick(DialogInterface dialog, int which)
                 {
                       
                 }
             });
             builder.show();
			
			break;
		case R.id.finish:
			//返回姓名数据 并启动socket程序 发送到robot
			temp = name.getText().toString();
			if(myspinner.getSelectedItem().toString().equals("女"))
				{temp = temp + "女士";}
			else
				{temp = temp + "先生";}
			
			Intent intent = new Intent();   //open the activity for registerin
            intent.putExtra("name_return", temp);
            setResult(RESULT_OK,intent);  //RESULT_OKK 和RESULT_CANCEL
            finish();
			break;
		}
	}
	

}
