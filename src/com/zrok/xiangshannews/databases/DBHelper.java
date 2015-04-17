package com.zrok.xiangshannews.databases;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zrok.xiangshannews.bean.ChannelItem;

/*
 * 
 * Author: zhangr@bluemobi.gz.cn
 * Created Date:2015-1-9
 * Copyright @ 2015 BU
 * Description: 数据库帮助类
 * History:
 */
public class DBHelper extends SQLiteOpenHelper{
	/**
	 * 声明SQLiteDatabase 变量
	 */
	protected SQLiteDatabase databases;
	private static DBHelper sInstance;
	private static final String TAG = "DBHelper";
	private static final int VERSION = 1;
	/**
	 * 数据库名
	 */
    private static final String DB_NAME = "zrok.db";
    /**
     * 关键字表名
     */
    private final String TABLE_NAME_CHANNLE = "ChannelItem";
    
    
	
    public final static String ID = "_id";
    /**
     * 频道字段名
     */
    public static final String NAME = "name";
    public static final String ORDERID = "orderId";
    public static final String SELECTED = "selected";
	
    private final Object mChannelLock = new Object();
	
    /**
     * 
     * @param context 上下文
     * @param name 数据库名
     * @param factory
     * @param version 版本号
     */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		databases = getWritableDatabase();
	}
	
	public static DBHelper getInstence(Context context) {
		if (null == sInstance) {
			sInstance = new DBHelper(context);
		}
		return sInstance;
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		StringBuilder sb=new StringBuilder();
		sb.setLength(0);
		sb.append("CREATE TABLE IF NOT EXISTS "+TABLE_NAME_CHANNLE
				+"("+ID+" INTEGER PRIMARY KEY, "
				+ NAME +" TEXT,"
				+ ORDERID +" TEXT,"
				+ SELECTED+" TEXT);");          
        db.execSQL(sb.toString());
        sb.setLength(0);
        
        
        
        
        db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * 
	 * 方法说明  增加频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param ScheduleEntity
	 * @return
	 */
	public void insertChannel(ChannelItem bean){
		if (null != databases) {
			synchronized (mChannelLock) {
				ContentValues values = new ContentValues();
				values.put(NAME, bean.getName());
				values.put(ORDERID, bean.getOrderId());
				values.put(SELECTED, bean.getSelected());
				databases.insert(TABLE_NAME_CHANNLE, null, values);	
			}
		}
	}
	
	
	/**
	 * 
	 * 方法说明  删除频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param ScheduleEntity
	 * @return
	 */
	public void deleteChannelForId(String id) {
		if (null != databases) {
			final String where = "(" + ID + " = '"+id+"')";
			databases.delete(TABLE_NAME_CHANNLE, where,null);
		}
	}
	
	/**
	 * 
	 * 方法说明  修改频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param  ScheduleEntity
	 * @return
	 */
	public void updateChannelForType(ChannelItem bean,int type){
		if (null != databases) {
			ContentValues values = new ContentValues();
			values.put(NAME, bean.getName());
			values.put(ORDERID, bean.getOrderId());
			values.put(SELECTED, type);
		    final String where = "(" + ID + " = '"+bean.getId()+"')";
		    databases.update(TABLE_NAME_CHANNLE, values, where, null);
		}
	};
	
	/**
	 * 
	 * 方法说明  清空所有数据
	 * @author rui.zhang@bluemobi.cn 2015-2-26
	 * @param 
	 * @return
	 */
	public void clearAllChannel() {
		if (null != databases) {
			final String where = " 1=1 ";
			databases.delete(TABLE_NAME_CHANNLE, where, null);
		}
	}
	
	/**
	 * 
	 * 方法说明  清空user channel
	 * @author rui.zhang@bluemobi.cn 2015-2-26
	 * @param 
	 * @return
	 */
	public void clearChannelForType(int type) {
		if (null != databases) {
			final String where = " "+SELECTED+" = "+type;
			databases.delete(TABLE_NAME_CHANNLE, where, null);
		}
	}

	/**
	 * 
	 * 方法说明  获取所以频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param 
	 * @return
	 */
	public List<ChannelItem> getAllChannel(){
		Cursor cursor = null;
		List<ChannelItem> datas = null;
		ChannelItem bean = null;
		if (null != databases) {
			try {
				cursor = databases.rawQuery("select * from "+TABLE_NAME_CHANNLE, null);
				datas = new ArrayList<ChannelItem>();
				while (cursor.moveToNext()) {
					bean = new ChannelItem();
					bean.setId(cursor.getInt(cursor.getColumnIndex(ID)));
					bean.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					bean.setOrderId(cursor.getInt(cursor.getColumnIndex(ORDERID)));
					bean.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)));
					datas.add(bean);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if (null != cursor) {
					cursor.close();
				}
			}
		}
		return datas;
	}
	
	/**
	 * 
	 * 方法说明  获取用户频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param 
	 * @return
	 */
	public List<ChannelItem> getUserChannels(){
		Cursor cursor = null;
		List<ChannelItem> datas = null;
		ChannelItem bean = null;
		if (null != databases) {
			try {
				cursor = databases.rawQuery("select * from "+TABLE_NAME_CHANNLE+" where "+SELECTED+" =1 order by "+ID, null);
				datas = new ArrayList<ChannelItem>();
				while (cursor.moveToNext()) {
					bean = new ChannelItem();
					bean.setId(cursor.getInt(cursor.getColumnIndex(ID)));
					bean.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					bean.setOrderId(cursor.getInt(cursor.getColumnIndex(ORDERID)));
					bean.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)));
					datas.add(bean);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if (null != cursor) {
					cursor.close();
				}
			}
		}
		return datas;
	}
	
	/**
	 * 
	 * 方法说明  获取其他频道
	 * @author zhangr@bluemobi.gz.cn 2015-1-15
	 * @param 
	 * @return
	 */
	public List<ChannelItem> getOtherChannels(){
		Cursor cursor = null;
		List<ChannelItem> datas = null;
		ChannelItem bean = null;
		if (null != databases) {
			try {
				cursor = databases.rawQuery("select * from "+TABLE_NAME_CHANNLE+" where "+SELECTED+" =0 order by "+ID, null);
				datas = new ArrayList<ChannelItem>();
				while (cursor.moveToNext()) {
					bean = new ChannelItem();
					bean.setId(cursor.getInt(cursor.getColumnIndex(ID)));
					bean.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					bean.setOrderId(cursor.getInt(cursor.getColumnIndex(ORDERID)));
					bean.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)));
					datas.add(bean);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if (null != cursor) {
					cursor.close();
				}
			}
		}
		return datas;
	}

}
