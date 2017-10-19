package com.cnwidsom.rootkit;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class echoes a string called from JavaScript.
 */
public class RootKitPlugin extends CordovaPlugin {
  @Override
  protected void pluginInitialize() {
    super.pluginInitialize();
  }

  private boolean isActivityMonitorServiceRunning = false;

  protected Activity getActivity() {
    return this.cordova.getActivity();
  }

  @Override
  public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("changeOomAdj")) {
      this.changeOomAdj(args.getInt(0), callbackContext);
      return true;
    } else if (action.equals("lockOomAdj")) {
      Timer t = new Timer();
      long interval = args.getLong(1);
      t.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          try {
            RootKitPlugin.this.changeOomAdj(args.getInt(0), callbackContext);
          } catch (Exception ex) {
            Log.e(RootKitPlugin.class.getName(), "", ex);
          }
        }
      }, new Date(), interval);
      return true;
    } else if (action.equals("startMainActivityHolder")) {
      startMainActivityHolder(args.getLong(0), callbackContext);
    }
    return false;
  }

  private void changeOomAdj(int adjNum, CallbackContext callbackContext) {
    try {
      int pid = ProcessUtils.getProcessId(getActivity());
      callbackContext.success(ProcessUtils.setProcessOomAdj(pid, adjNum) ? 1 : 0);
    } catch (Exception ex) {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }

  private void startMainActivityHolder(long interval, CallbackContext callbackContext) {
    if (!isActivityMonitorServiceRunning) {
      Log.i(this.getClass().getName(), "Starting Activity Monitor service");
      Intent intent = new Intent(getActivity(), ActivityMonitorService.class);
      intent.putExtra("interval", interval);
      getActivity().startService(intent);
      isActivityMonitorServiceRunning = true;
    }
  }
}
