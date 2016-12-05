/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-5下午4:21:55
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.open.tencenttv.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.ListViewTV;
import com.open.androidtvwidget.view.MainUpView;
import com.open.tencenttv.BaseV4Fragment;
import com.open.tencenttv.R;
import com.open.tencenttv.adapter.StarNewsTabAdapter;
import com.open.tencenttv.bean.StarFeedBean;
import com.open.tencenttv.json.StarFeedJson;
import com.open.tencenttv.utils.UrlUtils;

/**
 ***************************************************************************************************************************************************************************** 
 * 明星资讯 时间轴
 * 
 * @author :fengguangjing
 * @createTime:2016-12-5下午4:21:55
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class StarNewsTabFragment extends BaseV4Fragment<StarFeedJson> {
	private ListViewTV mListViewTV;
	private List<StarFeedBean> list = new ArrayList<StarFeedBean>();
	private StarNewsTabAdapter mStarNewsTabAdapter;
	private String url;

	public static StarNewsTabFragment newInstance(String url, MainUpView mainUpView1, EffectNoDrawBridge mRecyclerViewBridge, View mOldView) {
		StarNewsTabFragment fragment = new StarNewsTabFragment();
		fragment.url = url;
		fragment.mOldView = mOldView;
		fragment.mRecyclerViewBridge = mRecyclerViewBridge;
		fragment.mainUpView1 = mainUpView1;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_star_news_tab, container, false);
		mListViewTV = (ListViewTV) view.findViewById(R.id.listview);
		doAsync(this, this, this);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mStarNewsTabAdapter = new StarNewsTabAdapter(getActivity(), list);
		mListViewTV.setAdapter(mStarNewsTabAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#call()
	 */
	@Override
	public StarFeedJson call() throws Exception {
		// TODO Auto-generated method stub
		StarFeedJson mStarFeedJson = new StarFeedJson();
		List<StarFeedBean> list = parseStarFeed(url);
		mStarFeedJson.setList(list);
		return mStarFeedJson;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#onCallback(java.lang.Object)
	 */
	@Override
	public void onCallback(StarFeedJson result) {
		// TODO Auto-generated method stub
		super.onCallback(result);
		list.clear();
		list.addAll(result.getList());
		mStarNewsTabAdapter.notifyDataSetChanged();
	}

	private List<StarFeedBean> parseStarFeed(String href) {
		List<StarFeedBean> list = new ArrayList<StarFeedBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i(TAG, "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent).timeout(10000).get();
			Element masthead = doc.select("div.video_tab").first();
			Elements liElements = masthead.select("div.info_list");
			/**
			 */
			// 解析文件
			for (int i = 0; i < liElements.size(); i++) {
				StarFeedBean bean = new StarFeedBean();
				try {
					 
					list.add(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
