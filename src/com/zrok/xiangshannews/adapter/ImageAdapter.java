
package com.zrok.xiangshannews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zrok.xiangshannews.view.NewImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ImageAdapter extends BaseAdapter {

	private List<String> lists = new ArrayList<String>();
    private Context context;

    public ImageAdapter(Context context){
    	this.context = context;
    }
    
    public void appendList(List<String> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewImageView newImageView;
        if (convertView == null) {
            newImageView = new NewImageView(context);
        } else {
            newImageView = (NewImageView) convertView;
        }

        newImageView.setImage(lists, position);

        return newImageView;
    }

}
