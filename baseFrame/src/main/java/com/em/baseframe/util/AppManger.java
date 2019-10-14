package com.em.baseframe.util;

import android.app.Activity;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

/**
 * @title  App管理类
 * @date   2017/06/17
 * @author enmaoFu
 */
public class AppManger {
	private static Stack<Activity> mActivityStack;
	private static AppManger mAppManager;

	private AppManger() {

	}

	
	
	/**
	 * 单一实例
	 */
	public static AppManger getInstance() {
		if (mAppManager == null) {
			
			synchronized(AppManger.class){
				if (mAppManager == null) {
					mAppManager = new AppManger();
				}
			}
		}
		return mAppManager;
	}


	/**
	 * 查看某个activity是否被添加过
	 * @param cls
	 */
	public boolean isAddActivity(Class<?> cls){
		Iterator<Activity> iterator = mActivityStack.iterator();
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 关闭所选所以之外的activity
	 */
	public void KillexceptActivitiy(Activity activity){
		int size = mActivityStack.size();
		for (int i = size-1; i >=0; i--) {
			if (mActivityStack.get(i)!=activity) {
				mActivityStack.get(i).finish();
				mActivityStack.remove(i);
			}
		}
	}

	/**
	 * 获取栈顶Activity（堆栈中最后一个压入的）
	 */
	public Activity getTopActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 结束栈顶Activity（堆栈中最后一个压入的）
	 */
	public void killTopActivity() {
		Activity activity = mActivityStack.lastElement();
		killActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void killActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void killActivity(Class<?> cls) {
//		for (Activity activity : mActivityStack) {
//			if (activity.getClass().equals(cls)) {
//				killActivity(activity);
//			}
//		}
		
		Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
                activity=null;
            }
        }
		
	}

	/**
	 * 结束所有Activity
	 */
	public void killAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 是否有打开app
	 * @return
	 */
	public boolean isOpenActivity(){
		if (mActivityStack == null) {
			return false;
		}else if (mActivityStack.size()==0){
			return false;
		}
		return true;
	}


	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			killAllActivity();
		/*	ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

