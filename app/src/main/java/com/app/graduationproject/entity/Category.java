package com.app.graduationproject.entity;

/**
 * Created by lenovo on 2016/10/25.
 */
public class Category {

    private int imageId;
    private String title;
    private String video_clip;
    private String people;

    public Category(int imageId, String title, String video_clip, String people) {
        this.imageId = imageId;
        this.title = title;
        this.video_clip = video_clip;
        this.people = people;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_clip() {
        return video_clip;
    }

    public void setVideo_clip(String video_clip) {
        this.video_clip = video_clip;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
