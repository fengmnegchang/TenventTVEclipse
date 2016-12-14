/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-12-14上午10:02:25
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
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.open.tencenttv.BaseV4Fragment;
import com.open.tencenttv.R;
import com.open.tencenttv.TencentTVWebViewActivity;
import com.open.tencenttv.bean.FilmShow;
import com.open.tencenttv.bean.UserVipPayInfoBean;
import com.open.tencenttv.bean.VipAct;
import com.open.tencenttv.bean.VipPrivilege;
import com.open.tencenttv.json.UserVipJson;
import com.open.tencenttv.json.UserVipPayInfoJson;
import com.open.tencenttv.utils.UrlUtils;

/**
 ***************************************************************************************************************************************************************************** 
 * 我的VIP信息
 * 
 * @author :fengguangjing
 * @createTime:2016-12-14上午10:02:25
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class UserMyVipFragment extends BaseV4Fragment<UserVipJson> {
	private String url = "http://pay.video.qq.com/fcgi-bin/price-month?otype=json&hlw_params=pf%3D1%26app%3Dbrowser&callback=jQuery191033791200020116796_1481188958294&low_login=1&_=1481188958306";
	private LinearLayout layout_vip_join;// 成为VIP会员
	private LayoutInflater inflater;
	private LinearLayout layout_vip_privilege;// VIP特权
	private LinearLayout layout_film_show;// 会员专享大片
	private LinearLayout layout_vip_act;// 会员活动

	public static UserMyVipFragment newInstance(String url, MainUpView mainUpView1, EffectNoDrawBridge mRecyclerViewBridge, View mOldView) {
		UserMyVipFragment fragment = new UserMyVipFragment();
		fragment.mOldView = mOldView;
		fragment.mRecyclerViewBridge = mRecyclerViewBridge;
		fragment.mainUpView1 = mainUpView1;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_my_vip, container, false);
		layout_vip_join = (LinearLayout) view.findViewById(R.id.layout_vip_join);
		layout_vip_privilege = (LinearLayout) view.findViewById(R.id.layout_vip_privilege);
		layout_film_show = (LinearLayout) view.findViewById(R.id.layout_film_show);
		layout_vip_act = (LinearLayout) view.findViewById(R.id.layout_vip_act);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		inflater = LayoutInflater.from(getActivity());
		volleyJson(url);
		doAsync(this, this, this);
	}

	@Override
	public void volleyJson(final String href) {
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", UrlUtils.getCookie());

		StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, href, headers, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				System.out.println("href=" + href);
				System.out.println("response=" + response);
				Gson gson = new Gson();

				String responseJson = response.toString().replace("QZOutputJson={", "{").replace("}};", "}}").replace("};", "}").replace("jQuery191033791200020116796_1481188958294({", "{")
						.replace("})", "}");
				UserVipJson mUserVipJson = gson.fromJson(responseJson, UserVipJson.class);
				if (mUserVipJson != null && mUserVipJson.getMonth_price() != null) {
					if (mUserVipJson.getMonth_price().getValue() != null) {
						String value = mUserVipJson.getMonth_price().getValue();
						UserVipPayInfoJson mUserVipPayInfoJson = gson.fromJson(value, UserVipPayInfoJson.class);
						if (mUserVipPayInfoJson != null && mUserVipPayInfoJson.getPay_info() != null && mUserVipPayInfoJson.getPay_info().size() > 0) {
							for (int i = 0; i < mUserVipPayInfoJson.getPay_info().size(); i++) {
								UserVipPayInfoBean paybean = mUserVipPayInfoJson.getPay_info().get(i);
								View view = inflater.inflate(R.layout.layout_user_vip_my_join, null);
								TextView text_product_desc = (TextView) view.findViewById(R.id.text_product_desc);
								TextView text_month_price = (TextView) view.findViewById(R.id.text_month_price);
								TextView text_join_vip = (TextView) view.findViewById(R.id.text_join_vip);
								text_product_desc.setText(paybean.getMonth() + "个月" + paybean.getTotal_price() + "元");
								text_month_price.setText("（仅需" + paybean.getMonth_price() + "元/月）");
								text_join_vip.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								});
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								params.leftMargin = 10;
								params.rightMargin = 10;
								params.topMargin = 10;
								params.bottomMargin = 10;
								layout_vip_join.addView(view, params);
							}
						}
					}
				}
			}
		}, UserMyVipFragment.this);
		requestQueue.add(jsonObjectRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#call()
	 */
	@Override
	public UserVipJson call() throws Exception {
		// TODO Auto-generated method stub
		UserVipJson mUserVipJson = parseVip(UrlUtils.TENCENT_U_VIP);
		return mUserVipJson;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.open.tencenttv.BaseV4Fragment#onCallback(java.lang.Object)
	 */
	@Override
	public void onCallback(UserVipJson result) {
		// TODO Auto-generated method stub
		super.onCallback(result);
		if (result.getVip_privilege_list() != null && result.getVip_privilege_list().size() > 0) {
			for (int i = 0; i < result.getVip_privilege_list().size(); i++) {
				final VipPrivilege vippribean = result.getVip_privilege_list().get(i);
				View view = inflater.inflate(R.layout.layout_user_vip_privilege, null);
				ImageView image_vipsrc = (ImageView) view.findViewById(R.id.image_vipsrc);
				TextView text_vipname = (TextView) view.findViewById(R.id.text_vipname);
				text_vipname.setText(vippribean.getVipname());
				image_vipsrc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TencentTVWebViewActivity.startTencentTVWebViewActivity(getActivity(), vippribean.getViphref());
					}
				});
				if (vippribean.getVipimgurl() != null && vippribean.getVipimgurl().length() > 0) {
					DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grid_view_item_test).showImageForEmptyUri(R.drawable.grid_view_item_test)
							.showImageOnFail(R.drawable.grid_view_item_test).cacheInMemory().cacheOnDisc().build();
					ImageLoader.getInstance().displayImage(vippribean.getVipimgurl(), image_vipsrc, options, getImageLoadingListener());
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin = 10;
				params.rightMargin = 10;
				params.topMargin = 10;
				params.bottomMargin = 10;
				layout_vip_privilege.addView(view, params);
			}
		}

		if (result.getFilm_show_list() != null && result.getFilm_show_list().size() > 0) {
			for (int i = 0; i < result.getFilm_show_list().size(); i++) {
				final FilmShow filmbean = result.getFilm_show_list().get(i);
				View view = inflater.inflate(R.layout.layout_user_vip_film_show, null);
				ImageView image_src = (ImageView) view.findViewById(R.id.image_src);
				TextView text_mark_sd = (TextView) view.findViewById(R.id.text_mark_sd);
				TextView text_mark_custom = (TextView) view.findViewById(R.id.text_mark_custom);
				TextView text_figure_tit = (TextView) view.findViewById(R.id.text_figure_tit);
				TextView text_info_txt = (TextView) view.findViewById(R.id.text_info_txt);
				text_mark_sd.setText(filmbean.getMark_sd());
				text_mark_custom.setText(filmbean.getMark_custom());
				text_figure_tit.setText(filmbean.getFigure_tit() + "   " + filmbean.getFigure_score());
				text_info_txt.setText(filmbean.getInfo_txt());

				image_src.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TencentTVWebViewActivity.startTencentTVWebViewActivity(getActivity(), "https://v.qq.com/x"+filmbean.getFilmhref());
					}
				});
				if (filmbean.getImgsrc() != null && filmbean.getImgsrc().length() > 0) {
					DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grid_view_item_test).showImageForEmptyUri(R.drawable.grid_view_item_test)
							.showImageOnFail(R.drawable.grid_view_item_test).cacheInMemory().cacheOnDisc().build();
					ImageLoader.getInstance().displayImage(filmbean.getImgsrc(), image_src, options, getImageLoadingListener());
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin = 10;
				params.rightMargin = 10;
				params.topMargin = 10;
				params.bottomMargin = 10;
				layout_film_show.addView(view, params);
			}
		}

		if (result.getVip_act_list() != null && result.getVip_act_list().size() > 0) {
			for (int i = 0; i < result.getVip_act_list().size(); i++) {
				final VipAct actbean = result.getVip_act_list().get(i);
				View view = inflater.inflate(R.layout.layout_user_vip_act, null);
				ImageView image_src = (ImageView) view.findViewById(R.id.image_src);
				TextView text_vipact_tit = (TextView) view.findViewById(R.id.text_vipact_tit);
				TextView text_vipact_txt = (TextView) view.findViewById(R.id.text_vipact_txt);
				text_vipact_tit.setText(actbean.getVipact_tit());
				text_vipact_txt.setText(actbean.getVipact_txt());
				image_src.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TencentTVWebViewActivity.startTencentTVWebViewActivity(getActivity(), actbean.getVipacthref());
					}
				});
				if (actbean.getImgsrc() != null && actbean.getImgsrc().length() > 0) {
					DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.grid_view_item_test).showImageForEmptyUri(R.drawable.grid_view_item_test)
							.showImageOnFail(R.drawable.grid_view_item_test).cacheInMemory().cacheOnDisc().build();
					ImageLoader.getInstance().displayImage(actbean.getImgsrc(), image_src, options, getImageLoadingListener());
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin = 10;
				params.rightMargin = 10;
				params.topMargin = 10;
				params.bottomMargin = 10;
				layout_vip_act.addView(view, params);
			}
		}
	}

	private UserVipJson parseVip(String href) {
		UserVipJson mUserVipJson = new UserVipJson();
		try {
			href = makeURL(href, new HashMap<String, Object>() {
				{
				}
			});
			Log.i(TAG, "url = " + href);
			Document doc = Jsoup.connect(href).userAgent(UrlUtils.userAgent).timeout(10000).get();

			Element vip_privilegeElement = doc.select("div.vip_privilege").first();
			Elements liElements = vip_privilegeElement.select("li");

			/**
			 * <li class="item">
			 * <a class="item_link" target="_blank"
			 * href="http://film.qq.com/vip/vip_privilege_detail.html?tab=0">
			 * <span class="icon_privilege icon_privilege_new"></span>
			 * <p class="name">
			 * 院线新片全覆盖
			 * </p>
			 * </a></li>
			 */
			// 解析文件
			if (liElements != null && liElements.size() > 0) {
				List<VipPrivilege> vip_privilege_list = new ArrayList<VipPrivilege>();
				VipPrivilege vipPriBean;
				for (int i = 0; i < liElements.size(); i++) {
					vipPriBean = new VipPrivilege();
					Element aElement = liElements.get(i).select("a").first();
					String viphref = aElement.attr("href");
					String vipname = aElement.select("p").first().text();
					vipPriBean.setViphref(viphref);
					vipPriBean.setVipname(vipname);
					vipPriBean.setVipimgurl("http://imgcache.gtimg.cn/tencentvideo_v1/vstyle/web/v3/style/user_center_new/images/vip/sprite_icon_privilege.png?d=0613&max_age=31104000");
					vip_privilege_list.add(vipPriBean);
					Log.i(TAG, "viphref==" + viphref + ";vipname==" + vipname);
				}
				mUserVipJson.setVip_privilege_list(vip_privilege_list);
			}

			/**
			 * <li class="list_item">
			 * <a target="_blank" href="/cover/a/avuik2dix9zqv8p.html"
			 * class="figure_img"> <img src=
			 * "http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/a/avuik2dix9zqv8p_y.jpg"
			 * alt=" "> <i class="mark_play"></i> <span
			 * class="mark_sd">蓝光</span><span class="mark_custom">腾讯出品</span>
			 * 
			 * </a> <div class="figure_info">
			 * <h3 class="info_tit">
			 * <a href="/cover/a/avuik2dix9zqv8p.html" target="_blank"
			 * class="figure_tit">如果蜗牛有爱情</a> <span
			 * class="figure_score">9.2</span>
			 * 
			 * </h3>
			 * <p class="info_txt">
			 * 王凯王子文玩命缉凶
			 * </p>
			 * </div></li>
			 */
			Element film_showElement = doc.select("div.film_show").first();
			Elements filmliElements = film_showElement.select("li");
			if (filmliElements != null && filmliElements.size() > 0) {
				List<FilmShow> film_show_list = new ArrayList<FilmShow>();
				FilmShow filmBean;
				for (int i = 0; i < filmliElements.size(); i++) {
					filmBean = new FilmShow();
					try {
						Element aElement = filmliElements.get(i).select("a.figure_img").first();
						String filmhref = aElement.attr("href");
						filmBean.setFilmhref(filmhref);
						Log.i(TAG, "filmhref==" + filmhref);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Element aElement = filmliElements.get(i).select("a.figure_img").first();
						Element imgElement = aElement.select("img").first();
						String imgsrc = imgElement.attr("src");
						filmBean.setImgsrc(imgsrc);
						Log.i(TAG, "imgsrc==" + imgsrc);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element aElement = filmliElements.get(i).select("a.figure_img").first();
						Element mark_sdElement = aElement.select("span.mark_sd").first();
						String mark_sd = mark_sdElement.text();
						filmBean.setMark_sd(mark_sd);
						Log.i(TAG, "mark_sd==" + mark_sd);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element aElement = filmliElements.get(i).select("a.figure_img").first();
						Element mark_customElement = aElement.select("span.mark_custom").first();
						String mark_custom = mark_customElement.text();
						filmBean.setMark_custom(mark_custom);
						Log.i(TAG, "mark_custom==" + mark_custom);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_infoElement = filmliElements.get(i).select("div.figure_info").first();
						Element aElement = figure_infoElement.select("a").first();
						String figure_tit = aElement.text();
						filmBean.setFigure_tit(figure_tit);
						Log.i(TAG, "figure_tit==" + figure_tit);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_infoElement = filmliElements.get(i).select("div.figure_info").first();
						Element spanElement = figure_infoElement.select("span.figure_score").first();
						String figure_score = spanElement.text();
						filmBean.setFigure_score(figure_score);
						Log.i(TAG, "figure_score==" + figure_score);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element figure_infoElement = filmliElements.get(i).select("div.figure_info").first();
						Element pElement = figure_infoElement.select("p.info_txt").first();
						String info_txt = pElement.text();
						filmBean.setInfo_txt(info_txt);
						Log.i(TAG, "info_txt==" + info_txt);
					} catch (Exception e) {
						e.printStackTrace();
					}
					film_show_list.add(filmBean);
				}
				mUserVipJson.setFilm_show_list(film_show_list);
			}

			/**
			 * <li class="list_item">
			 * <a href=
			 * "http://v.qq.com/movie/p/topic/lxby/index.html?ptag=user.ad"
			 * target="_blank" class="item_img"> <img src=
			 * "http://i.gtimg.cn/qqlive/images/20161206/i1481192440_1.jpg"
			 * class="cover" alt=""> <div class="info_txt">
			 * <h3 class="tit">月月秒大奖，周周抽好礼！</h3>
			 * <p class="txt">
			 * 开通连续包月会员即有机会获得洗颜专科洗面奶，还有iPad、音箱大奖等你来！
			 * </p>
			 * </div>
			 * 
			 * </a></li>
			 */
			Element mod_vip_actElement = doc.select("ul.mod_vip_act").first();
			Elements vip_actliElements = mod_vip_actElement.select("li");
			if (vip_actliElements != null && vip_actliElements.size() > 0) {
				List<VipAct> vip_act_list = new ArrayList<VipAct>();
				VipAct vipActBean;
				for (int i = 0; i < vip_actliElements.size(); i++) {
					vipActBean = new VipAct();
					try {
						Element aElement = vip_actliElements.get(i).select("a").first();
						String vipacthref = aElement.attr("href");
						Log.i(TAG, "vipacthref==" + vipacthref);
						vipActBean.setVipacthref(vipacthref);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element imgElement = vip_actliElements.get(i).select("img").first();
						String imgsrc = imgElement.attr("src");
						vipActBean.setImgsrc(imgsrc);
						Log.i(TAG, "imgsrc==" + imgsrc);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element h3Element = vip_actliElements.get(i).select("h3.tit").first();
						String vipact_tit = h3Element.text();
						vipActBean.setVipact_tit(vipact_tit);
						Log.i(TAG, "vipact_tit==" + vipact_tit);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Element pElement = vip_actliElements.get(i).select("p.txt").first();
						String vipact_txt = pElement.text();
						vipActBean.setVipact_txt(vipact_txt);
						Log.i(TAG, "vipact_txt==" + vipact_txt);
					} catch (Exception e) {
						e.printStackTrace();
					}

					vip_act_list.add(vipActBean);
				}
				mUserVipJson.setVip_act_list(vip_act_list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mUserVipJson;
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
}
