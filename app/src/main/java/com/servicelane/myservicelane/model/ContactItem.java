package com.servicelane.myservicelane.model;

public class ContactItem {
    private int titleRes;
    private int icon;
    private String title;
    private String url;

    public ContactItem(int icon, String title, String url) {
        this.icon = icon;
        this.title = title;
        this.url = url;
        this.titleRes = 0;
    }

    public ContactItem(int icon, int titleRes, String url) {
        this.icon = icon;
        this.titleRes = titleRes;
        this.title = "";
        this.url = url;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }
}
