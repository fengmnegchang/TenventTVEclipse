package com.open.tencenttv.fragment;

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
import com.open.tencenttv.adapter.RankFragmentAdapter;
import com.open.tencenttv.bean.CommonT;
import com.open.tencenttv.bean.RankBean;
import com.open.tencenttv.utils.UrlUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * *****************************************************************************
 * *****************************************************************************
 * ****************** 播放排行榜
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
public class RankAllFragment extends BaseV4Fragment {
	private String rankname;
	private List<RankBean> data = new ArrayList<RankBean>();
	private ListViewTV mListViewTV;
	private RankFragmentAdapter mAdapter;
	String title;

	public static RankAllFragment newInstance(String title, String rankname,
			MainUpView mainUpView1, View mOldView,
			EffectNoDrawBridge mRecyclerViewBridge) {
		RankAllFragment fragment = new RankAllFragment();
		fragment.rankname = rankname;
		fragment.title = title;
		fragment.mainUpView1 = mainUpView1;
		fragment.mOldView = mOldView;
		fragment.mRecyclerViewBridge = mRecyclerViewBridge;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rank, container, false);
		mListViewTV = (ListViewTV) view.findViewById(R.id.listview);
		doAsync(this, this, this);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter = new RankFragmentAdapter(getActivity(), data);
		mListViewTV.setAdapter(mAdapter);
	}

	@Override
	public CommonT call() throws Exception {
		CommonT mCommonT = new CommonT();
		List<RankBean> list = parseRankList(UrlUtils.TENCENT_RANK_ALL_URL);
		mCommonT.setPlaylist(list);
		return mCommonT;
	}

	@Override
	public void onCallback(CommonT result) {
		super.onCallback(result);
		data.clear();
		data.addAll(result.getPlaylist());
		mAdapter.notifyDataSetChanged();
		// // 延时请求其它位置的item.
		// Handler handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// mListViewTV.requestFocusFromTouch();
		// mListViewTV.setSelection(0);
		// }
		// };
		// handler.sendMessageDelayed(handler.obtainMessage(), 188);
	}

	public ArrayList<RankBean> parseRankList(String href) {
		ArrayList<RankBean> list = new ArrayList<RankBean>();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i("url", "url = " + href);

			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent)
					.timeout(10000).get();
			Element masthead = doc.select("div.container_inner").first();
			Elements divElements = masthead.select("div.wrapper_rank ");
			if (divElements != null && divElements.size() > 0) {
				for (int y = 0; y < divElements.size(); y++) {
					Element h2Elements = divElements.get(y)
							.select("h2.rank_title").first();
					if (h2Elements.text().equals(title)) {

						Elements ulElements = divElements.get(y).select(
								"ul.rank_list");
						if (ulElements != null && ulElements.size() > 0) {
							Elements liElements = divElements.get(y).select(
									"li");
							String l0String = liElements.get(0).text();
							if (l0String.equals(rankname)) {

								// 解析文件
								/**
								 * <li class="rank_name">欧美</li> <li class="rank_first">
								 * <a href="/detail/l/lvxqk7s7yynbdba.html"
								 * target="_blank" class="figure"
								 * _hot="top.movie.euam.top1" title="谍影重重5">
								 * <img onerror="picerr(this, 17)" lz_src=
								 * "http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/l/lvxqk7s7yynbdba_c.jpg"
								 * alt="谍影重重5" /> <div class="figure_sign"> <i
								 * class="num num_1_large">1</i> </div> <div
								 * class="figure_caption"> <i
								 * class="tend tend_up" title="上升1"></i> </div>
								 * </a> <div class="figure_title"> <a
								 * href="/detail/l/lvxqk7s7yynbdba.html"
								 * target="_blank" _hot="top.movie.euam.top1"
								 * title="谍影重重5">谍影重重5</a> </div> <div
								 * class="main_role">
								 * <p class="role_l">
								 * 主演：
								 * </p>
								 * <a href=
								 * "http://v.qq.com/search.html?pagetype=3&stag=word.tag&ms_key=%E9%A9%AC%E7%89%B9%C2%B7%E8%BE%BE%E8%92%99"
								 * _hot="top.movie.euam.name" target="_blank"
								 * title="马特·达蒙"><span>马特·达蒙</span></a><a href=
								 * "http://v.qq.com/search.html?pagetype=3&stag=word.tag&ms_key=%E6%9C%B1%E4%B8%BD%E5%A8%85%C2%B7%E6%96%AF%E8%92%82%E5%B0%94%E6%96%AF"
								 * _hot="top.movie.euam.name" target="_blank"
								 * title="朱丽娅·斯蒂尔斯"><span>朱丽娅·斯蒂尔斯</span></a><a
								 * href=
								 * "http://v.qq.com/search.html?pagetype=3&stag=word.tag&ms_key=%E8%89%BE%E4%B8%BD%E8%A5%BF%E4%BA%9A%C2%B7%E7%BB%B4%E5%9D%8E%E5%BE%B7"
								 * _hot="top.movie.euam.name" target="_blank"
								 * title="艾丽西亚·维坎德"><span>艾丽西亚·维坎德</span></a><a
								 * href=
								 * "http://v.qq.com/search.html?pagetype=3&stag=word.tag&ms_key=%E6%B1%A4%E7%B1%B3%C2%B7%E6%9D%8E%C2%B7%E7%90%BC%E6%96%AF"
								 * _hot="top.movie.euam.name" target="_blank"
								 * title="汤米·李·琼斯"><span>汤米·李·琼斯</span></a>
								 * </div> <div class="editor">导演：<a href=
								 * "http://v.qq.com/search.html?pagetype=3&stag=word.tag&ms_key=%E4%BF%9D%E7%BD%97%C2%B7%E6%A0%BC%E6%9E%97%E6%A0%BC%E6%8B%89%E6%96%AF"
								 * _hot="top.movie.euam.name" target="_blank"
								 * title="保罗·格林格拉斯"><span>保罗·格林格拉斯</span></a>
								 * </div> <div
								 * class="item_desc">影片故事发生在后斯诺登时代，中情局系统被黑客入侵，
								 * 多项特工计划可能泄露
								 * 。与此同时，中情局还发现了伯恩和好搭档尼基·帕森斯的踪迹。年轻的网络专家海瑟
								 * ·李自告奋勇追踪伯恩 ，而伯恩也在寻找着关于自己身世的惊天黑幕
								 * 。中情局高官杜威是知晓一切幕后秘密的人，他、海瑟·李和伯恩之间的角力让影片充满悬念
								 * ，不到最后一刻，就无法了解真正的结局。</div></li>
								 * 
								 * <li>
								 * <a href="/detail/g/gd4ls0ntz1hgf0d.html"
								 * target="_blank" class="rank_item"
								 * _hot="top.movie.euam.top2" title="爱宠大机密">
								 * <div class="item_l"> <i
								 * class="num num_2">2</i> <span
								 * class="item_name">爱宠大机密</span> </div> <div
								 * class="item_r"> <i class="tend tend_down"
								 * title="下降1"></i> </div> </a></li>
								 */
								for (int i = 1+y*10; i < (y+1)*10+1; i++) {
									RankBean bean = new RankBean();
									try {
										try {
											Element aElement = liElements
													.get(i).select("a").first();
											String hrefurl = aElement
													.attr("href");
											String title = aElement
													.attr("title");
											System.out.print("y===" + y
													+ ";i===" + i
													+ ";hrefurl ===" + hrefurl
													+ ";title===" + title);
											bean.setActorName(title);
											bean.setRankurl(hrefurl);
										} catch (Exception e) {
											e.printStackTrace();
										}

										try {
											Element iElement = liElements
													.get(i).select("i.num")
													.first();
											String count = iElement.text();
											System.out.print("y===" + y
													+ ";i===" + i
													+ ";count ===" + count);
											bean.setCount(count);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									list.add(bean);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
