package com.open.tencenttv.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.open.tencenttv.BaseV4Fragment;
import com.open.tencenttv.R;
import com.open.tencenttv.VedioPreViewActivity;
import com.open.tencenttv.adapter.PinDaoFragmentAdapter;
import com.open.tencenttv.bean.CommonT;
import com.open.tencenttv.bean.PinDaoBean;
import com.open.tencenttv.utils.UrlUtils;
import com.open.tencenttv.widget.HeaderGridView;
import com.open.tencenttv.widget.scroll.ObservableScrollView;
import com.open.tencenttv.widget.scroll.ScrollViewListener;

/**
 * *****************************************************************************
 * *****************************************************************************
 * ****************** 频道内容 fragment
 * 
 * @author :fengguangjing
 * @createTime: 16/11/2
 * @version:
 * @modifyTime:
 * @modifyAuthor:
 * @description: 
 *               ****************************************************************
 *               ***************************************************************
 *               *********************************************
 */
public class PinDaoFragment extends BaseV4Fragment {
	private String pindaoName;
	TextView text_pindao_name;
	// gridview
	private List<String> data;
	private HeaderGridView gridView;
	private PinDaoFragmentAdapter mAdapter;
	private int mSavePos = -1;
	private int mCount = 50;
	// private View headerView;
	String url;
	private List<PinDaoBean> xlist = new ArrayList<PinDaoBean>();
	private ObservableScrollView scroll_more;
	private LinearLayout layout_more;
	private int offset;//初始化加载多少数据，分页

	public static PinDaoFragment newInstance(String url, String pindaoName, MainUpView mainUpView1, View mOldView, EffectNoDrawBridge mRecyclerViewBridge) {
		PinDaoFragment fragment = new PinDaoFragment();
		fragment.url = url;
		fragment.pindaoName = pindaoName;
		fragment.mainUpView1 = mainUpView1;
		fragment.mOldView = mOldView;
		fragment.mRecyclerViewBridge = mRecyclerViewBridge;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pindao, container, false);
		gridView = (HeaderGridView) view.findViewById(R.id.gridView);
		scroll_more = (ObservableScrollView) view.findViewById(R.id.scroll_more);
		layout_more = (LinearLayout) view.findViewById(R.id.layout_more);
		// headerView =
		// inflater.inflate(R.layout.adapter_pindao_fragment_headview,null);
		// text_pindao_name = (TextView)
		// headerView.findViewById(R.id.text_pindao_name);
		// text_pindao_name.setText(pindaoName);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// gridView.addHeaderView(headerView);
		// 加载数据.
		// getData(20);
		doAsync(this, this, this);
		mAdapter = new PinDaoFragmentAdapter(getActivity(), xlist);
		gridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/**
				 * 这里注意要加判断是否为NULL. 因为在重新加载数据以后会出问题.
				 */
				if (view != null) {
					mainUpView1.setFocusView(view, mOldView, 1.2f);
				}
				mOldView = view;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSavePos = position; // 保存原来的位置(不要按照我的抄，只是DEMO)
				// initGridViewData(new Random().nextInt(3));
				// mFindhandler.sendMessageDelayed(mFindhandler.obtainMessage(),
				// 111);
				Toast.makeText(getActivity(), "GridView Item " + position + " pos:" + mSavePos, Toast.LENGTH_LONG).show();

				// 进入频道
				Intent intent = new Intent();
				intent.setClass(getActivity(), VedioPreViewActivity.class);
				startActivity(intent);
			}
		});
