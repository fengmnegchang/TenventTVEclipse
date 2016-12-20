package com.open.tencenttv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.open.tencenttv.R;
import com.open.tencenttv.TencentTVWebViewActivity;
import com.open.tencenttv.bean.SliderNavBean;
import com.open.tencenttv.utils.UrlUtils;

/**
 * ****************************************************************************************************************************************************************************
 *
 * @author :fengguangjing
 * @createTime: 16/11/17
 * @version:
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
public class MediumPagerAdapter extends CommonPagerAdapter<SliderNavBean> {

    public MediumPagerAdapter(Context mContext, List<SliderNavBean> list) {
		super(mContext, list);
	}
    
    @Override
	public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_medium_pager, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView textView = (TextView) view.findViewById(R.id.textview);

        final SliderNavBean sliderNavBean = getItem(position);
        textView.setText(sliderNavBean.getTitle());
        if (sliderNavBean.getImageUrl() != null && sliderNavBean.getImageUrl().length() > 0) {
//            mImageLoader.DisplayImage(sliderNavBean.getImageUrl(), imageView);
            DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grid_view_item_test)
                    .showImageForEmptyUri(R.drawable.grid_view_item_test)
                    .showImageOnFail(R.drawable.grid_view_item_test).cacheInMemory().cacheOnDisc()
                    .build();
            ImageLoader.getInstance().displayImage(UrlUtils.TENCENT_IMAGE_URL+sliderNavBean.getImageUrl(), imageView,options,getImageLoadingListener());
        }
        imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TencentTVWebViewActivity.startTencentTVWebViewActivity(mContext, sliderNavBean.getHrefUrl());
			}
		});
        container.addView(view);
        return view;
    }
     
}
