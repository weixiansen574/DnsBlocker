package top.weixiansen574.DnsBlocker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.MyViewHolder> {
    Context context;
    List<ApplicationInfo> installedApplications;
    PackageManager packageManager;
    Config config;


    public AppListAdapter(Context context, List<ApplicationInfo> installedApplications) {
        this.context = context;
        this.packageManager = context.getPackageManager();
        this.installedApplications = installedApplications;
        this.config = Config.newInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_app_info, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ApplicationInfo applicationInfo = installedApplications.get(position);
        holder.appName.setText(applicationInfo.loadLabel(packageManager).toString());
        holder.packageName.setText(applicationInfo.packageName);
        holder.appLogo.setImageDrawable(applicationInfo.loadIcon(packageManager));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (config == null) {
                    Toast.makeText(context, "无法读取配置文件，模块是否已启用？", Toast.LENGTH_SHORT).show();
                } else {
                    View editTextView = View.inflate(context,R.layout.edit_text,null);
                    EditText editText = editTextView.findViewById(R.id.edit_text);
                    editText.setText(config.getDnsRule(applicationInfo.packageName));
                    new AlertDialog.Builder(context)
                            .setTitle("设置域名过滤规则:"+applicationInfo.packageName)
                            .setView(editTextView)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                System.out.println(editText);
                                config.putDnsRule(editText.getText().toString(), applicationInfo.packageName);
                                Toast.makeText(context, "规则已设置", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton(android.R.string.cancel, (dialog, which) -> {

                            })
                            .setNeutralButton("删除规则", (dialog, which) -> {
                                config.removeDnsRule(applicationInfo.packageName);
                            }).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return installedApplications.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<ApplicationInfo> installedApplications) {
        this.installedApplications = installedApplications;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView appLogo = itemView.findViewById(R.id.appLogo);
        TextView appName = itemView.findViewById(R.id.appName);
        TextView packageName = itemView.findViewById(R.id.packageName);

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
