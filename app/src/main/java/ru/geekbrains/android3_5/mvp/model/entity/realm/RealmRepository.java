package ru.geekbrains.android3_5.mvp.model.entity.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRepository extends RealmObject
{
    @PrimaryKey
    public String id;
    public String name;
}
