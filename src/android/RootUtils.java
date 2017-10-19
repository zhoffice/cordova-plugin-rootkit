package com.cnwidsom.rootkit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by hitachi on 2017/10/9.
 */

public class RootUtils {
  /**
   * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
   *
   * @param command 命令： String apkRoot="chmod 777 "+getPackageCodePath();
   *                RootCommand(apkRoot);
   * @return 应用程序是/否获取Root权限
   */
  public static boolean rootCommand(String command) {

    Process process = null;
    DataOutputStream os = null;

    try {
      process = Runtime.getRuntime().exec("su");
      os = new DataOutputStream(process.getOutputStream());
      os.writeBytes(command + "\n");
      os.writeBytes("exit\n");
      os.flush();
      process.waitFor();

      if (process.exitValue() != 0) {
        throw new Exception("设备尚未ROOT");
      }
    } catch (Exception e) {
      Log.i("*** INFO ***", "ROOT REE" + e.getMessage());
      return false;
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        process.destroy();
      } catch (Exception e) {
      }
    }

    Log.i("*** INFO ***", "Root SUC ");
    return true;

  }

  /**
   * 判断某个界面是否在前台
   *
   * @param context   Context
   * @param className 界面的类名
   * @return 是否在前台显示
   */
  public static boolean isForeground(Context context, String className) {
    if (context == null || TextUtils.isEmpty(className))
      return false;
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
    if (list != null && list.size() > 0) {
      ComponentName cpn = list.get(0).topActivity;
      if (className.equals(cpn.getClassName()))
        return true;
    }
    return false;
  }

  /**
   * 判断Activity是否活动
   *
   * @param context      一个context
   * @param activityName 要判断Activity
   * @return boolean
   */
  public static boolean isActivityAlive(Context context, String activityName) {
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
    for (ActivityManager.RunningTaskInfo info : list) {
      // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
      if (info.topActivity.toString().equals(activityName) || info.baseActivity.toString().equals(activityName)) {
        Log.i(RootUtils.class.getName(), info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
        return true;
      }
    }
    return false;
  }
}
