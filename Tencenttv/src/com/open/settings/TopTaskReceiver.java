package com.open.settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.oepn.settingd.library.processutil.models.AndroidAppProcess;
import com.open.settingd.library.processutil.ProcessManager;
import com.open.settings.library.BackgroundUtil;

public class TopTaskReceiver extends BroadcastReceiver {
	//adb shell am broadcast -a com.example.settingsprj.TopTaskReceiver.FLAG
	private String TOP_TASK = "com.open.settings.TopTaskReceiver.FLAG";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action!=null && action.equals(TOP_TASK)){
			getTopComponentName(context);
			if(Build.VERSION.SDK_INT>=20){
				getLinuxCoreInfo(context,context.getPackageName());
			}
		}
	}
	
    /**
     * 方法2：通过getRunningAppProcesses的IMPORTANCE_FOREGROUND属性判断是否位于前台，当service需要常驻后台时候，此方法失效,
     * 在小米 Note上此方法无效，在Nexus上正常
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     * @return
     */
    public static boolean getRunningAppProcesses(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
        	Log.e("TopTaskReceiver", appProcess.processName+";"+appProcess.pid);
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 方法6：无意中看到乌云上有人提的一个漏洞，Linux系统内核会把process进程信息保存在/proc目录下，使用Shell命令去获取的他，再根据进程的属性判断是否为前台
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean getLinuxCoreInfo(Context context, String packageName) {

        List<AndroidAppProcess> processes = ProcessManager.getRunningForegroundApps(context);
        for (AndroidAppProcess appProcess : processes) {
        	Log.e("TopTaskReceiver", appProcess.getPackageName()+";"+appProcess.pid);
            if (appProcess.getPackageName().equals(packageName) && appProcess.foreground) {
                return true;
            }
        }
        return false;

    }

//com.android.packageinstaller.PackageInstallerActivity;PackageName = com.android.packageinstaller
	//D:\adt-bundle-windows\sdk\platform-tools>adb shell am force-stop com.android.packageinstaller
	//D:\adt-bundle-windows\sdk\platform-tools>adb shell
	//shell@cancro:/ $ pm clear com.android.packageinstaller
	//pm clear com.android.packageinstaller
	//Success
	//10-21 11:35:40.636: E/TopTaskReceiver(11654): className = com.kuzhuan.activitys.MainActivity;PackageName = com.kuzhuan


	  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	    public static void queryUsageStats(Context context) {
	        class RecentUseComparator implements Comparator<UsageStats> {
	            @Override
	            public int compare(UsageStats lhs, UsageStats rhs) {
	                return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
	            }
	        }
	        RecentUseComparator mRecentComp = new RecentUseComparator();
	        long ts = System.currentTimeMillis();
	        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
	        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000 * 10, ts);
	        if (usageStats == null || usageStats.size() == 0) {
	        	return;
	        }
	        Collections.sort(usageStats, mRecentComp);
	        String currentTopPackage = usageStats.get(0).getPackageName();
	        Log.e("TopTaskReceiver", "currentTopPackage="+currentTopPackage);
	    }

	public static ComponentName getTopComponentName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentName = taskInfo.get(0).topActivity;
		String log = "className = "+componentName.getClassName()+";PackageName = "+componentName.getPackageName();
		Toast.makeText(context, log, Toast.LENGTH_LONG).show();
		Log.e("TopTaskReceiver", log);
		return componentName;
	}
}
