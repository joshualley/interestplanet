package com.example.interestplanet.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Planet extends AbstractModel {
    private String creatorId;
    @Exclude
    private User creator;
    private String name;
    private String cover;
    private List<String> memberIds;

    public Planet() {}
    public Planet(String creatorId, String cover, String name, List<String> memberIds) {
        this.creatorId = creatorId;
        this.cover = cover;
        this.name = name;
        this.memberIds = memberIds;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }


}
