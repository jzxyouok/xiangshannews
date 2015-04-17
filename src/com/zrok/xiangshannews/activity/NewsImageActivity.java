package com.zrok.xiangshannews.activity;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zrok.xiangshannews.R;
import com.zrok.xiangshannews.adapter.ImageAdapter;
import com.zrok.xiangshannews.bean.NewDetailModle;
import com.zrok.xiangshannews.bean.NewModle;
import com.zrok.xiangshannews.view.flipview.FlipView;
import com.zrok.xiangshannews.view.flipview.FlipView.OnFlipListener;
import com.zrok.xiangshannews.view.flipview.FlipView.OnOverFlipListener;
import com.zrok.xiangshannews.view.flipview.OverFlipMode;

/**
 * 
 * Author: rui.zhang@bluemobi.cn
 * Created Date:2015-4-10
 * Copyright @ 2015 BU
 * Description: 新闻图片
 * History:
 */
public class NewsImageActivity extends Activity implements OnFlipListener,
OnOverFlipListener{

	private TextView new_title,title;
	private FlipView flip_view;
	
	protected ImageAdapter imageAdapter;
	private NewModle newModle;
	private List<String> imgList;
    private NewDetailModle newDetailModle;
    private String titleString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		flip_view = (FlipView) findViewById(R.id.flip_view);
		new_title = (TextView) findViewById(R.id.new_title);
		title = (TextView) findViewById(R.id.title);
		title.setText("新闻图片");
		
		if (getIntent().getExtras().getSerializable("newDetailModle") != null) {
            newDetailModle = (NewDetailModle) getIntent().getExtras().getSerializable("newDetailModle");
            imgList = newDetailModle.getImgList();
            titleString = newDetailModle.getTitle();
        } else {
            newModle = (NewModle) getIntent().getExtras().getSerializable("newModle");
            imgList = newModle.getImagesModle().getImgList();
            titleString = newModle.getTitle();
        }
		initData();
	}
	
	private void initData(){
		new_title.setText(titleString);
		imageAdapter = new ImageAdapter(this);
        imageAdapter.appendList(imgList);
        flip_view.setOnFlipListener(this);
        flip_view.setAdapter(imageAdapter);
        // mFlipView.peakNext(false);
        flip_view.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        flip_view.setOnOverFlipListener(this);
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		// TODO Auto-generated method stub
		
	}
}
