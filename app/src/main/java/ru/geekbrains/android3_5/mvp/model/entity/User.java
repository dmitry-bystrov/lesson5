package ru.geekbrains.android3_5.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

public class User
{
    String login;
    String avatarUrl;
    String reposUrl;

    public User(String login, String avatarUrl,String reposUrl)
    {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.reposUrl = reposUrl;
    }

    List<Repository> repos = new ArrayList<>();

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getReposUrl()
    {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl)
    {
        this.reposUrl = reposUrl;
    }

    public List<Repository> getRepos()
    {
        return repos;
    }

    public void setRepos(List<Repository> repos)
    {
        this.repos = repos;
    }
}
