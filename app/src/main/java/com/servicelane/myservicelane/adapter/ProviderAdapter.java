package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.user.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ViewHolder> {
    private Context context;
    private List<User> users;

    public ProviderAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.layout_provider, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(viewHolder.imgAvatar);
        viewHolder.imgCheck.setVisibility(user.isFeatured() ? View.VISIBLE : View.GONE);
        viewHolder.textName.setText(user.getName());
        viewHolder.ratingBar.setRating(user.getRate());
        viewHolder.textCategory.setText(user.getCategory());
        viewHolder.textParish.setText(user.getParish());
        viewHolder.textReview.setText(Helper.string(context, R.string.with_reviews, user.getReviewCnt()));
        viewHolder.textPhotoCnt.setText(Helper.string(context, R.string.photos_with, user.getPortfolioCnt()));
        viewHolder.textServiceCnt.setText(Helper.string(context, R.string.services_with, user.getServiceCnt()));
        viewHolder.root.setOnClickListener(v -> UserActivity.startActivity(context, user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        @BindView(R.id.imgAvatar)
        CircleImageView imgAvatar;

        @BindView(R.id.imgCheck)
        ImageView imgCheck;

        @BindView(R.id.textName)
        TextView textName;

        @BindView(R.id.ratingBar)
        MaterialRatingBar ratingBar;

        @BindView(R.id.textReview)
        TextView textReview;

        @BindView(R.id.textCategory)
        TextView textCategory;

        @BindView(R.id.textParish)
        TextView textParish;

        @BindView(R.id.textPhotoCnt)
        TextView textPhotoCnt;

        @BindView(R.id.textServiceCnt)
        TextView textServiceCnt;

        ViewHolder(View v) {
            super(v);
            root = v;
            ButterKnife.bind(this, v);
        }
    }
}
