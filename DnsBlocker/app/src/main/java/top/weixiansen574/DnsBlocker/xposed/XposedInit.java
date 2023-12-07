package top.weixiansen574.DnsBlocker.xposed;

import android.os.Build;
import android.text.TextUtils;

import androidx.core.os.BuildCompat;

import java.net.InetAddress;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.weixiansen574.DnsBlocker.BuildConfig;

public class XposedInit implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XSharedPreferences pref = new XSharedPreferences(BuildConfig.APPLICATION_ID, "dns_block_list");
        String rulesText = pref.getString(loadPackageParam.packageName, null);
        String[] rules = null;
        if (rulesText != null) {
            rules = rulesText.split("\n");
        }
        boolean enable_print_passed_host_name = pref.getBoolean("enable_print_passed_host_name", false);
        boolean enable_print_blocked_host_name = pref.getBoolean("enable_print_blocked_host_name", false);

        String[] finalRules = rules;
        XposedHelpers.findAndHookMethod(InetAddress.class, "getAllByName", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String hostName = (String) param.args[0];
                boolean isBlocked = false;
                if (finalRules != null) {
                    for (String rule : finalRules) {
                        if (hostName.contains(rule)) {
                            param.setThrowable(new java.net.UnknownHostException());
                            param.setResult(null);
                            isBlocked = true;
                            break;
                        }
                    }
                    if (isBlocked) {
                        if (enable_print_blocked_host_name) {
                            XposedBridge.log("DnsBlocker[" + loadPackageParam.packageName + "] 已屏蔽域名:" + hostName);
                        }
                    } else {
                        if (enable_print_passed_host_name) {
                            XposedBridge.log("DnsBlocker[" + loadPackageParam.packageName + "] 已放行域名:" + hostName);
                        }
                    }
                }

            }
        });
    }
}
