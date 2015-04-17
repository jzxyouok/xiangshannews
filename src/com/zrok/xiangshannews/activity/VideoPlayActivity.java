package com.zrok.xiangshannews.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import com.zrok.xiangshannews.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * Author: rui.zhang@bluemobi.cn
 * Created Date:2015-4-10
 * Copyright @ 2015 BU
 * Description: 视频播放
 * History:
 */
public class VideoPlayActivity extends Activity implements OnInfoListener,
OnBufferingUpdateListener, OnPreparedListener{
	
	private VideoView mVideoView;
	protected ProgressBar mProgressBar;
	protected TextView mLoadRate;
	protected ImageView mVideoEnd;
	private Uri uri;
    private String playUrl;
    private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_play_videobuffer);
		mVideoView = (VideoView) findViewById(R.id.buffer);
		mProgressBar = (ProgressBar) findViewById(R.id.probar);
		mLoadRate = (TextView) findViewById(R.id.load_rate);
		mVideoEnd = (ImageView) findViewById(R.id.video_end);
		
		if (!LibsChecker.checkVitamioLibs(this))
            return;
		
		
		playUrl = getIntent().getExtras().getString("playUrl");
        title = getIntent().getExtras().getString("filename");
        if ("".equals(playUrl) || playUrl == null) {
            Toast.makeText(getApplicationContext(), "请求地址错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        uri = Uri.parse(playUrl);
        mVideoView.setVideoURI(uri);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(this);
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		mediaPlayer.setPlaybackSpeed(1.0f);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		// TODO Auto-generated method stub
		mLoadRate.setText(percent + "%");
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		System.out.println(what);
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLoadRate.setText("");
                    mLoadRate.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                // mVideoEnd.setVisibility(View.VISIBLE);
                mVideoView.start();
                mProgressBar.setVisibility(View.GONE);
                mLoadRate.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                break;
        }
        return true;
	}
}
