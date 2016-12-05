/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-5下午4:37:55
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.open.tencenttv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.tencenttv.R;
import com.open.tencenttv.bean.StarFeedBean;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-5下午4:37:55
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class StarNewsTabAdapter extends CommonAdapter<StarFeedBean>{

	public StarNewsTabAdapter(Context mContext, List<StarFeedBean> list) {
		super(mContext, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StarFeedBean bean = (StarFeedBean) getItem(position);
		View view;
		view = LayoutInflater.from(mContext).inflate(R.layout.adapter_star_news_tab, null);
		return view;
	}
}
