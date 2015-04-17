package com.zrok.xiangshannews.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zrok.xiangshannews.R;
import com.zrok.xiangshannews.activity.NewsDetailActivity;
import com.zrok.xiangshannews.activity.NewsImageActivity;
import com.zrok.xiangshannews.adapter.NewsAdapter;
import com.zrok.xiangshannews.bean.NewModle;
import com.zrok.xiangshannews.tools.Constants;
import com.zrok.xiangshannews.tools.StringUtils;
import com.zrok.xiangshannews.view.viewimage.Animations.DescriptionAnimation;
import com.zrok.xiangshannews.view.viewimage.Animations.SliderLayout;
import com.zrok.xiangshannews.view.viewimage.SliderTypes.BaseSliderView;
import com.zrok.xiangshannews.view.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.zrok.xiangshannews.view.viewimage.SliderTypes.TextSliderView;

public class NewsFragment extends Fragment implements OnSliderClickListener{
	private final static String TAG = "NewsFragment";
	Activity activity;
	private List<NewModle> newsList = new ArrayList<NewModle>();
	private NewsAdapter adapter;
	private String text;
	private int channel_id;
	private PullToRefreshListView mPullRefreshListView;
	private LoadControler mLoadControler = null;
	private int index = 0;
	private String urlString;
	private ListView mListView;
	private SliderLayout mDemoSlider;
	
	protected HashMap<String, String> url_maps;

    protected HashMap<String, NewModle> newHashMap;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;
		url_maps = new HashMap<String, String>();
        newHashMap = new HashMap<String, NewModle>();
		System.out.println("------text:"+text+"  channel_id:"+channel_id);
		initData();
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		setUserVisibleHint(true);
		
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(activity, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy(true,false).setLastUpdatedLabel(label);
				newsList.clear();
				index = 0;
				urlString = Constants.getPreUrl(text) + index + Constants.URL.endUrl;
				mLoadControler = RequestManager.getInstance().get(urlString, requestListener, 1);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy(false,true).setRefreshingLabel("加载20条数据");
				index = index + 20;
				urlString = Constants.getPreUrl(text) + index + Constants.URL.endUrl;
				mLoadControler = RequestManager.getInstance().get(urlString, requestListener, 1);
			}
			
		});

		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				NewModle bean = (NewModle) parent.getItemAtPosition(position);
				intent.putExtra("newModle", bean);
				 if (bean.getImagesModle() != null && bean.getImagesModle().getImgList().size() > 1) {
					 intent.setClass(activity,NewsImageActivity.class);   
			    } else {
					intent.setClass(activity,NewsDetailActivity.class);
			    }
				startActivity(intent);
			}
		});
		mListView = mPullRefreshListView.getRefreshableView();
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_item, null);
		mDemoSlider = (SliderLayout) headView.findViewById(R.id.slider);
		mListView.addHeaderView(headView);
		//初次加载列表，显示加载框
		mPullRefreshListView.setRefreshing();
		adapter = new NewsAdapter(activity,newsList);
		mPullRefreshListView.setAdapter(adapter);
		
		
		return view;
	}

	private void initData() {
		newsList.clear();
		urlString = Constants.getPreUrl(text) + index + Constants.URL.endUrl;
		mLoadControler = RequestManager.getInstance().get(urlString, requestListener, 1);
	}
	
	private void initSliderLayout(List<NewModle> newModles) {

        if (!isNullString(newModles.get(0).getImgsrc()))
            newHashMap.put(newModles.get(0).getImgsrc(), newModles.get(0));
        if (!isNullString(newModles.get(1).getImgsrc()))
            newHashMap.put(newModles.get(1).getImgsrc(), newModles.get(1));
        if (!isNullString(newModles.get(2).getImgsrc()))
            newHashMap.put(newModles.get(2).getImgsrc(), newModles.get(2));
        if (!isNullString(newModles.get(3).getImgsrc()))
            newHashMap.put(newModles.get(3).getImgsrc(), newModles.get(3));

        if (!isNullString(newModles.get(0).getImgsrc()))
            url_maps.put(newModles.get(0).getTitle(), newModles.get(0).getImgsrc());
        if (!isNullString(newModles.get(1).getImgsrc()))
            url_maps.put(newModles.get(1).getTitle(), newModles.get(1).getImgsrc());
        if (!isNullString(newModles.get(2).getImgsrc()))
            url_maps.put(newModles.get(2).getTitle(), newModles.get(2).getImgsrc());
        if (!isNullString(newModles.get(3).getImgsrc()))
            url_maps.put(newModles.get(3).getTitle(), newModles.get(3).getImgsrc());

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView.setOnSliderClickListener(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name));

            textSliderView.getBundle()
                    .putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
        //mDemoSlider.stopAutoCycle();
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        adapter.appendList(newModles);
    }
	
	
	private RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(String response, String url, int actionId) {
			System.out.println("------onSuccess---onRefreshComplete---");
			 List<NewModle> list =NewModle.readJsonNewModles(response, Constants.getTypeId(text));
		        if (index == 0 && list.size() >= 4) {
		            initSliderLayout(list);
		        } else {
		        	adapter.appendList(list);
		        }
		        
			newsList.addAll(list);
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}

		@Override
		public void onError(String errorMsg, String url, int actionId) {
			System.out.println("actionId:" + actionId + ", onError!\n" + errorMsg);
		}

		@Override
		public void onRequest() {
			System.out.println("request send...");
		}
	};


	
	
	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if(mLoadControler!=null){
			mLoadControler.cancel();
		}
		System.out.println("onDestroyView...");
		
		super.onDestroyView();

	}
	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("onDestroy...");
		super.onDestroy();
	}
	
	 public boolean isNullString(String imgUrl) {

	        if (StringUtils.isEmpty(imgUrl)) {
	            return true;
	        }
	        return false;
	    }

	@Override
	public void onSliderClick(BaseSliderView slider) {
		System.out.println("-----onSliderClick---");
		NewModle bean = newHashMap.get(slider.getUrl());
		Intent intent = new Intent();
		intent.putExtra("newModle", bean);
		 if (bean.getImagesModle() != null && bean.getImagesModle().getImgList().size() > 1) {
			 intent.setClass(activity,NewsImageActivity.class);   
	    } else {
			intent.setClass(activity,NewsDetailActivity.class);
	    }
		startActivity(intent);
	}
}
