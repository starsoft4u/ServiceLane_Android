package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.ServicePackage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceMiniAdapter extends RecyclerView.Adapter<ServiceMiniAdapter.ViewHolder> {
    private Context context;
    private List<ServicePackage> services;

    public ServiceMiniAdapter(Context context, List<ServicePackage> services) {
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.layout_service_mini, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ServicePackage service = services.get(position);
        viewHolder.textName.setText(service.getName());
        viewHolder.textDescription.setText(service.getDescription());
        viewHolder.textRate.setText(Helper.string(context, R.string.rate_with, String.valueOf(service.getRate()), service.getDuration().unit()));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @BindView(R.id.textName)
        TextView textName;

        @BindView(R.id.textDescription)
        TextView textDescription;

        @BindView(R.id.textRate)
        TextView textRate;

        ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.bind(this, v);
        }
    }
}
