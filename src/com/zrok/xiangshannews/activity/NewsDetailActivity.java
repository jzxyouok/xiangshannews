package com.zrok.xiangshannews.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.zrok.xiangshannews.R;
import com.zrok.xiangshannews.app.AppApplication;
import com.zrok.xiangshannews.bean.NewDetailModle;
import com.zrok.xiangshannews.bean.NewModle;
import com.zrok.xiangshannews.tools.Constants;
import com.zrok.xiangshannews.tools.DialogUtil;
import com.zrok.xiangshannews.tools.Options;
import com.zrok.xiangshannews.view.ProgressPieView;
import com.zrok.xiangshannews.view.html.HtmlTextView;

/**
 * 
 * Author: rui.zhang@bluemobi.cn
 * Created Date:2015-4-10
 * Copyright @ 2015 BU
 * Description: 新闻详情 
 * History:
 */
public class NewsDetailActivity extends Activity implements ImageLoadingListener,ImageLoadingProgressListener{

	private TextView img_count,new_title,new_time,title;
	private ProgressPieView progressPieView;
	private ImageView new_img,play;
	private HtmlTextView wb_details;
	private Dialog progressDialog;
	
	private String newUrl;
    private NewModle newModle;
    private String newID;
    private String imgCountString;
    private NewDetailModle newDetailModle;
    protected DisplayImageOptions options;
    private LoadControler mLoadControler = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		title = (TextView) findViewById(R.id.title);
		img_count = (TextView) findViewById(R.id.img_count);
		new_title = (TextView) findViewById(R.id.new_title);
		new_time = (TextView) findViewById(R.id.new_time);
		progressPieView = (ProgressPieView) findViewById(R.id.progressPieView);
		new_img = (ImageView) findViewById(R.id.new_img);
		play = (ImageView) findViewById(R.id.play);
		wb_details = (HtmlTextView) findViewById(R.id.wb_details);
		title.setText("新闻详情");
		newModle = (NewModle) getIntent().getExtras().getSerializable("newModle");
        newID = newModle.getDocid();
        newUrl = Constants.getUrl(newID);
        options = Options.getListOptions();
	
		new_img.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
            	imageMore(view);
            }

        }
        );

		
		initWebView();
	}
	
	public void initWebView() {
        try {
        	progressPieView.setShowText(true);
        	progressPieView.setShowImage(false);
        	
            
            loadData(newUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void setNewsView(){
		if (newDetailModle == null)
            return;
		
		if (!"".equals(newDetailModle.getUrl_mp4())) {
            ImageLoader.getInstance().displayImage(newDetailModle.getCover(), new_img, options,this,this);
            new_img.setVisibility(View.VISIBLE);
        } else {
            if (newDetailModle.getImgList().size() > 0) {
                imgCountString = "共" + newDetailModle.getImgList().size() + "张";
                String imageUrl = newDetailModle.getImgList().get(0);
                ImageLoader.getInstance().displayImage(imageUrl, new_img, options,this,this);
                new_img.setVisibility(View.VISIBLE);
            }
        }
		new_title.setText(newDetailModle.getTitle());
		new_time.setText("来源：" + newDetailModle.getSource() + " " + newDetailModle.getPtime());
        String content = newDetailModle.getBody();
        content = content.replace("<!--VIDEO#1--></p><p>", "");
        content = content.replace("<!--VIDEO#2--></p><p>", "");
        content = content.replace("<!--VIDEO#3--></p><p>", "");
        content = content.replace("<!--VIDEO#4--></p><p>", "");
        content = content.replace("<!--REWARD#0--></p><p>", "");
        wb_details.setHtmlFromString(content, false);
		
	}
	
	
	
	private void loadData(String url){
		
		mLoadControler = RequestManager.getInstance().get(url, requestListener, 1);
	}
	
	
	private RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(String response, String url, int actionId) {
			dismissProgressDialog();
			
			newDetailModle = NewDetailModle.readJsonNewDetailModles(response, newID);
			setNewsView();
		}

		@Override
		public void onError(String errorMsg, String url, int actionId) {
			dismissProgressDialog();
			Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequest() {
			showProgressDialog();
		}
	};

	public void imageMore(View view) {
        try {
        	Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("newDetailModle", newDetailModle);
            if (!"".equals(newDetailModle.getUrl_mp4())) {
                bundle.putString("playUrl", newDetailModle.getUrl_mp4());
                intent.setClass(getApplicationContext(), VideoPlayActivity.class);
            	intent.putExtras(bundle);
            	startActivity(intent);
            } else {
            	intent.setClass(getApplicationContext(), NewsImageActivity.class);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	/**
     * 显示dialog
     * 
     * @param msg 显示内容
     */
    public void showProgressDialog() {
        try {

            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(this);

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏dialog
     */
    public void dismissProgressDialog() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dialog是否显示
     */
    public boolean isShow() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub
		progressPieView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		progressPieView.setVisibility(View.GONE);
		
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		if (!"".equals(newDetailModle.getUrl_mp4())) {
			play.setVisibility(View.VISIBLE);
        } else {
        	img_count.setVisibility(View.VISIBLE);
        	img_count.setText(imgCountString);
        }
		progressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub
		progressPieView.setVisibility(View.GONE);
	}

	@Override
	public void onProgressUpdate(String imageUri, View view, int current,
			int total) {
		// TODO Auto-generated method stub
		int currentpro = (current * 100 / total);
        if (currentpro == 100) {
        	progressPieView.setVisibility(View.GONE);
        	progressPieView.setShowText(false);
        } else {
        	progressPieView.setVisibility(View.VISIBLE);
        	progressPieView.setProgress(currentpro);
        	progressPieView.setText(currentpro + "%");
        }
	}
	
	
	/**
     * 返回
     */
    public void doBack(View view) {
        onBackPressed();
    }
}
