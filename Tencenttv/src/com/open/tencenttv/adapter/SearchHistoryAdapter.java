package com.open.tencenttv.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.tencenttv.R;
import com.open.tencenttv.bean.SearchHistoryBean;

/**
 * *****************************************************************************
 * *****************************************************************************
 * ******************
 * 
 * @author :fengguangjing
 * @createTime: 16/11/3
 * @version:
 * @modifyTime:
 * @modifyAuthor:
 * @description: 
 *               ****************************************************************
 *               ***************************************************************
 *               *********************************************
 */
public class SearchHistoryAdapter extends CommonAdapter<SearchHistoryBean> {
	private final LayoutInflater mInflater;

	public SearchHistoryAdapter(Context mContext, List<SearchHistoryBean> list) {
		super(mContext, list);
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.adapter_search_history, parent, false);
			viewHolder.text_sb_num = (TextView) convertView.findViewById(R.id.text_sb_num);
			viewHolder.text_data_word = (TextView) convertView.findViewById(R.id.text_data_word);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		SearchHistoryBean bean = (SearchHistoryBean) getItem(position);
		if (bean != null) {
			viewHolder.text_sb_num.setText(bean.getC_pos()+"");
			viewHolder.text_data_word.setText(bean.getC_title());
		}
		return convertView;
	}

	class ViewHolder {
		TextView text_sb_num;
		TextView text_data_word;
	}

}