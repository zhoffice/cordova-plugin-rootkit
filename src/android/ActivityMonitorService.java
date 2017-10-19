package com.cnwidsom.rootkit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cnwidsom.bcia.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hitachi on 2017/10/19.
 */

public class ActivityMonitorService extends Service {
  Timer timer = null;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void startCheckLoop(long interval) {
    if (timer == null) {
      timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          if (!RootUtils.isForeground(ActivityMonitorService.this, MainActivity.class.getName())) {
            Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
          }
          Log.e(this.getClass().getName(), "Running Check Loop.");
        }
      }, 0, interval);
    }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //默认300秒检测一次
    long interval = intent.getLongExtra("interval", 300000);
    startCheckLoop(interval);
    return START_STICKY;
  }
}
