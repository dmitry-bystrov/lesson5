package ru.geekbrains.android3_5.ui;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.geekbrains.android3_5.App;
import ru.geekbrains.android3_5.R;
import ru.geekbrains.android3_5.mvp.model.image.ImageLoader;
import ru.geekbrains.android3_5.mvp.model.image.android.ImageLoaderGlide;
import ru.geekbrains.android3_5.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_5.mvp.view.MainView;

public class MainActivity extends MvpAppCompatActivity implements MainView
{

    @BindView(R.id.iv_avatar) ImageView avatarImageView;
    @BindView(R.id.tv_error) TextView errorTextView;
    @BindView(R.id.tv_username) TextView usernameTextView;
    @BindView(R.id.pb_loading) ProgressBar loadingProgressBar;
    @BindView(R.id.rv_repos) RecyclerView reposRecyclerView;

    RepoRVAdapter adapter;

    @InjectPresenter
    MainPresenter presenter;

    @Inject ImageLoader<ImageView> imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getInstance().getAppComponent().inject(MainActivity.this);

        reposRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reposRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void init()
    {
        adapter = new RepoRVAdapter(presenter.getRepoListPresenter());
        reposRecyclerView.setAdapter(adapter);
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter()
    {
        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }


    @Override
    public void showAvatar(String avatarUrl)
    {
        imageLoader.loadInto(avatarUrl, avatarImageView);
    }

    @Override
    public void showError(String message)
    {

        errorTextView.setText(message);
    }

    @Override
    public void showLoading()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading()
    {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateRepoList()
    {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setUsername(String username)
    {
        usernameTextView.setText(username);
    }
}
