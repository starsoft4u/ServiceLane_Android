package com.servicelane.myservicelane.model;

public class MenuCell {
    private int title;
    private boolean checked = false;
    private Type type;

    public MenuCell(int title, Type type) {
        this.setTitle(title);
        this.setType(type);
    }

    public MenuCell(int title, Type type, boolean value) {
        this.setTitle(title);
        this.setChecked(value);
        this.setType(type);
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean value) {
        this.checked = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        DEFAULT, TITLE_ONLY, SWITCH, BUTTON
    }
}
