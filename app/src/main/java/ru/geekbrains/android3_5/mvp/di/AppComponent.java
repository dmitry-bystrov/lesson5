package ru.geekbrains.android3_5.mvp.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.geekbrains.android3_5.mvp.di.modules.AppModule;
import ru.geekbrains.android3_5.mvp.di.modules.ImageModule;
import ru.geekbrains.android3_5.mvp.di.modules.RepoModule;
import ru.geekbrains.android3_5.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_5.ui.MainActivity;

@Singleton
@Component(modules = {
        AppModule.class,
        RepoModule.class,
        ImageModule.class
})
public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(MainActivity mainActivity);
}
