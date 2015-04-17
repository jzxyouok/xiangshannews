package com.zrok.xiangshannews.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;

import com.zrok.xiangshannews.bean.ChannelItem;
import com.zrok.xiangshannews.databases.DBHelper;
import com.zrok.xiangshannews.tools.PreferencesUtils;

public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	
	private List<ChannelItem> channels;
	
	private Context context;
	
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "头条", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "军事", 2, 1));
        defaultUserChannels.add(new ChannelItem(3, "娱乐", 3, 1));
        defaultUserChannels.add(new ChannelItem(4, "体育", 4, 1));
        defaultUserChannels.add(new ChannelItem(5, "财经", 5, 1));
        defaultUserChannels.add(new ChannelItem(6, "科技", 6, 1));
        defaultUserChannels.add(new ChannelItem(18, "电影", 12, 1));
        defaultUserChannels.add(new ChannelItem(19, "NBA", 13, 1));
        defaultUserChannels.add(new ChannelItem(20, "数码", 14, 1));
        defaultUserChannels.add(new ChannelItem(21, "移动", 15, 1));
        // defaultUserChannels.add(new ChannelItem(7, "图片", 1, 0));
        defaultOtherChannels.add(new ChannelItem(7, "CBA", 1, 0));
        defaultOtherChannels.add(new ChannelItem(8, "笑话", 2, 0));
        defaultOtherChannels.add(new ChannelItem(9, "汽车", 3, 0));
        defaultOtherChannels.add(new ChannelItem(10, "时尚", 4, 0));
        defaultOtherChannels.add(new ChannelItem(11, "北京", 5, 0));
        defaultOtherChannels.add(new ChannelItem(12, "足球", 6, 0));
        defaultOtherChannels.add(new ChannelItem(13, "房产", 7, 0));
        defaultOtherChannels.add(new ChannelItem(14, "游戏", 8, 0));
        defaultOtherChannels.add(new ChannelItem(15, "精选", 9, 0));
        defaultOtherChannels.add(new ChannelItem(16, "电台", 10, 0));
        defaultOtherChannels.add(new ChannelItem(17, "情感", 11, 0));
        
        //defaultUserChannels.add(new ChannelItem(22, "彩票", 16, 0));
        //defaultUserChannels.add(new ChannelItem(23, "教育", 17, 0));
        defaultOtherChannels.add(new ChannelItem(24, "论坛", 18, 0));
        defaultOtherChannels.add(new ChannelItem(25, "旅游", 19, 0));
        defaultOtherChannels.add(new ChannelItem(26, "手机", 20, 0));
        defaultOtherChannels.add(new ChannelItem(27, "博客", 21, 0));
        defaultOtherChannels.add(new ChannelItem(28, "社会", 22, 0));
        defaultOtherChannels.add(new ChannelItem(29, "家居", 23, 0));
        defaultOtherChannels.add(new ChannelItem(30, "暴雪", 24, 0));
        //defaultUserChannels.add(new ChannelItem(31, "亲子", 25, 0));
	}
	
	public ChannelManage(Context context){
		this.context = context;
		initChannelData();
	}
	
	
	/**
	 * 初始化频道管理类
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(Context context)throws SQLException {
		if (channelManage == null){
			channelManage = new ChannelManage(context);
		}
		return channelManage;
	}
	
	public List<ChannelItem> getUserChannel() {
		channels = DBHelper.getInstence(context).getUserChannels();
		if(channels==null&&channels.size()<0){
			return defaultUserChannels;
		}
		return channels;
	}
	
	private void initChannelData(){
		//保存默认频道
		ChannelItem bean = null;
		int count1 = defaultUserChannels.size();
		int count2 = defaultOtherChannels.size();
		int count_user = PreferencesUtils.getInt(context, "channel_user");
		int count_other = PreferencesUtils.getInt(context, "channel_other");
		if(count1!=count_user){
			//清空user channel数据
			DBHelper.getInstence(context).clearChannelForType(1);
			//插入新数据
			for(int i =0;i<count1;i++){
				bean = defaultUserChannels.get(i);
				DBHelper.getInstence(context).insertChannel(bean);
			}
			PreferencesUtils.putInt(context, "channel_user", count1);
			
		}
		
		if(count2!=count_other){
			//清空other channel数据
			DBHelper.getInstence(context).clearChannelForType(0);
			
			//保存其他频道
			for(int i =0;i<count2;i++){
				bean = defaultOtherChannels.get(i);
				DBHelper.getInstence(context).insertChannel(bean);
			}
			PreferencesUtils.putInt(context, "channel_other", count1);
		}
	}
	
	 /**
     * 保存用户频道到数据库
     * 
     * @param userList
     */
    public void saveUserChannel(List<ChannelItem> userList) {
    	ChannelItem bean = null;
        for (int i = 0; i < userList.size(); i++) {
        	bean = userList.get(i);
        	bean.setOrderId(i);
        	bean.setSelected(1);
            DBHelper.getInstence(context).insertChannel(bean);
        }
    }
    
    /**
     * 保存其他频道到数据库
     * 
     * @param otherList
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
    	ChannelItem bean = null;
        for (int i = 0; i < otherList.size(); i++) {
        	bean = otherList.get(i);
        	bean.setOrderId(i);
        	bean.setSelected(0);
            DBHelper.getInstence(context).insertChannel(bean);
        }
    }
    
    
	
}
