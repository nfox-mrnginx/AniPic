package com.android.nuruha.anipic;

/**
 * Created by nuruha on 19/01/2017.
 */

public class imagec {
    private String image;
    private String source;
    private String name;
    private int download;

    public imagec(){

    }
    public imagec(String image, String source, String name, int download) {
        this.image = image;
        this.source = source;
        this.name = name;
        this.download = download;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }
}
