package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.ServicePackage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private Context context;
    private List<ServicePackage> services;
    private boolean isStartUp;
    private int[] banners = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};

    public ServiceAdapter(Context context, List<ServicePackage> services, boolean isStartUp) {
        this.context = context;
        this.services = services;
        this.isStartUp = isStartUp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.layout_service, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.picker_text_1, new String[]{
                "Duration",
                ServicePackage.Duration.HOURLY.title(),
                ServicePackage.Duration.DAILY.title(),
                ServicePackage.Duration.WEEKLY.title(),
                ServicePackage.Duration.MONTHLY.title(),
                ServicePackage.Duration.YEARLY.title(),
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.pickerDuration.setAdapter(adapter);

        if (position < services.size()) {
            final ServicePackage service = services.get(position);
            viewHolder.textName.setText(service.getName());
            viewHolder.editName.setVisibility(View.GONE);
            viewHolder.textDescription.setVisibility(View.VISIBLE);
            viewHolder.textDescription.setText(service.getDescription());
            viewHolder.editDescription.setVisibility(View.GONE);
            viewHolder.editRate.setText(service.getRate());
            viewHolder.editRate.setEnabled(false);
            viewHolder.pickerDuration.setSelection(adapter.getPosition(service.getDuration().title()));
            viewHolder.pickerDuration.setEnabled(false);
            viewHolder.imgBanner.setImageResource(banners[position % banners.length]);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnRemoveServicePackage(service.getId())));
            viewHolder.btnSave.setVisibility(View.GONE);
        } else {
            viewHolder.textName.setText(R.string.create_new_service);
            viewHolder.editName.setVisibility(View.VISIBLE);
            viewHolder.editName.setText("");
            viewHolder.textDescription.setVisibility(View.GONE);
            viewHolder.editDescription.setVisibility(View.VISIBLE);
            viewHolder.editDescription.setText("");
            viewHolder.editRate.setText("");
            viewHolder.editRate.setEnabled(true);
            viewHolder.pickerDuration.setSelection(0);
            viewHolder.pickerDuration.setEnabled(true);
            viewHolder.imgBanner.setImageResource(R.drawable.banner4);
            viewHolder.btnDelete.setVisibility(View.GONE);
            viewHolder.btnSave.setVisibility(View.VISIBLE);
            viewHolder.btnSave.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnAddServicePackage(
                    viewHolder.editName.getText().toString(),
                    viewHolder.editDescription.getText().toString(),
                    viewHolder.editRate.getText().toString(),
                    ServicePackage.Duration.fromTitle(viewHolder.pickerDuration.getSelectedItem().toString()))));
            viewHolder.btnSave.setBackgroundResource(isStartUp ? R.drawable.button_green_s : R.drawable.button_primary);
        }
    }

    @Override
    public int getItemCount() {
        return services.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @BindView(R.id.imgBanner)
        ImageView imgBanner;

        @BindView(R.id.textName)
        TextView textName;

        @BindView(R.id.editName)
        EditText editName;

        @BindView(R.id.textDescription)
        TextView textDescription;

        @BindView(R.id.editDescription)
        EditText editDescription;

        @BindView(R.id.editRate)
        EditText editRate;

        @BindView(R.id.pickerDuration)
        AppCompatSpinner pickerDuration;

        @BindView(R.id.btnDelete)
        Button btnDelete;

        @BindView(R.id.btnSave)
        Button btnSave;

        ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.bind(this, v);
        }
    }
}
