package com.example.hetelservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	//Socket (IP & PORT)
    private String IP_URL = "192.168.1.100";
    private int IP_PORT = 9088;
    
    private final int MSG_GET_DATA= 1001;
    
    private Socket MySocketClient = null;
    private Thread MyThreadClient = null;
    
    private int send_name_count = 0;  //事件标记
    private int delete_name_count = 0;
    
    boolean endflag= true;
    
    private char[] Cmd = {'2','3','4','5','7'};  //for test
    
    //使用sharedpreferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    
    //Button
    private Button room_101;
    private Button room_102;
    private Button room_103;
    private Button room_104;
    private Button room_105;
    
    private Button room_201;
    private Button room_202;
    private Button room_203;
    private Button room_204;
    private Button room_205;
    
    private Button room_301;
    private Button room_302;
    private Button room_303;
    private Button room_304;
    private Button room_305;
    
    private Button register;
    
    public int[] isEmpty ={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    
    //varibale to count the number of rooms  关于初始化的问题 其他 是否放在oncreate中
    public int room_four = 5;
    public int room_two  = 10;
    public String room_number = "";
    public String name_return = "";
    public String recv="";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		room_four = 5;
		room_two =  10;
	
		room_101 = (Button)findViewById(R.id.room101);
		room_101.setOnClickListener(this);
		room_102 = (Button)findViewById(R.id.room102);
		room_102.setOnClickListener(this);
		room_103 = (Button)findViewById(R.id.room103);
		room_103.setOnClickListener(this);
		room_104 = (Button)findViewById(R.id.room104);
		room_104.setOnClickListener(this);
		room_105 = (Button)findViewById(R.id.room105);
		room_105.setOnClickListener(this);
		
		room_201 = (Button)findViewById(R.id.room201);
		room_201.setOnClickListener(this);
		room_202 = (Button)findViewById(R.id.room202);
		room_202.setOnClickListener(this);
		room_203 = (Button)findViewById(R.id.room203);
		room_203.setOnClickListener(this);
		room_204 = (Button)findViewById(R.id.room204);
		room_204.setOnClickListener(this);
		room_205 = (Button)findViewById(R.id.room205);
		room_205.setOnClickListener(this);
		
		room_301 = (Button)findViewById(R.id.room301);
		room_301.setOnClickListener(this);
		room_302 = (Button)findViewById(R.id.room302);
		room_302.setOnClickListener(this);
		room_303 = (Button)findViewById(R.id.room303);
		room_303.setOnClickListener(this);
		room_304 = (Button)findViewById(R.id.room304);
		room_304.setOnClickListener(this);
		room_305 = (Button)findViewById(R.id.room305);
		room_305.setOnClickListener(this);	
		
		register = (Button)findViewById(R.id.register);
		register.setOnClickListener(this);	
		
        endflag = true;  //子线程
	    MyThreadClient = new Thread(MyRunnable);
		MyThreadClient.start();
		
		pref = getSharedPreferences("dataroom",MODE_PRIVATE);
		editor = pref.edit();
		
		editor.putInt("room_2", room_two);
		editor.putInt("room_4", room_four);
		editor.putString("room_chosen","");
		editor.commit();
		
		for(int i=0; i<15;i++)
		{
			int remember = 0;
			remember = pref.getInt("room"+Integer.toString(i+1), 0);
			if(remember == 1)
			{
				button_on_off(i,false,1);
			}
		}
		
	}	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		//floor 1
		case R.id.room101:
			ChangeColor(1,room_101);
			break;
			
		case R.id.room102:
			ChangeColor(2,room_102);
			break;
			
		case R.id.room103:
			ChangeColor(3,room_103);
			break;
			
		case R.id.room104:
			ChangeColor(4,room_104);
			break;
			
		case R.id.room105:
			ChangeColor(5,room_105);
			break;
			
		//floor 2	
		case R.id.room201:
			ChangeColor(6,room_201);
			break;
			
		case R.id.room202:
			ChangeColor(7,room_202);
			break;
			
		case R.id.room203:
			ChangeColor(8,room_203);
			break;
			
		case R.id.room204:
			ChangeColor(9,room_204);
			break;
			
		case R.id.room205:
			ChangeColor(10,room_205);
			break;
			
		//floor 3
		case R.id.room301:
			ChangeColor(11,room_301);
			break;
			
		case R.id.room302:
			ChangeColor(12,room_302);
			break;
			
		case R.id.room303:
			ChangeColor(13,room_303);
			break;
			
		case R.id.room304:
			ChangeColor(14,room_304);
			break;
			
		case R.id.room305:
			ChangeColor(15,room_305);
			break;
			
		case R.id.register:
			editor.commit();
			room_number="";
			Intent setIntent = new Intent();   //open the activity for registering
            setIntent.setClass(this,Information.class);
            startActivityForResult(setIntent,1); //请求码
            
            //锁定按键
            for(int i =0;i<15;i++)
            {
            	if(isEmpty[i]==1)
            	{          		
            		button_on_off(i,false,1);
            	}
            	editor.commit();
           	
            }  	
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		switch(requestCode){
		case 1:
			if(resultCode == RESULT_OK)
			{
				name_return = data.getStringExtra("name_return");
				send_name_count = 1;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
///---------------------Socket线程----------------------///
    private Runnable MyRunnable	= new Runnable(){
    	@Override
    	public void run(){	
    		while(endflag)
    		{
    			if(MySocketClient == null||MySocketClient.isConnected()== false)
    			{
    				try
    				{  
    					MySocketClient = new Socket("172.16.3.79",9088); //79
    					BufferedWriter writer = new BufferedWriter(
    							new OutputStreamWriter(MySocketClient.getOutputStream())); //bufferedWriter
    					
    					BufferedReader  reader = new BufferedReader(
    							new InputStreamReader(MySocketClient.getInputStream()));// budderedReader
    					
    					InputStream isRead = MySocketClient.getInputStream(); 
    					
    					while(MySocketClient.isConnected())  //确保一直是连上的
    					{
    						//MySocketClient.sendUrgentData(0xFF);
    						if(send_name_count>0)  //[1] 情况1：返回姓名信息
    						{
    							writer.write(name_return);
    							writer.flush();  
    					
    							send_name_count--;
    							Thread.sleep(150);	  
    						}
    						
    						//[2] 情况2： 其他事件接收  数据的接收
    						if(isRead.available()>0)
    						{
    							String name_info = "";
    							byte[] buffer = new byte[isRead.available()]; 
    					    	isRead.read(buffer);  
    					    	name_info = new String(buffer);  // 转换为字符串
    					    	
    					    	name_info = name_info.substring(0, name_info.length()-2);
    					    	
    					    	//editor.putString("name_info",name_info);
    					    	//editor.commit();
    					    	
    					    	Message msg = new Message();
								msg.what = MSG_GET_DATA;
								msg.obj = name_info;
								Myhandler.sendMessage(msg);
    					    	
    					    	//if(name_info.equals("my"))
    					    	//data_delete(name_info);				    		
    						}
    						
    					}
    					
    					MySocketClient.close();     //如果断开了重建  close 会自动调用一次flush()
    				}	
    				catch (Exception ex){
    					MySocketClient = null;
				    }
    			}
    		}
    		if(!MySocketClient.isClosed())
				try {
					MySocketClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	};

///--------------------Handlder 中处理子线程UI-------------------/////
	Handler Myhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg){
			Log.i("MAIN","handle internal Message,id="+msg.what);
			switch(msg.what)
			{
			
			case MSG_GET_DATA:
				data_delete(msg.obj.toString());  //取出String值 delete 数据库值；
				break;
				
			default:
				break;
			}
		}
	};

///-------------------Change  ButtonColor---------------------///
	public void ChangeColor(int i,Button button)
	{
		String roomnumber =" ";
		
		if(isEmpty[i-1]==0)
		{
			button.setBackgroundColor(Color.parseColor("#EE0000"));
			isEmpty[i-1]=1;
			
			if(i<=5) {
				room_two--;
				roomnumber = "room10"+Integer.toString(i);
			}
			if((i>5)&&(i<=10)){
				room_two--;
				roomnumber = "room20"+Integer.toString(i-5);
			}
			if(i>10){
				room_four--;
				roomnumber ="room30"+Integer.toString(i-10);
			}		
			room_number = room_number+roomnumber+"  ";
		}
		else
		{
			button.setBackgroundColor(Color.parseColor("#ADD8E6"));
			isEmpty[i-1]=0;
			
			if(i<=5) {
				room_two++;
				roomnumber = "room10"+Integer.toString(i);
			}
			if((i>5)&&(i<=10)){
				room_two++;
				roomnumber = "room20"+Integer.toString(i-5);
			}
			if(i>10){
				room_four++;
				roomnumber = "room30"+Integer.toString(i-10);  //litte error 注意
			}	
			room_number = room_number.replace(roomnumber, "");
		}	
		editor.putInt("room_2", room_two);
		editor.putInt("room_4", room_four);
	    editor.putString("room_chosen", room_number);
	    
	    if(!room_number.contains("r"))
	    	  editor.putString("room_chosen", "未选择房间");
	    		
	}

///------------------------删除数据库----------------------------------///
	void data_delete(String s)
	{
		MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"Information.db",null,1);
		SQLiteDatabase mydb = dbHelper.getWritableDatabase();
		String room_get="";
		
		Cursor cursor = mydb.query("information", null, "name = ?", new String[]{s}, null, null, null);
		if (cursor.moveToFirst()) 
		{
			do {
			// 遍历Cursor对象
			room_get = cursor.getString(cursor.getColumnIndex("room"));
			
			editor.putString("room_info",room_get);
	    	editor.commit();
			
			}while (cursor.moveToNext());
		}
		cursor.close();
		
		for(int i = 1;i<16;i++)
		{
			if(i<6)
			{
				if(room_get.equals("room10"+Integer.toString(i)+"  "))
					button_on_off(i-1,true,0);
			}
			if((i<11)&&(i>5))
			{
				if(room_get.equals("room20"+Integer.toString(i-5)+"  "))
					button_on_off(i-1,true,0);
			}
					
			if((i<16)&&(i>10))
			{
				if(room_get.equals("room30"+Integer.toString(i-10)+"  "))
					button_on_off(i-1,true,0);
			}		
		}		
		mydb.delete("information", "name = ?", new String[]{s});
	}
	
///---------------------Button 的可见性-------------------------------///
	void button_on_off(int s, boolean is,int rem)
	{		
		if(s==0) 
		{
			room_101.setClickable(is);
			if(is == false)  room_101.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_101.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room1", rem);
		}
	
		if(s==1)
		{
			room_102.setClickable(is);
			if(is == false)  room_102.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_102.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room2", rem);
		}
	
		if(s==2)
		{
			room_103.setClickable(is);
			if(is == false)  room_103.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_103.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room3", rem);
		}
		if(s==3)
		{
			room_104.setClickable(is);
			if(is == false)  room_104.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_104.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room4", rem);
		}
		if(s==4)
		{
			room_105.setClickable(is);
			if(is == false)  room_105.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_105.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room5", rem);
		}
		if(s==5)
		{
			room_201.setClickable(is);
			if(is == false)  room_201.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_201.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room6", rem);
		}
		if(s==6)
		{
			room_202.setClickable(is);
			if(is == false)  room_202.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_202.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room7", rem);
		}
		if(s==7)
		{
			room_203.setClickable(is);
			if(is == false)  room_203.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_203.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room8", rem);
		}
		if(s==8)
		{
			room_204.setClickable(is);
			if(is == false)  room_204.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_204.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room9", rem);
		}
		if(s==9)
		{
			room_205.setClickable(is);
			if(is == false)  room_205.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_205.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room10", rem);
		}
		if(s==10)
		{
			room_301.setClickable(is);
			if(is == false)  room_301.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_301.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room11", rem);
		}
		if(s==11)
		{
			room_302.setClickable(is);
			if(is == false)  room_302.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_302.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room12", rem);
		}
		if(s==12)
		{
			room_303.setClickable(is);
			if(is == false)  room_303.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_303.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room13", rem);
		}
		if(s==13)
		{
			room_304.setClickable(is);
			if(is == false)  room_304.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_304.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room14", rem);
		}
		if(s==14) 
		{
			room_305.setClickable(is);
			if(is == false)  room_305.setBackgroundColor(Color.parseColor("#EE0000"));
			if(is == true )  room_305.setBackgroundColor(Color.parseColor("#ADD8E6"));
			editor.putInt("room15", rem);
		}
		
		editor.commit();
	}
	
///------------------------重写on-key-down-----------------------------///
    private long exitTime = 0; 
    public boolean onKeyDown(int keyCode, KeyEvent event){  
		 if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
			 if((System.currentTimeMillis()-exitTime) > 2500)
			 {  //System.currentTimeMillis()>2500
				 Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();                                  
		         exitTime = System.currentTimeMillis();
		     }  
			 else{ 
				 endflag = false;
				 finish();  
				 System.exit(0);  
		     }  
			 return true;  
		 }  
		 return super.onKeyDown(keyCode, event);  
    }  

}
