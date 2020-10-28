package com.vivek.wo.entity;

import com.google.gson.annotations.SerializedName;

//TODO 示例实体类
public class Repo {
    private int id;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    private String description;
    private Owner owner;
    @SerializedName("stargazers_count")
    private int stars;

    public static class Owner {
        private String login;
        private String url;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Owner{" +
                    "login='" + login + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", stars=" + stars +
                '}';
    }
}
