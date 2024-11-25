package com.sweng.InteractiveStory.entity.user;

public class Scrittore extends Giocatore {
    private boolean storyAdmin;

    public Scrittore(String uid, String nome, String email, boolean storyAdmin) {
        super(uid, nome, email);
        this.storyAdmin = storyAdmin;
    }

    public boolean isStoryAdmin() {
        return storyAdmin;
    }

    public void setStoryAdmin(boolean storyAdmin) {
        this.storyAdmin = storyAdmin;
    }
}
