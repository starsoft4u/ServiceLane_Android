package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.ContactItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {
    private Context context;
    private List<ContactItem> items;

    public ContactItemAdapter(Context context, List<ContactItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.layout_contact_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ContactItem item = items.get(position);
        viewHolder.imgIcon.setImageResource(item.getIcon());
        if (item.getTitleRes() == 0) {
            viewHolder.textItem.setText(item.getTitle());
        } else {
            viewHolder.textItem.setText(item.getTitleRes());
        }
        viewHolder.root.setOnClickListener(v -> {
            Intent intent;
            if (item.getUrl().startsWith("mailto:")) {
                intent = new Intent(Intent.ACTION_SENDTO);
            } else if (item.getUrl().startsWith("tel:")) {
                intent = new Intent(Intent.ACTION_CALL);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
            }
            intent.setData(Uri.parse(item.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @BindView(R.id.imgIcon)
        CircleImageView imgIcon;

        @BindView(R.id.textItem)
        TextView textItem;

        ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.bind(this, v);
        }
    }
}
