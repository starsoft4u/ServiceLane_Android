package com.servicelane.myservicelane;

import com.servicelane.myservicelane.model.Common;
import com.servicelane.myservicelane.model.MenuCell;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.Review;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.model.User;

import java.util.List;

public class Events {
    public static class OnMenuItemSelected {
        public MenuCell menuCell;
        public boolean checked;

        public OnMenuItemSelected(MenuCell menuCell) {
            this(menuCell, false);
        }

        public OnMenuItemSelected(MenuCell menuCell, boolean checked) {
            this.menuCell = menuCell;
            this.checked = checked;
        }
    }

    public static class OnPortfolioPhotoTapped {
        public Portfolio portfolio;

        public OnPortfolioPhotoTapped(Portfolio portfolio) {
            this.portfolio = portfolio;
        }
    }

    public static class OnAddPortfolio {
    }

    public static class OnDeletePortfolio {
        public Portfolio portfolio;

        public OnDeletePortfolio(Portfolio portfolio) {
            this.portfolio = portfolio;
        }
    }

    public static class OnHomeRequest {
        public int categoryId;

        public OnHomeRequest(int categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static class OnHomeResponse {
        public int categoryId;
        public int providerCnt;
        public List<User> providers;
        public List<Common> categories;
        public List<Common> topCategories;

        public OnHomeResponse(int categoryId, int providerCnt, List<User> providers, List<Common> categories, List<Common> topCategories) {
            this.categoryId = categoryId;
            this.providerCnt = providerCnt;
            this.providers = providers;
            this.categories = categories;
            this.topCategories = topCategories;
        }
    }

    public static class OnExploreRequest {
        public String parish;

        public OnExploreRequest(String parish) {
            this.parish = parish;
        }
    }

    public static class OnExploreResponse {
        public String parish;
        public List<String> parishes;
        public List<User> providers;

        public OnExploreResponse(String parish, List<String> parishes, List<User> providers) {
            this.parish = parish;
            this.parishes = parishes;
            this.providers = providers;
        }
    }

    public static class OnSearchFilter {
    }

    public static class OnSearchSetupRequest {
    }

    public static class OnSearchSetupResponse {
        public List<Common> categories;
        public List<String> parishes;
        public List<User> providers;

        public OnSearchSetupResponse(List<Common> categories, List<String> parishes, List<User> providers) {
            this.categories = categories;
            this.parishes = parishes;
            this.providers = providers;
        }
    }

    public static class OnSearchRequest {
        public int categoryId;
        public String parish;
        public int rate;
        public int reviewCnt;
        public String from;
        public String to;

        public OnSearchRequest(int categoryId, String parish, int rate, int reviewCnt, String from, String to) {
            this.categoryId = categoryId;
            this.parish = parish;
            this.rate = rate;
            this.reviewCnt = reviewCnt;
            this.from = from;
            this.to = to;
        }
    }

    public static class OnSearchResponse {
        public List<User> providers;

        public OnSearchResponse(List<User> providers) {
            this.providers = providers;
        }
    }

    public static class OnProfileRequest {
        public int userId;

        public OnProfileRequest(int userId) {
            this.userId = userId;
        }
    }

    public static class OnProfileResponse {
        public User user;
        public List<Portfolio> portfolios;
        public List<Review> reviews;
        public List<ServicePackage> services;

        public OnProfileResponse(User user, List<Portfolio> portfolios, List<Review> reviews, List<ServicePackage> services) {
            this.user = user;
            this.portfolios = portfolios;
            this.reviews = reviews;
            this.services = services;
        }
    }

    public static class OnAddServicePackage {
        public String name;
        public String description;
        public String rate;
        public ServicePackage.Duration duration;

        public OnAddServicePackage(String name, String description, String rate, ServicePackage.Duration duration) {
            this.name = name;
            this.description = description;
            this.rate = rate;
            this.duration = duration;
        }
    }

    public static class OnRemoveServicePackage {
        public int serviceId;

        public OnRemoveServicePackage(int serviceId) {
            this.serviceId = serviceId;
        }
    }
}
