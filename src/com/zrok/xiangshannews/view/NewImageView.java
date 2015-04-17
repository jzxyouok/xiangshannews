
package com.zrok.xiangshannews.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.zrok.xiangshannews.R;
import com.zrok.xiangshannews.tools.Options;

public class NewImageView extends RelativeLayout implements ImageLoadingListener,
        ImageLoadingProgressListener {

    protected ImageView currentImage;

    protected TextView currentPage;

    protected ProgressButton progressButton;

    protected CompoundButton.OnCheckedChangeListener checkedChangeListener;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;

    protected Context context;
    
    private View view;

    public NewImageView(Context context) {
        this(context,null);
        this.context = context;
        options = Options.getListOptions();
        progress(context);
    }
    
    public NewImageView(Context context, AttributeSet attrs) {    
         super(context, attrs);
         //在构造函数中将Xml中定义的布局解析出来。   
         view = LayoutInflater.from(context).inflate(R.layout.item_image, this, true);
         currentImage = (ImageView) view.findViewById(R.id.current_image);
         currentPage = (TextView) view.findViewById(R.id.current_page);
         progressButton = (ProgressButton) view.findViewById(R.id.progressButton);
    } 

    public void setImage(List<String> imgList, int position) {
        currentPage.setText((position + 1) + "/" + imgList.size());
        imageLoader.displayImage(imgList.get(position), currentImage, options, this,
                this);
    }

    public void initView() {
        progressButton.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        progressButton.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        progressButton.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        progressButton.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {

        int currentpro = (current * 100 / total);

        if (currentpro == 100) {
            progressButton.setVisibility(View.GONE);
        } else {
            progressButton.setVisibility(View.VISIBLE);
        }
        if (currentpro >= 0 && currentpro <= 100) {
            progressButton.setProgress(currentpro);
        }
        updatePinProgressContentDescription(progressButton, context);

    }

    public void progress(final Context context) {
        checkedChangeListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        updatePinProgressContentDescription((ProgressButton) compoundButton,
                                context);
                    }
                };
    }

    /**
     * Helper method to update the progressButton's content description.
     */
    private void updatePinProgressContentDescription(ProgressButton button, Context context) {
        int progress = button.getProgress();
        if (progress <= 0) {
            button.setContentDescription(context.getString(
                    button.isChecked() ? R.string.content_desc_pinned_not_downloaded
                            : R.string.content_desc_unpinned_not_downloaded));
        } else if (progress >= button.getMax()) {
            button.setContentDescription(context.getString(
                    button.isChecked() ? R.string.content_desc_pinned_downloaded
                            : R.string.content_desc_unpinned_downloaded));
        } else {
            button.setContentDescription(context.getString(
                    button.isChecked() ? R.string.content_desc_pinned_downloading
                            : R.string.content_desc_unpinned_downloading));
        }
    }

}
