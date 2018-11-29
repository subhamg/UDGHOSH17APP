package com.iitk.udghosh.models;

/**
 * Created by hiteshkr on 11/10/17.
 */

public class InfoCard {
    private String name;
    private String thumbnail;

    public InfoCard() {
    }

    public InfoCard(String name, String thumbnail) {
        this.name = name;

        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
