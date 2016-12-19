/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-9上午11:38:02
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.open.tencenttv.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.ListViewTV;
import com.open.androidtvwidget.view.MainUpView;
import com.open.tencenttv.BaseV4Fragment;
import com.open.tencenttv.R;
import com.open.tencenttv.adapter.UserSubscribeListViewAdapter;
import com.open.tencenttv.bean.UserVFollowlstBean;
import com.open.tencenttv.json.UserVFollowlstJson;
import com.open.tencenttv.utils.UrlUtils;

/**
 ***************************************************************************************************************************************************************************** 
 * 订阅
 * 
 * @author :fengguangjing
 * @createTime:2016-12-9上午11:38:02
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class UserSubscribeFragment extends BaseV4Fragment<UserVFollowlstJson, UserSubscribeFragment> {
	private String url = "http://c.v.qq.com/vfollowlst?otype=json&pagenum=1&pagesize=5&callback=jQuery19105151766284214294_1481251654251&g_tk=1605338694&_=1481251654252";
	private ListViewTV listViewTV;
	private UserSubscribeListViewAdapter mUserSubscribeListViewAdapter;
	private List<UserVFollowlstBean> list = new ArrayList<UserVFollowlstBean>();

	public static UserSubscribeFragment newInstance(String url, MainUpView mainUpView1, EffectNoDrawBridge mRecyclerViewBridge, View mOldView) {
		UserSubscribeFragment fragment = new UserSubscribeFragment();
		fragment.setFragment(fragment);
		fragment.mOldView = mOldView;
		fragment.mRecyclerViewBridge = mRecyclerViewBridge;
		fragment.mainUpView1 = mainUpView1;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_common_listview, container, false);
		listViewTV = (ListViewTV) view.findViewById(R.id.listview);
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

		mUserSubscribeListViewAdapter = new UserSubscribeListViewAdapter(getActivity(), list);
		listViewTV.setAdapter(mUserSubscribeListViewAdapter);
		
		bindEvent();
	}


	@Override
	public void volleyJson(final String href) {
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, href, UrlUtils.getHeaders(), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				System.out.println("href=" + href);
				System.out.println("response=" + response);
				Gson gson = new Gson();

				String responseJson = response.toString().replace("QZOutputJson={", "{").replace("}};", "}}").replace("};", "}").replace("jQuery19105151766284214294_1481251654251({", "{")
						.replace("})", "}");
				UserVFollowlstJson mUserVFollowlstJson = gson.fromJson(responseJson, UserVFollowlstJson.class);
				if (mUserVFollowlstJson != null && mUserVFollowlstJson.getVpp() != null) {
					list.clear();
					list.addAll(mUserVFollowlstJson.getVpp());
					mUserSubscribeListViewAdapter.setHasfollow(Integer.parseInt(mUserVFollowlstJson.getHasfollow()));
					weakReferenceHandler.sendEmptyMessageDelayed(MESSAGE_HANDLER_COMPLETE, 200);
				}
			}
		}, UserSubscribeFragment.this);
		requestQueue.add(jsonObjectRequest);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#bindEvent()
	 */
	@Override
	public void bindEvent() {
		// TODO Auto-generated method stub
		super.bindEvent();
		listViewTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "listView item" + view.getId() + ";postion=" + (int) id + " ========onItemSelected ");
				if (view != null) {
					view.bringToFront();
					mRecyclerViewBridge.setFocusView(view, mOldView, 1.1f);
					mOldView = view;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i(TAG, "listView item" + " ========onNothingSelected ");
			}
		});

		listViewTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				// 失去焦点时，将子view还原
				Log.i(TAG, "listView item" + view.getId() + " ========onFocusChange " + b);
				if (!b) {
					for (int i = 0; i < listViewTV.getChildCount(); i++) {
						View mvView = listViewTV.getChildAt(i);
						mRecyclerViewBridge.setUnFocusView(mvView);
					}
				}

			}
		});

		listViewTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					view.bringToFront();
					mRecyclerViewBridge.setFocusView(view, mOldView, 1.1f);
					mOldView = view;
				}
				Log.i(TAG, "listView item" + (int) id + " ========onItemClick ");
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.open.tencenttv.BaseV4Fragment#onErrorResponse(com.android.volley.
	 * VolleyError)
	 */
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		super.onErrorResponse(error);
		System.out.println(error);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#handlerMessage(android.os.Message)
	 */
	@Override
	public void handlerMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handlerMessage(msg);
		switch (msg.what) {
		case MESSAGE_HANDLER:
			volleyJson(url);
			break;
		case MESSAGE_HANDLER_COMPLETE:
			mUserSubscribeListViewAdapter.notifyDataSetChanged();
			weakReferenceHandler.sendEmptyMessageDelayed(MESSAGE_DEFAULT_POSITION, 50);
			break;
		case MESSAGE_DEFAULT_POSITION:
			listViewTV.setDefaultSelect(0);
			break;
		default:
			break;
		}
	}
}
