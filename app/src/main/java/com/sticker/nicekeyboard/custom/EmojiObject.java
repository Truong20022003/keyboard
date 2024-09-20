package com.sticker.nicekeyboard.custom;


public class EmojiObject {
    private String keyEmoji;
    private String nameEmoji;
    private String pathEmojiInAssert;

    public EmojiObject(String str, String str2) {
        this.nameEmoji = str;
        this.keyEmoji = str2;
    }

    public EmojiObject(String str) {
        this.keyEmoji = str;
    }

    public EmojiObject(String str, String str2, String str3) {
        this.nameEmoji = str;
        this.keyEmoji = str2;
        this.pathEmojiInAssert = str3;
    }

    public String getNameEmoji() {
        return this.nameEmoji;
    }

    public void setNameEmoji(String str) {
        this.nameEmoji = str;
    }

    public String getKeyEmoji() {
        return this.keyEmoji;
    }

    public void setKeyEmoji(String str) {
        this.keyEmoji = str;
    }

    public String getPathEmojiInAssert() {
        return this.pathEmojiInAssert;
    }

    public void setPathEmojiInAssert(String str) {
        this.pathEmojiInAssert = str;
    }
}
