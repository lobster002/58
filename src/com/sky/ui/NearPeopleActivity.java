package com.sky.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.sky.R;
import com.sky.ui.util.CollectionUtils;
import com.sky.ui.view.ActionBar;
import com.sky.ui.view.ActionBar.onLeftButtonClickListener;
import com.sky.adapter.NearPeopleAdapter;
import com.sky.bean.MyUser;
import com.sky.xlistview.XListView;
import com.sky.xlistview.XListView.IXListViewListener;

/**
 * ���������б�
 * 
 * @ClassName: NewFriendActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-6 ����4:28:09
 */
public class NearPeopleActivity extends ActivityBase implements
		IXListViewListener, OnItemClickListener {

	XListView mListView;
	NearPeopleAdapter adapter;
	String from = "";
	private ActionBar bar;
	List<MyUser> nears = new ArrayList<MyUser>();

	private double QUERY_KILOMETERS = 10;// Ĭ�ϲ�ѯ10���ﷶΧ�ڵ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_people);
		initView();
	}

	private void initView() {
		Log.i("nearby", "1");
		bar = (ActionBar) findViewById(R.id.headbar);
		bar.setTitleAndLeftButton(R.drawable.back, "��������");
		bar.setOnLeftButtonClickListener(new onLeftButtonClickListener() {

			@Override
			public void onClick() {
				finish();
			}
		});
		// initTopBarForLeft("��������");
		initXListView();
	}

	private void initXListView() {
		Log.i("nearby", "2");
		mListView = (XListView) findViewById(R.id.list_near);
		mListView.setOnItemClickListener(this);
		// ���Ȳ��������ظ���
		mListView.setPullLoadEnable(false);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();

		adapter = new NearPeopleAdapter(this, nears);
		mListView.setAdapter(adapter);
		Log.i("nearby", "3");
		initNearByList(false);
	}

	int curPage = 0;
	ProgressDialog progress;

	private void initNearByList(final boolean isUpdate) {
		Log.i("nearby", "5");
		if (!isUpdate) {
			progress = new ProgressDialog(NearPeopleActivity.this);
			progress.setMessage("���ڲ�ѯ��������...");
			progress.setCanceledOnTouchOutside(true);
			progress.show();
			Log.i("nearby", "6");
		}

		if (!mApplication.getLatitude().equals("")
				&& !mApplication.getLongtitude().equals("")) {
			double latitude = Double.parseDouble(mApplication.getLatitude());
			double longtitude = Double
					.parseDouble(mApplication.getLongtitude());
			// ��װ�Ĳ�ѯ�������������ҳ��ʱ isUpdateΪfalse��������ˢ�µ�ʱ������Ϊtrue���С�
			// �˷���Ĭ��ÿҳ��ѯ10������,�����ѯ����10�������ڲ�ѯ֮ǰ����BRequest.QUERY_LIMIT_COUNT���磺BRequest.QUERY_LIMIT_COUNT=20
			// �˷����������Ĳ�ѯָ��10�����ڵ��Ա�ΪŮ�Ե��û��б���Ĭ�ϰ��������б�
			// ����㲻���ѯ�Ա�ΪŮ���û������Խ�equalProperty��Ϊnull����equalObj��Ϊnull����
			userManager.queryKiloMetersListByPage(isUpdate, 0, "location",
					longtitude, latitude, true, QUERY_KILOMETERS, null, false,
					new FindListener<MyUser>() {
						// �˷���Ĭ�ϲ�ѯ���д�����λ����Ϣ�����Ա�ΪŮ���û��б�������㲻����������б��Ļ�������ѯ�����е�isShowFriends����Ϊfalse����
						// userManager.queryNearByListByPage(isUpdate,0,"location",
						// longtitude, latitude, true,"sex",false,new
						// FindListener<User>() {

						@Override
						public void onSuccess(List<MyUser> list) {
							// TODO Auto-generated method stub
							if (CollectionUtils.isNotNull(list)) {
								if (isUpdate) {
									nears.clear();
								}
								adapter.addAll(list);
								if (list.size() < BRequest.QUERY_LIMIT_COUNT) {
									mListView.setPullLoadEnable(false);
									ShowToast("���������������!");
								} else {
									mListView.setPullLoadEnable(true);
								}
							} else {
								ShowToast("���޸�������!");
							}

							if (!isUpdate) {
								progress.dismiss();
							} else {
								refreshPull();
							}
						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							ShowToast("���޸�������!");
							mListView.setPullLoadEnable(false);
							if (!isUpdate) {
								progress.dismiss();
							} else {
								refreshPull();
							}
						}

					});
		} else {
			ShowToast("���޸�������!");
			progress.dismiss();
			refreshPull();
		}

	}

	/**
	 * ��ѯ����
	 * 
	 * @Title: queryMoreNearList
	 * @Description: TODO
	 * @param @param page
	 * @return void
	 * @throws
	 */
	private void queryMoreNearList(int page) {
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		// ��ѯ10���ﷶΧ�ڵ��Ա�ΪŮ���û��б�
		userManager.queryKiloMetersListByPage(true, page, "location",
				longtitude, latitude, true, QUERY_KILOMETERS, null, false,
				new FindListener<MyUser>() {
					// ��ѯȫ������λ����Ϣ���Ա�ΪŮ�Ե��û��б�
					// userManager.queryNearByListByPage(true,page, "location",
					// longtitude, latitude, true,"sex",false,new
					// FindListener<User>() {

					@Override
					public void onSuccess(List<MyUser> arg0) {
						// TODO Auto-generated method stub
						if (CollectionUtils.isNotNull(arg0)) {
							adapter.addAll(arg0);
						}
						refreshLoad();
					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowLog("��ѯ���฽�����˳���:" + arg1);
						mListView.setPullLoadEnable(false);
						refreshLoad();
					}

				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		MyUser user = (MyUser) adapter.getItem(position - 1);
		Intent intent = new Intent(this, SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initNearByList(true);
	}

	private void refreshLoad() {
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}

	private void refreshPull() {
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		double latitude = Double.parseDouble(mApplication.getLatitude());
		double longtitude = Double.parseDouble(mApplication.getLongtitude());
		// ���ǲ�ѯ10���ﷶΧ�ڵ��Ա�ΪŮ�û�����
		userManager.queryKiloMetersTotalCount(MyUser.class, "location",
				longtitude, latitude, true, QUERY_KILOMETERS, null, false,
				new CountListener() {
					// ���ǲ�ѯ�����������Ա�ΪŮ�Ե��û�����
					// userManager.queryNearTotalCount(User.class, "location",
					// longtitude, latitude, true,"sex",false,new
					// CountListener() {

					@Override
					public void onSuccess(int arg0) {
						// TODO Auto-generated method stub
						if (arg0 > nears.size()) {
							curPage++;
							queryMoreNearList(curPage);
						} else {
							ShowToast("���ݼ������");
							mListView.setPullLoadEnable(false);
							refreshLoad();
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowLog("��ѯ������������ʧ��" + arg1);
						refreshLoad();
					}
				});

	}

}