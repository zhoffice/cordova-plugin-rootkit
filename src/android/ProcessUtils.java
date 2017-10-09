package com.cnwidsom.rootkit;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by hitachi on 2017/10/9.
 */

public class ProcessUtils {
  public static int getProcessId(Context context) {
    int pid = android.os.Process.myPid();
    String processName = "";
    ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

    List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
      if (runningAppProcessInfo.pid == pid) {
        processName = runningAppProcessInfo.processName;
      }
    }

    Log.i(ProcessUtils.class.getCanonicalName(), "当前进程名称:" + processName);
    return pid;
  }

  public static boolean setProcessOomAdj(int pid, int adjNum) {
    String apkRoot = "echo " + adjNum + " > /proc/" + pid + "/oom_adj";
    return RootUtils.rootCommand(apkRoot);
  }
}
