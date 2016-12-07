/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-7下午1:42:33
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.open.tencenttv;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.open.tencenttv.fragment.PinDaoTabHorizontalViewPagerFragment;
import com.open.tencenttv.fragment.SearchTabHorizontalViewPagerFragment;
import com.open.tencenttv.utils.UrlUtils;

/**
 ***************************************************************************************************************************************************************************** 
 * 腾讯视频 搜索
 * 
 * @author :fengguangjing
 * @createTime:2016-12-7下午1:42:33
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class TencentTVSearchActivity extends CommonFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tencent_tv_search);
		init();

	}

	@Override
	protected void findView() {
		super.findView();
		this.mInflater = LayoutInflater.from(getApplicationContext());
		mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
	}

	@Override
	protected void initValue() {
		super.initValue();

		// 默认是 OpenEff...，建议使用 NoDraw... ...
		mainUpView1.setEffectBridge(new EffectNoDrawBridge());
		mRecyclerViewBridge = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
		mRecyclerViewBridge.setTranDurAnimTime(200);
		mainUpView1.setUpRectResource(R.drawable.white_light_10); // 设置移动边框的图片.
		mainUpView1.setDrawUpRectPadding(new Rect(25, 25, 23, 23)); // 边框图片设置间距

		SearchTabHorizontalViewPagerFragment fragment = SearchTabHorizontalViewPagerFragment.newInstance(UrlUtils.TENCENT_SEARCH, "电影", mainUpView1, mOldView, mRecyclerViewBridge);

		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.frame_search, fragment).commit();
	}
}
