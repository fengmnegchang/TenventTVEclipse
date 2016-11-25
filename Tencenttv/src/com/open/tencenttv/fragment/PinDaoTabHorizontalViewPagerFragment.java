package com.open.tencenttv.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.OpenTabHost;
import com.open.androidtvwidget.view.TextViewWithTTF;
import com.open.tencenttv.BaseV4Fragment;
import com.open.tencenttv.R;
import com.open.tencenttv.WeakReferenceHandler;
import com.open.tencenttv.adapter.OpenTabTitleAdapter;
import com.open.tencenttv.adapter.RankPagerAdapter;
import com.open.tencenttv.bean.RankBean;
import com.open.tencenttv.json.RankJson;
import com.open.tencenttv.utils.UrlUtils;

/**
 * *****************************************************************************
 * *****************************************************************************
 * ******************
 * 
 * @author :fengguangjing
 * @createTime: 16/11/18
 * @version:
 * @modifyTime:
 * @modifyAuthor:
 * @description: 
 *               ****************************************************************
 *               ***************************************************************
 *               *********************************************
 */
public class PinDaoTabHorizontalViewPagerFragment extends BaseV4Fragment<RankJson> implements OpenTabHost.OnTabSelectListener {
	public static final String TAG = PinDaoTabHorizontalViewPagerFragment.class.getSimpleName();
	ViewPager viewpager;
	// 移动边框.
	EffectNoDrawBridge mEffectNoDrawBridge;
	View mNewFocus;
	RankPagerAdapter mRankPagerAdapter;
	// RankViewPagerAdapter mRankViewPagerAdapter;
	private List<RankBean> titlerankList = new ArrayList<RankBean>();
	OpenTabHost mOpenTabHost;
	OpenTabTitleAdapter mOpenTabTitleAdapter;
	private List<String> titleList = new ArrayList<String>();
	private List<Fragment> listRankFragment = new ArrayList<Fragment>();// view数组
	String url;
	String pindaoName;
	FrameLayout layout_search;
	int position;
	public static PinDaoTabHorizontalViewPagerFragment newInstance(String url, String pindaoName, MainUpView mainUpView1, View mOldView, EffectNoDrawBridge mEffectNoDrawBridge) {
		PinDaoTabHorizontalViewPagerFragment fragment = new PinDaoTabHorizontalViewPagerFragment();
		fragment.mainUpView1 = mainUpView1;
		fragment.mOldView = mOldView;
		fragment.mEffectNoDrawBridge = mEffectNoDrawBridge;
		fragment.url = url;
		fragment.pindaoName = pindaoName;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pindao_tab_horizontal_viewpager, container, false);
		// 初始化viewpager.
		viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		mOpenTabHost = (OpenTabHost) view.findViewById(R.id.openTabHost);
		layout_search =  (FrameLayout) view.findViewById(R.id.layout_search);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		doAsync(this, this, this);
		PinDaoSearchHeadFragment rightFragment = PinDaoSearchHeadFragment.newInstance(weakReferenceHandler,url,pindaoName,mainUpView1,mOldView,mRecyclerViewBridge);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.layout_search, rightFragment).commit();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		weakReferenceHandler = new WeakReferenceHandler(this);
	}
	
	Handler mFocusHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// // 初始化.
			switchTab(mOpenTabHost, position);
			viewpager.setCurrentItem(position);

		}
	};

	/* (non-Javadoc)
	 * @see com.open.tencenttv.BaseV4Fragment#handlerMessage(android.os.Message)
	 */
	@Override
	public void handlerMessage(Message msg) {
		super.handlerMessage(msg);
		Log.i(TAG,"CurrentItem=="+viewpager.getCurrentItem()+";url=="+msg.obj+";what=="+msg.what);
		((PinDaoFragment)listRankFragment.get(viewpager.getCurrentItem())).refreshHandlerMessage(msg.obj+"");
	}

	/**
	 * demo (翻页的时候改变状态) 将标题栏的文字颜色改变. <br>
	 * 你可以写自己的东西，我这里只是DEMO.
	 */
	public void switchTab(OpenTabHost openTabHost, int postion) {
		List<View> viewList = openTabHost.getAllTitleView();
		for (int i = 0; i < viewList.size(); i++) {
			TextViewWithTTF view = (TextViewWithTTF) openTabHost.getTitleViewIndexAt(i);
			if (view != null) {
				Resources res = view.getResources();
				if (res != null) {
					if (i == postion) {
						view.setTextColor(res.getColor(android.R.color.white));
						view.setTypeface(null, Typeface.BOLD);
						view.setSelected(true); // 为了显示 失去焦点，选中为 true的图片.
					} else {
						view.setTextColor(res.getColor(R.color.white_50));
						view.setTypeface(null, Typeface.NORMAL);
						view.setSelected(false);
					}
				}
			}
		}
	}

	@Override
	public void onTabSelect(OpenTabHost openTabHost, View titleWidget, int position) {
		if (viewpager != null) {
			viewpager.setCurrentItem(position);
		}
	}

	@Override
	public RankJson call() throws Exception {
		RankJson mCommonT = new RankJson();
		ArrayList<RankBean> list = new ArrayList<RankBean>();
		try {
			list = parseTitleRank(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
		mCommonT.setList(list);
		return mCommonT;
	}

	@Override
	public void onCallback(RankJson result) {
		super.onCallback(result);
		titlerankList.clear();
		titlerankList.addAll(result.getList());
		// 初始化标题栏.
		titleList.clear();
		listRankFragment.clear();
		String url0 = this.url.split("/\\?")[0];
		String url1 = this.url.split("/\\?")[1];
		for (int i=0;i<result.getList().size();i++) {
			RankBean bean = result.getList().get(i);
			titleList.add(bean.getRankName());
			if("最近热播".equals(bean.getRankName())){
				position = i;
			}
			Log.i(TAG,"PinDaoFragment url ="+url0 +"/"+bean.getRankurl()+";pindaoname="+ bean.getRankName());
			PinDaoFragment fragment = PinDaoFragment.newInstance(url0 +"/"+bean.getRankurl(), bean.getRankName(), mainUpView1, mOldView, mRecyclerViewBridge);
			listRankFragment.add(fragment);
		}
		mOpenTabTitleAdapter = new OpenTabTitleAdapter(getActivity(), titleList);
		mOpenTabHost.setAdapter(mOpenTabTitleAdapter);

		// mRankViewPagerAdapter = new
		// RankViewPagerAdapter(getActivity(),result.getTitlerankList());
		mRankPagerAdapter = new RankPagerAdapter(getChildFragmentManager(), listRankFragment, titleList);
		viewpager.setAdapter(mRankPagerAdapter);

		// 初始化viewpager.
		// 全局焦点监听. (这里只是demo，为了方便这样写，你可以不这样写)
		viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				// 判断 : 避免焦点框跑到标题栏. (只是demo，你自己处理逻辑)
				// 你也可以让标题栏放大，有移动边框.
				if (newFocus != null && !(newFocus instanceof TextViewWithTTF)) {
					mEffectNoDrawBridge.setVisibleWidget(false);
					mNewFocus = newFocus;
					mOldView = oldFocus;
					mainUpView1.setFocusView(newFocus, oldFocus, 1.2f);
					// 让被挡住的焦点控件在前面.
					newFocus.bringToFront();
					OPENLOG.D("addOnGlobalFocusChangeListener");
				} else { // 标题栏处理.
					mNewFocus = null;
					mOldView = null;
					mainUpView1.setUnFocusView(oldFocus);
					mEffectNoDrawBridge.setVisibleWidget(true);
				}
			}
		});
		viewpager.setOffscreenPageLimit(4);
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switchTab(mOpenTabHost, position);
				OPENLOG.D("onPageSelected position:" + position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// viewPager 正在滚动中.
				OPENLOG.D("onPageScrolled position:" + position + " positionOffset:" + positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_IDLE: // viewpager 滚动结束.
					mainUpView1.setFocusView(mNewFocus, mOldView, 1.2f);
					// 监听动画事件.
					mEffectNoDrawBridge.setOnAnimatorListener(new OpenEffectBridge.NewAnimatorListener() {
						@Override
						public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation) {
						}

						@Override
						public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation) {
							// 动画结束的时候恢复原来的时间. (这里只是DEMO)
							mEffectNoDrawBridge.setTranDurAnimTime(OpenEffectBridge.DEFAULT_TRAN_DUR_ANIM);
						}
					});
					// 让被挡住的焦点控件在前面.
					if (mNewFocus != null)
						mNewFocus.bringToFront();
					OPENLOG.D("SCROLL_STATE_IDLE");
					break;
				case ViewPager.SCROLL_STATE_DRAGGING:
					OPENLOG.D("SCROLL_STATE_DRAGGING");
					break;
				case ViewPager.SCROLL_STATE_SETTLING: // viewPager开始滚动.
					mEffectNoDrawBridge.clearAnimator(); // 清除之前的动画.
					mEffectNoDrawBridge.setTranDurAnimTime(0); // 避免边框从其它地方跑出来.
					OPENLOG.D("SCROLL_STATE_SETTLING");
					break;
				}
			}
		});

		// 初始化标题栏.
		mOpenTabHost.setOnTabSelectListener(this);

		mFocusHandler.sendEmptyMessageDelayed(10, 5000);
	}

	public ArrayList<RankBean> parseTitleRank(String href) {
		ArrayList<RankBean> list = new ArrayList<RankBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i(TAG, "url = " + href);

			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent).timeout(10000).get();

			Element masthead = doc.select("ul.filter_tabs").first();
			Elements liElements = masthead.select("li");
			/**
			 * <div class="mod_row_filter cf">
			 * <ul class="filter_tabs cf">
			 * <li class="item ">
			 * <a href="?cate=10001&#38;offset=0&#38;sort=5"
			 * _boss="new">最新上架</a><span class="divid">|</span></li>
			 * <li class="item ">
			 * <a href="?cate=10001&#38;offset=0&#38;sort=5&#38;theatre=1"
			 * _boss="Cinema">院线上映</a><span class="divid">|</span></li>
			 * <li class="item current">
			 * <a href="?cate=10001&#38;offset=0&#38;sort=4"
			 * _boss="hot">最近热播</a><span class="divid">|</span></li>
			 * <li class="item ">
			 * <a href="?cate=10001&#38;offset=0&#38;sort=6"
			 * _boss="reputation">最受好评</a></li>
			 * </ul>
			 */
			// 解析文件
			if (liElements != null && liElements.size() > 1) {
				for (int i = 0; i < liElements.size(); i++) {
					RankBean bean = new RankBean();
					try {
						Element aElement = liElements.get(i).select("a").first();
						String hrefurl = aElement.attr("href");
						String title = aElement.text().replace("|", "");
						Log.i(TAG,"i===" + i + "hrefurl==" + hrefurl + ";title===" + title);
						bean.setRankName(title);
						bean.setRankurl(hrefurl);
						list.add(bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
