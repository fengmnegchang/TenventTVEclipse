package com.open.tencenttv.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * *****************************************************************************
 * *****************************************************************************
 * ******************
 * 
 * @author :fengguangjing
 * @createTime: 16/11/16
 * @version:
 * @modifyTime:
 * @modifyAuthor:
 * @description: 
 *               ****************************************************************
 *               ***************************************************************
 *               *********************************************
 */
public class UrlUtils {
	/** 腾讯视频主页 */
	public static final String TENCENT_URL = "http://v.qq.com/";
	/** 图片地址前缀 */
	public static final String TENCENT_IMAGE_URL = "http:";
	/** 频道电视剧主页 */
	public static final String TENCENT_TV_URL = "http://v.qq.com/tv/";
	/** 播放排名 */
	public static final String TENCENT_RANK_URL = "http://v.qq.com/rank/detail/1_-1_-1_-1_2_-1.html";
	/** 播放全部排名 */
	public static final String TENCENT_RANK_ALL1_URL = "http://v.qq.com/rank/index/-1_-1_-1.html";
	/** 播放全部排名重定向 */
	public static final String TENCENT_RANK_ALL_URL = "http://v.qq.com/rank/";
	/** 视频检索 */
	public static final String TENCENT_X_MOVIE_LIST = "http://v.qq.com/x/movielist/?cate=10001&offset=0&sort=4";
	/** 按明星分类 */
	public static final String TENCENT_STAR = "http://v.qq.com/x/star/77904";
	/** 浏览器代理 **/
	public static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";

	public static final String tencentAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36 QQBrowser/4.1.4132.400";

	/** 搜索 */
	public static final String TENCENT_SEARCH = "http://v.qq.com/x/search/?q=%E5%A6%82%E6%9E%9C%E8%9C%97%E7%89%9B%E6%9C%89%E7%88%B1%E6%83%85&stag=101&smartbox_ab=";

	/**
	 * JsonObjectRequest 请求设置Headers
	 */
	public static Map<String,String> getHeaders(){
	 Map<String, String> headers = new HashMap<String, String>();
	 headers.put("Cookie",
	 "J3g_guest_id=-9045538589999304704; cuid=5032023480; sd_userid=27201462782213238; sd_cookie_crttime=1462782213238; eas_sid=y1i4W655K8T8X9U3N3p7C7U2x7; pac_uid=1_624926379; qq_slist_autoplay=on; tvfe_boss_uuid=e776aacde64effb9; h_uid=H01560819fdc; ptcz=c307e47376dee800ee4a82794866f608297b218323a8b12fd611bbd8f75f86b6; pt2gguin=o0624926379; mobileUV=1_158907f70d3_bbd13; guid=116z231z244z1420123456789qwertyu; pgv_info=ssid=s3058190196; pgv_pvid=6914624368; o_cookie=624926379");
	 headers.put("Accept","*/*");
	 headers.put("Accept-Encoding","gzip, deflate, sdch");
	 headers.put("Accept-Language","zh-CN,zh;q=0.8");
	 headers.put("Connection","keep-alive");
	 headers.put("Host","data.video.qq.com");
	 headers.put("Referer","http://v.qq.com/x/search/?q=%E9%94%A6%E7%BB%A3%E6%9C%AA%E5%A4%AE&stag=101&smartbox_ab=");
//	 headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
	 headers.put("User-Agent",tencentAgent);
	 return headers;
	 }
}
