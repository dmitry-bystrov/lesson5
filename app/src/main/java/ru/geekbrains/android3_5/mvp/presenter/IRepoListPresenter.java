package ru.geekbrains.android3_5.mvp.presenter;

import ru.geekbrains.android3_5.mvp.view.RepoRowView;


public interface IRepoListPresenter
{
    void bindRepoListRow(int pos, RepoRowView rowView);
    int getRepoCount();
}
