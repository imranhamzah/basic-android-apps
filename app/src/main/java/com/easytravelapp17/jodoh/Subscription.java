package com.easytravelapp17.jodoh;

public class Subscription {
    private String plan_title;
    private String duration;
    private String plan_image;

    public Subscription() {
        this.plan_title = plan_title;
        this.duration = duration;
        this.plan_image = plan_image;
    }

    public String getPlan_title() {
        return plan_title;
    }

    public void setPlan_title(String plan_title) {
        this.plan_title = plan_title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlan_image() {
        return plan_image;
    }

    public void setPlan_image(String plan_image) {
        this.plan_image = plan_image;
    }
}
