package com.servicelane.myservicelane.view.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kennyc.view.MultiStateView;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.ContactItemAdapter;
import com.servicelane.myservicelane.adapter.GridDecoration;
import com.servicelane.myservicelane.adapter.ServiceMiniAdapter;
import com.servicelane.myservicelane.model.ContactItem;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class IdentityFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    private static final String ARG_SERVICES = "services";

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

    @BindView(R.id.textCategory)
    TextView textCategory;

    @BindView(R.id.textParish)
    TextView textParish;

    @BindView(R.id.textPhotoCnt)
    TextView textPhotoCnt;

    @BindView(R.id.textServiceCnt)
    TextView textServiceCnt;

    @BindView(R.id.textReviewCnt)
    TextView textReviewCnt;

    @BindView(R.id.contactItemView)
    RecyclerView contactItemView;

    @BindView(R.id.textShortBio)
    TextView textShortBio;

    @BindView(R.id.textServicePackageCnt)
    TextView textServicePackageCnt;

    @BindView(R.id.stateView)
    MultiStateView stateView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private User user;
    private List<ServicePackage> services = new ArrayList<>();

    public static IdentityFragment newInstance(User user, List<ServicePackage> services) {
        IdentityFragment fragment = new IdentityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_USER, user);
        bundle.putParcelableArrayList(ARG_SERVICES, (ArrayList<ServicePackage>) services);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
            services = getArguments().getParcelableArrayList(ARG_SERVICES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_identity, container, false);

        ButterKnife.bind(this, view);

        // Load data
        textSince.setText(Helper.string(Objects.requireNonNull(getContext()), R.string.member_since, Helper.date(user.getCreatedOn(), "yyyy")));
        Picasso.get().load(user.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgAvatar);
        imgCheck.setVisibility(user.isFeatured() ? View.VISIBLE : View.GONE);
        textName.setText(user.getName());
        ratingBar.setRating(user.getRate());
        textCategory.setText(user.getCategory());
        textParish.setText(user.getParish());
        textPhotoCnt.setText(Helper.string(getContext(), R.string.photos_with, user.getPortfolioCnt()));
        textServiceCnt.setText(Helper.string(getContext(), R.string.services_with, user.getServiceCnt()));
        textReviewCnt.setText(Helper.string(getContext(), R.string.reviews_with, user.getReviewCnt()));

        // Contact items
        List<ContactItem> items = new ArrayList<>();

        if (!TextUtils.isEmpty(user.getPhone())) {
            items.add(new ContactItem(R.drawable.ic_phone, user.getPhone(), "tel:" + user.getPhoneCompact()));
        }
        if (!TextUtils.isEmpty(user.getWhatsApp())) {
            items.add(new ContactItem(R.drawable.ic_whatsapp, user.getWhatsApp(), "https://api.whatsapp.com/send?phone=" + user.getWhatsAppCompact()));
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            items.add(new ContactItem(R.drawable.ic_mail, R.string.send_me_an_email, "mailto:" + user.getEmail()));
        }
        if (!TextUtils.isEmpty(user.getFacebook())) {
            items.add(new ContactItem(R.drawable.ic_facebook, R.string.my_facebook_page, user.getFacebook()));
        }
        if (!TextUtils.isEmpty(user.getWebSite())) {
            items.add(new ContactItem(R.drawable.ic_website, R.string.visit_my_website, user.getWebSite()));
        }

        ContactItemAdapter contactItemAdapter = new ContactItemAdapter(getContext(), items);
        contactItemView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        contactItemView.addItemDecoration(new GridDecoration(2, Helper.pixel(Objects.requireNonNull(getContext()), 4), false));
        contactItemView.setItemAnimator(new DefaultItemAnimator());
        contactItemView.setNestedScrollingEnabled(false);
        contactItemView.setAdapter(contactItemAdapter);

        // Short bio
        if (TextUtils.isEmpty(user.getShortBio())) {
            textShortBio.setText(R.string.empty_short_bio);
            textShortBio.setTextColor(ContextCompat.getColor(getContext(), R.color.darkGrey));
            textShortBio.setGravity(Gravity.CENTER);
        } else {
            textShortBio.setText(user.getShortBio());
            textShortBio.setTextColor(Color.BLACK);
            textShortBio.setGravity(Gravity.NO_GRAVITY);
        }


        // Service count
        textServicePackageCnt.setText(Helper.string(getContext(), R.string.service_packages_with, user.getServiceCnt()));

        ServiceMiniAdapter adapter = new ServiceMiniAdapter(getContext(), services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        emptyText.setText(R.string.empty_service);
        stateView.setViewState(services.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);

        return view;
    }
}
