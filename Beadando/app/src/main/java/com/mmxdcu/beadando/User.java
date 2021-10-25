package com.mmxdcu.beadando;

public class User {

    private String id;
    private String nickname;
    private String imageURL;

    public User(String id, String nickname, String imageURL) {
        this.id = id;
        this.nickname = nickname;
        this.imageURL = imageURL;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

