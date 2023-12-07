package top.weixiansen574.DnsBlocker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class Config {
    private SharedPreferences sp;
    private Config (SharedPreferences sp){
        this.sp = sp;
    }
    @Nullable
    public static Config newInstance(Context context){
        try {
            return new Config(context.getSharedPreferences("dns_block_list", Context.MODE_WORLD_READABLE));
        } catch (SecurityException ignored) {
            System.out.println("配置获取失败");
            return null;
        }
    }

    public void putDnsRule(String dnsRule,String packageName){
        sp.edit().putString(packageName,dnsRule).apply();
    }
    @Nullable
    public String getDnsRule(String packageName){
        return sp.getString(packageName,null);
    }
    public void removeDnsRule(String packageName){
        sp.edit().remove(packageName).apply();
    }

    public void setEnablePrintPassedHostName(boolean enable){
        sp.edit().putBoolean("enable_print_passed_host_name",enable).apply();
    }

    public boolean getEnablePrintPassedHostName(){
        return sp.getBoolean("enable_print_passed_host_name",false);
    }

    public void setEnablePrintBlockedHostName(boolean enable){
        sp.edit().putBoolean("enable_print_blocked_host_name",enable).apply();
    }
    public boolean getEnablePrintBlockedHostName(){
        return sp.getBoolean("enable_print_blocked_host_name",false);
    }
}
