package com.zrok.xiangshannews.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zrok.xiangshannews.R;
import com.zrok.xiangshannews.app.AppApplication;
import com.zrok.xiangshannews.bean.NewModle;
import com.zrok.xiangshannews.tools.Constants;
import com.zrok.xiangshannews.view.ViewHolder;

public class NewsAdapter extends BaseAdapter{
	
	private Context context;
	public List<NewModle> newsList = new ArrayList<NewModle>();
	public NewsAdapter(Context context, List<NewModle> newsList) {
		this.newsList = newsList;
		this.context = context;
	}
	
	public void appendList(List<NewModle> list) {
        if (!newsList.containsAll(list) && list != null && list.size() > 0) {
        	newsList.addAll(list);
        }
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList == null ? 0 : newsList.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_new, null);
		}
		RelativeLayout title_layout = ViewHolder.get(convertView, R.id.title_layout);
		LinearLayout layout_image = ViewHolder.get(convertView, R.id.layout_image);
		ImageView left_image = ViewHolder.get(convertView, R.id.left_image); 
		TextView item_title = ViewHolder.get(convertView, R.id.item_title);
		TextView item_content = ViewHolder.get(convertView, R.id.item_content);
		TextView item_abstract = ViewHolder.get(convertView, R.id.item_abstract);
		ImageView item_image_0 = ViewHolder.get(convertView, R.id.item_image_0);
		ImageView item_image_1 = ViewHolder.get(convertView, R.id.item_image_1);
		ImageView item_image_2 = ViewHolder.get(convertView, R.id.item_image_2);
		
		NewModle bean = newsList.get(position);

		 if (bean.getImagesModle() == null) {
			 title_layout.setVisibility(View.VISIBLE);
			 layout_image.setVisibility(View.GONE);
			 ImageLoader.getInstance().displayImage(bean.getImgsrc(), left_image, AppApplication.getApp().getOptions());
			 item_title.setText(bean.getTitle());
			 item_content.setText(bean.getDigest());
		 }else{
			 title_layout.setVisibility(View.GONE);
			 layout_image.setVisibility(View.VISIBLE);
			 item_abstract.setText(bean.getTitle());
			   List<String> imageModle = bean.getImagesModle().getImgList();
			   ImageLoader.getInstance().displayImage(imageModle.get(0), item_image_0, AppApplication.getApp().getOptions());
			   ImageLoader.getInstance().displayImage(imageModle.get(1), item_image_1, AppApplication.getApp().getOptions());
			   ImageLoader.getInstance().displayImage(imageModle.get(2), item_image_2, AppApplication.getApp().getOptions());
		 }
		
		
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return newsList.get(position);
	}

}
