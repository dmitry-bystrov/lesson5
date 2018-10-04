package ru.geekbrains.android3_5.mvp.model.entity.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmUser extends RealmObject
{
    @PrimaryKey
    public String login;
    public String avatarUrl;
    public String reposUrl;
    public RealmList<RealmRepository> repos = new RealmList<>();
}
