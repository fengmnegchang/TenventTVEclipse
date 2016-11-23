package com.open.tencenttv.bean;

import com.jayfang.dropdownmenu.DropItemBean;
import com.open.androidtvwidget.leanback.mode.ListRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * T公共类封装
 * 
 * @author think
 * 
 */
public class CommonT implements Serializable {
	private ArrayList<NavPopPinDaoBean> navpoplist = new ArrayList<NavPopPinDaoBean>();// 头部导航菜单
	private ArrayList<NewFeatureBean> newfeaturelist = new ArrayList<NewFeatureBean>();// 推荐组别
	private ArrayList<ListRow> mediumlist = new ArrayList<ListRow>();// 频道分类数据
	private ArrayList<SliderNavBean> sliderNavlist = new ArrayList<SliderNavBean>();// 导航大图
	private List<RankBean> ranklist = new ArrayList<RankBean>();// 播放排行榜左边标题
	private List<RankBean> playlist = new ArrayList<RankBean>();// 播放排行榜
	private List<RankBean> titlerankList = new ArrayList<RankBean>();// 播放排行榜第二行标题
	private List<PinDaoBean> sidenavilist = new ArrayList<PinDaoBean>();// x影视检索，右部头v.qq.com/x/movielist/?cate=10001&offset=0&sort=4
	private List<PinDaoBean> xlist = new ArrayList<PinDaoBean>();// x 视频列表
	private List<DropItemBean> mMenuItems = new ArrayList<DropItemBean>();// 影视列表搜索头部

	public ArrayList<NavPopPinDaoBean> getNavpoplist() {
		return navpoplist;
	}

	public void setNavpoplist(ArrayList<NavPopPinDaoBean> navpoplist) {
		this.navpoplist = navpoplist;
	}

	public ArrayList<NewFeatureBean> getNewfeaturelist() {
		return newfeaturelist;
	}

	public void setNewfeaturelist(ArrayList<NewFeatureBean> newfeaturelist) {
		this.newfeaturelist = newfeaturelist;
	}

	public ArrayList<ListRow> getMediumlist() {
		return mediumlist;
	}

	public void setMediumlist(ArrayList<ListRow> mediumlist) {
		this.mediumlist = mediumlist;
	}

	public ArrayList<SliderNavBean> getSliderNavlist() {
		return sliderNavlist;
	}

	public void setSliderNavlist(ArrayList<SliderNavBean> sliderNavlist) {
		this.sliderNavlist = sliderNavlist;
	}

	public List<RankBean> getRanklist() {
		return ranklist;
	}

	public void setRanklist(List<RankBean> ranklist) {
		this.ranklist = ranklist;
	}

	public List<RankBean> getPlaylist() {
		return playlist;
	}

	public void setPlaylist(List<RankBean> playlist) {
		this.playlist = playlist;
	}

	public List<RankBean> getTitlerankList() {
		return titlerankList;
	}

	public void setTitlerankList(List<RankBean> titlerankList) {
		this.titlerankList = titlerankList;
	}

	public List<PinDaoBean> getSidenavilist() {
		return sidenavilist;
	}

	public void setSidenavilist(List<PinDaoBean> sidenavilist) {
		this.sidenavilist = sidenavilist;
	}

	public List<PinDaoBean> getXlist() {
		return xlist;
	}

	public void setXlist(List<PinDaoBean> xlist) {
		this.xlist = xlist;
	}

	public List<DropItemBean> getmMenuItems() {
		return mMenuItems;
	}

	public void setmMenuItems(List<DropItemBean> mMenuItems) {
		this.mMenuItems = mMenuItems;
	}

}
