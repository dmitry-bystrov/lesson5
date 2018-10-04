package ru.geekbrains.android3_5.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import ru.geekbrains.android3_5.mvp.model.entity.User;
import ru.geekbrains.android3_5.mvp.model.repo.AAUserRepo;
import ru.geekbrains.android3_5.mvp.model.repo.PaperUserRepo;
import ru.geekbrains.android3_5.mvp.model.repo.RealmUserRepo;
import ru.geekbrains.android3_5.mvp.view.MainView;
import ru.geekbrains.android3_5.mvp.view.RepoRowView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView>
{
    class RepoListPresenter implements IRepoListPresenter
    {
        @Override
        public void bindRepoListRow(int pos, RepoRowView rowView)
        {
            if (user != null)
            {
                rowView.setTitle(user.getRepos().get(pos).getName());
            }
        }

        @Override
        public int getRepoCount()
        {
            return user == null || user.getRepos() == null ? 0 : user.getRepos().size();
        }
    }

    private RepoListPresenter repoListPresenter = new RepoListPresenter();
    private Scheduler scheduler;
    private RealmUserRepo userRepo;
    private User user;

    public MainPresenter(Scheduler scheduler)
    {
        this.scheduler = scheduler;
        userRepo = new RealmUserRepo();
    }

    @Override
    protected void onFirstViewAttach()
    {
        super.onFirstViewAttach();
        getViewState().init();
        loadInfo();
    }

    @SuppressLint("CheckResult")
    public void loadInfo()
    {
        getViewState().showLoading();
        userRepo.getUser("googlesamples")
                .observeOn(scheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().hideLoading();
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .observeOn(scheduler)
                            .subscribe(repositories -> {
                                this.user.setRepos(repositories);
                                getViewState().updateRepoList();
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user repos");
                                getViewState().showError(throwable.getMessage());
                            });


                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                    getViewState().showError(throwable.getMessage());
                });
    }


    public RepoListPresenter getRepoListPresenter()
    {
        return repoListPresenter;
    }
}
