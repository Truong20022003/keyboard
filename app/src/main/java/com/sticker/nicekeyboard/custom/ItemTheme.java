package com.sticker.nicekeyboard.custom;


public class ItemTheme {
    private int idTheme;
    private String name;
    private int img;
    private boolean isSelect = false;
    private String colorCode;


    public ItemTheme() {

    }


    public ItemTheme(int i, String str) {
        this.idTheme = i;
        this.name = str;
    }

    public ItemTheme(int i, String str, int img) {
        this.idTheme = i;
        this.name = str;
        this.img = img;
    }

    public ItemTheme(int idTheme, String name, int img, boolean isSelect) {
        this.idTheme = idTheme;
        this.name = name;
        this.img = img;
        this.isSelect = isSelect;
    }

    public ItemTheme(int idTheme, String name, int img, String colorCode) {
        this.idTheme = idTheme;
        this.name = name;
        this.img = img;
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getIdTheme() {
        return this.idTheme;
    }

    public String getName() {
        return this.name;
    }

    public int getImg() {
        return this.img;
    }
}
