package com.servicelane.myservicelane.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.MenuCell;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<MenuCell> items;

    public MenuAdapter(List<MenuCell> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (i) {
            case 1:
                return new DefaultViewHolder(inflater.inflate(R.layout.layout_menu_disclosure, viewGroup, false), false);
            case 2:
                return new SwitchViewHolder(inflater.inflate(R.layout.layout_menu_switch, viewGroup, false));
            case 3:
                return new ViewHolder(inflater.inflate(R.layout.layout_menu_button, viewGroup, false));
            default:
                return new DefaultViewHolder(inflater.inflate(R.layout.layout_menu_disclosure, viewGroup, false), true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MenuCell item = items.get(position);
        switch (item.getType()) {
            case TITLE_ONLY:
                return 1;
            case SWITCH:
                return 2;
            case BUTTON:
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MenuCell item = items.get(position);

        viewHolder.textTitle.setText(item.getTitle());
        if (item.getType() == MenuCell.Type.DEFAULT || item.getType() == MenuCell.Type.BUTTON) {
            viewHolder.root.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnMenuItemSelected(item)));
            viewHolder.root.setBackgroundResource(R.drawable.clickable);
        } else {
            viewHolder.root.setBackgroundResource(R.drawable.none);
            if (viewHolder instanceof SwitchViewHolder) {
                ((SwitchViewHolder) viewHolder).btnSwitch.setChecked(item.isChecked());
                ((SwitchViewHolder) viewHolder).btnSwitch.setOnCheckedChangeListener((compoundButton, b) -> EventBus.getDefault().post(new Events.OnMenuItemSelected(item, b)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @BindView(R.id.textTitle)
        TextView textTitle;

        ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.bind(this, v);
        }
    }

    class DefaultViewHolder extends ViewHolder {
        @BindView(R.id.imgDisclosureIndicator)
        ImageView imgDisclosureIndicator;

        DefaultViewHolder(View v, boolean showDisclosureIndicator) {
            super(v);
            imgDisclosureIndicator.setVisibility(showDisclosureIndicator ? View.VISIBLE : View.GONE);
        }
    }

    class SwitchViewHolder extends ViewHolder {
        @BindView(R.id.btnSwitch)
        Switch btnSwitch;

        SwitchViewHolder(View v) {
            super(v);
        }
    }
}