//		scroll_more.setOnTouchListener(new OnTouchListener() {
//			private int lastY = 0;
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					lastY = scroll_more.getScrollY();
//					if (lastY == (layout_more.getHeight() - scroll_more.getHeight())) {
//						offset = offset + 20;
//						doAsync(PinDaoFragment.this, PinDaoFragment.this, PinDaoFragment.this);
//					}
//				}
//				return false;
//			}
//		});
		scroll_more.setScrollViewListener(new ScrollViewListener() {
			private int lastY = 0;
			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
				lastY = scroll_more.getScrollY();
				if (lastY == (layout_more.getHeight() - scroll_more.getHeight())) {
					offset = offset + 20;
					doAsync(PinDaoFragment.this, PinDaoFragment.this, PinDaoFragment.this);
				}
				System.out.println("x==="+x+";y=="+y+";oldx=="+oldx+";oldy=="+oldy+";layout_more.getHeight()=="+layout_more.getHeight()+";scroll_more.getHeight()=="+scroll_more.getHeight()+";scroll_more.getScrollY()=="+scroll_more.getScrollY());
			}
		});

	}

	// // 延时请求初始位置的item.
	Handler mFirstHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			gridView.setDefualtSelect(0);
		}
	};

	// // 更新数据后还原焦点框.
	// Handler mFindhandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (mSavePos != -1) {
	// gridView.requestFocusFromTouch();
	// gridView.setSelection(mSavePos);
	// }
	// }
	// };

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#call()
	 */
	@Override
	public CommonT call() throws Exception {
		// TODO Auto-generated method stub
		CommonT mCommonT = new CommonT();
		List<PinDaoBean> list = parseXlist(UrlUtils.TENCENT_X_MOVIE_LIST);
		mCommonT.setXlist(list);
		return mCommonT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.open.tencenttv.BaseV4Fragment#onCallback(com.open.tencenttv.bean.
	 * CommonT)
	 */
	@Override
	public void onCallback(CommonT result) {
		// TODO Auto-generated method stub
		super.onCallback(result);
		if(offset==0){
			xlist.clear();
			xlist.addAll(result.getXlist());
			mAdapter.notifyDataSetChanged();
			mFirstHandler.sendMessageDelayed(mFirstHandler.obtainMessage(), 188);
		}else{
			xlist.addAll(result.getXlist());
			mAdapter.notifyDataSetChanged();
		}
	}

	public ArrayList<PinDaoBean> parseXlist(String href) {
		ArrayList<PinDaoBean> list = new ArrayList<PinDaoBean>();
		try {
			//"http://v.qq.com/x/movielist/?cate=10001&offset=0&sort=4";
			href = href +"&offset="+offset;
			href = makeURL(href, new HashMap<String, Object>() {
				{ 
				}
			});
			Log.i("url", "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent).timeout(10000).get();
			Element masthead = doc.select("ul.figures_list").first();
			Elements liElements = masthead.select("li.list_item");

			/**
			 * <li class="list_item" r-on="{mouseenter: mouseIn; mouseleave: mouseOut}" data-trigger-class="list_item_hover">
			 * <a _boss="film"
			 * href="http://v.qq.com/cover/r/ratsiq6o6ha3lod.html"
			 * target="_blank" class="figure" tabindex="-1" > <img
			 * src="http://i.gtimg.cn/qqlive/images/20150608/pic_v.png"
			 * r-lazyload=
			 * "http://puui.qpic.cn/vcover_vt_pic/0/ratsiq6o6ha3lodt1467943241.jpg/220"
			 * r-imgerror="" alt="致青春·原来你还在这里" > <span class="figure_mask">
			 * <em class="mask_txt">吴亦凡刘亦菲高颜值青春</em></span> <span r-html=
			 * "{txv.common.getCoverImgtagHtml({&#34;tag_1&#34;:{&#34;id&#34;:&#34;-1&#34;,&#34;param&#34;:&#34;&#34;,&#34;param2&#34;:&#34;&#34;,&#34;text&#34;:&#34;&#34;},&#34;tag_2&#34;:{&#34;id&#34;:&#34;-1&#34;,&#34;param&#34;:&#34;&#34;,&#34;param2&#34;:&#34;&#34;,&#34;text&#34;:&#34;&#34;},&#34;tag_3&#34;:{&#34;id&#34;:&#34;-1&#34;,&#34;param&#34;:&#34;&#34;,&#34;param2&#34;:&#34;&#34;,&#34;text&#34;:&#34;&#34;},&#34;tag_4&#34;:{&#34;id&#34;:&#34;-1&#34;,&#34;param&#34;:&#34;&#34;,&#34;param2&#34;:&#34;&#34;,&#34;text&#34;:&#34;&#34;}}, [1, 2])}"
			 * ></span> </a> <strong class="figure_title"> <a _boss="film"
			 * href="http://v.qq.com/cover/r/ratsiq6o6ha3lod.html"
			 * target="_blank" title="致青春·原来你还在这里" >致青春·原来你还在这里</a> </strong>
			 * <div class="figure_desc">主演：
			 * 
			 * <a href="http://v.qq.com/x/star/131764" target="_blank"
			 * title="吴亦凡">吴亦凡</a>
			 * 
			 * <a href="http://v.qq.com/x/star/72172" target="_blank"
			 * title="刘亦菲">刘亦菲</a>
			 * 
			 * </div>
			 * 
			 * <div class="figure_info"> <span class="figure_info_brand"> <i
			 * class="ico_play_12"></i> <span class="info_inner">9593万</span>
			 * </span> <span class="mod_score"><em class="score_l">6</em>
			 * <em class="score_s">.5</em></span> </div>
			 * 
			 * <div class="figure_option" data-followid="ratsiq6o6ha3lod"
			 * data-followtype="1"> <i class="icon_collect"></i>
			 * <em class="collect_inner"></em>
			 * 
			 * </div></li>
			 * 
			 * 
			 */
			// 解析文件
			for (int i = 0; i < liElements.size(); i++) {
				PinDaoBean bean = new PinDaoBean();
				try {

					try {
						Element aElement = liElements.get(i).select("a.figure").first();
						String hrefurl = aElement.attr("href");
						System.out.print("i===" + i + ";hrefurl ===" + hrefurl);
						bean.setHrefurl(hrefurl);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element aElement = liElements.get(i).select("a.figure").first();
						Element imgElement = aElement.select("img").first();
						String imagesrc = imgElement.attr("r-lazyload");
						bean.setImagesrc(imagesrc);
						String alt = imgElement.attr("alt");
						bean.setAlt(alt);
						System.out.print("i===" + i + ";imagesrc ===" + imagesrc + ";alt==" + alt);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element spanElement = liElements.get(i).select("span.figure_mask").first();
						String figure_mask = spanElement.text();
						bean.setMask_txt(figure_mask);
						System.out.print("i===" + i + ";figure_mask ===" + figure_mask);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_descElement = liElements.get(i).select("div.figure_desc").first();
						String figure_desc = figure_descElement.text();
						bean.setFigure_desc(figure_desc);
						System.out.print("i===" + i + ";figure_desc ===" + figure_desc);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_infoElement = liElements.get(i).select("div.figure_info").first();
						Element info_innerElement = figure_infoElement.select("span.info_inner").first();
						String info_inner = info_innerElement.text();
						bean.setFigure_info(info_inner);
						System.out.print("i===" + i + ";info_inner ===" + info_inner);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_infoElement = liElements.get(i).select("div.figure_info").first();
						Element mod_scoreElement = figure_infoElement.select("span.mod_score").first();
						String mode_score = mod_scoreElement.text();
						bean.setMod_score(mode_score);
						System.out.print("i===" + i + ";mode_score ===" + mode_score);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// public List<String> getData(int count) {
	// data = new ArrayList<String>();
	// for (int i = 0; i < count; i++) {
	// String text = "text" + "电影" + i;
	// data.add(text);
	// }
	// return data;
	// }

	public void setPindaoName(String pindaoName) {
		text_pindao_name.setText(pindaoName);
	}

}
