package com.sweng.InteractiveStory.controller.dto;

public class UserRequest {
    private String uid;
    private String email;
    private String username;
    private boolean storyAdmin;

    // Getters e Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStoryAdmin() {
        return storyAdmin;
    }

    public void setStoryAdmin(boolean storyAdmin) {
        this.storyAdmin = storyAdmin;
    }
}

