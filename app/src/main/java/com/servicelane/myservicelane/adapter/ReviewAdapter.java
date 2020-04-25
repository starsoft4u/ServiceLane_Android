package com.servicelane.myservicelane.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Review;
import com.servicelane.myservicelane.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private User user;
    private List<Review> reviews;

    public ReviewAdapter(Context context, User user, List<Review> reviews) {
        this.context = context;
        this.user = user;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            return new UserHolder(inflater.inflate(R.layout.layout_user, viewGroup, false));
        } else {
            return new ReviewHolder(inflater.inflate(R.layout.layout_review, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position == 0 && viewHolder instanceof UserHolder) {

            UserHolder holder = (UserHolder) viewHolder;
            holder.textSince.setText(Helper.string(context, R.string.member_since, Helper.date(user.getCreatedOn(), "yyyy")));
            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.imgAvatar);
            holder.imgCheck.setVisibility(user.isFeatured() ? View.VISIBLE : View.GONE);
            holder.textName.setText(user.getName());
            holder.ratingBar.setRating(user.getRate());

        } else if (position > 0 && viewHolder instanceof ReviewHolder) {

            final Review review = reviews.get(position - 1);
            ReviewHolder holder = (ReviewHolder) viewHolder;
            Spannable str = new SpannableString(review.getDescription() + " – " + review.getName() + ", " + Helper.date(review.getDate(), "dd/MM/yy"));
            str.setSpan(new TextAppearanceSpan(context, R.style.Text_Bold), review.getDescription().length() + 2, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.BLACK), review.getDescription().length() + 2, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            str.setSpan(new AbsoluteSizeSpan(14, true), review.getDescription().length() + 2, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.textReview.setText(str, TextView.BufferType.SPANNABLE);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return reviews.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;

        ViewHolder(View v) {
            super(v);
            root = v;
        }
    }

    class UserHolder extends ViewHolder {
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

        UserHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    class ReviewHolder extends ViewHolder {
        @BindView(R.id.textReview)
        TextView textReview;

        ReviewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
