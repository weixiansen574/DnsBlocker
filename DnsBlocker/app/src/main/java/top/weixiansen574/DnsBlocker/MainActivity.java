package top.weixiansen574.DnsBlocker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    Config config;
    List<ApplicationInfo> installedApplications;

    PackageManager packageManager;
    AppListAdapter appListAdapter;

    private boolean enablePrintPassedHostName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        config = Config.newInstance(context);
        packageManager = getPackageManager();
        installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        appListAdapter = new AppListAdapter(context,installedApplications);
        recyclerView.setAdapter(appListAdapter);
    }

    private List<ApplicationInfo> searchFilter(String keyword){
        if (!TextUtils.isEmpty(keyword)) {
            List<ApplicationInfo> searchedInstalledApplications = new ArrayList<>();
            for (ApplicationInfo installedApplication : installedApplications) {
                if (installedApplication.packageName.contains(keyword) || installedApplication.loadLabel(packageManager).toString().contains(keyword)) {
                    searchedInstalledApplications.add(installedApplication);
                }
            }
            return searchedInstalledApplications;
        } else {
            return installedApplications;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                appListAdapter.refreshData(searchFilter(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    appListAdapter.refreshData(installedApplications);
                }
                return false;
            }
        });
        MenuItem item1 = menu.findItem(R.id.print_blocked_host_name);
        MenuItem item2 = menu.findItem(R.id.print_passed_host_name);
        item1.setChecked(config.getEnablePrintBlockedHostName());
        item2.setChecked(config.getEnablePrintPassedHostName());
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                config.setEnablePrintBlockedHostName(!config.getEnablePrintBlockedHostName());
                boolean enablePrintBlockedHostName = config.getEnablePrintBlockedHostName();
                if (enablePrintBlockedHostName){
                    Toast.makeText(context, "“打印已屏蔽的域名”已启用，请前往LSPosed管理器查看日志！", Toast.LENGTH_SHORT).show();
                }
                item1.setChecked(config.getEnablePrintBlockedHostName());
                return false;
            }
        });
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                config.setEnablePrintPassedHostName(!config.getEnablePrintPassedHostName());
                boolean enablePrintPassedHostName = config.getEnablePrintPassedHostName();

                if (enablePrintPassedHostName){
                    Toast.makeText(context, "“打印未屏蔽的域名”已启用，请前往LSPosed管理器查看日志！", Toast.LENGTH_SHORT).show();
                }
                item2.setChecked(enablePrintPassedHostName);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}