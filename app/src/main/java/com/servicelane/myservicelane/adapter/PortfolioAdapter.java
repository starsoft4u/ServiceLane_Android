package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.User;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {
    private Context context;
    private User user;
    private List<Portfolio> portfolios;
    private boolean imageOnly;

    public PortfolioAdapter(Context context, User user, List<Portfolio> portfolios, boolean imageOnly) {
        this.context = context;
        this.user = user;
        this.portfolios = portfolios;
        this.imageOnly = imageOnly;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            if (imageOnly) {
                return new UserFolder(inflater.inflate(R.layout.layout_user, viewGroup, false));
            } else {
                return new AddPortFolioHolder(inflater.inflate(R.layout.layout_add_portfolio, viewGroup, false));
            }
        } else {
            return new DefaultHolder(inflater.inflate(R.layout.layout_portfolio, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Check if normal cell or footer
        if (viewHolder instanceof UserFolder) {
            UserFolder holder = (UserFolder) viewHolder;
            holder.textSince.setText(Helper.string(context, R.string.member_since, Helper.date(user.getCreatedOn(), "yyyy")));
            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.imgAvatar);
            holder.imgCheck.setVisibility(user.isFeatured() ? View.VISIBLE : View.GONE);
            holder.textName.setText(user.getName());
            holder.ratingBar.setRating(user.getRate());
        } else if (viewHolder instanceof AddPortFolioHolder) {
            AddPortFolioHolder holder = (AddPortFolioHolder) viewHolder;
            holder.btnAdd.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnAddPortfolio()));
        } else {
            DefaultHolder holder = (DefaultHolder) viewHolder;
            Portfolio portfolio = portfolios.get(position - 1);

            // Image
            Picasso.get().load(portfolio.getImage()).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(holder.imgPortfolio);
            // Date
            holder.textDate.setText(Helper.string(context, R.string.uploaded_with, Helper.date(portfolio.getUploadedAt(), "M/d/yy")));
            // Click
            holder.imgPortfolio.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnPortfolioPhotoTapped(portfolio)));
            // Delete
            holder.btnDelete.setOnClickListener(v -> EventBus.getDefault().post(new Events.OnDeletePortfolio(portfolio)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return portfolios.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        ViewHolder(View v) {
            super(v);
            root = v;
        }
    }

    class DefaultHolder extends ViewHolder {
        @BindView(R.id.imgPortfolio)
        ImageView imgPortfolio;

        @BindView(R.id.textDate)
        TextView textDate;

        @BindView(R.id.btnDelete)
        Button btnDelete;

        DefaultHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            textDate.setVisibility(imageOnly ? View.GONE : View.VISIBLE);
            btnDelete.setVisibility(imageOnly ? View.GONE : View.VISIBLE);
        }
    }

    class UserFolder extends ViewHolder {
        @BindView(R.id.textSince)
        TextView textSince;

        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;

        @BindView(R.id.imgCheck)
        ImageView imgCheck;

        @BindView(R.id.textName)
        TextView textName;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        UserFolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    class AddPortFolioHolder extends ViewHolder {
        @BindView(R.id.btnAdd)
        ImageButton btnAdd;

        AddPortFolioHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
