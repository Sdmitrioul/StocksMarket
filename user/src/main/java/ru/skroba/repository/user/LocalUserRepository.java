package ru.skroba.repository.user;

import com.mongodb.Function;
import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.model.user.User;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class LocalUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    
    @Override
    public Observable<User> getUser(final long userId) {
        return users.stream()
                .filter(it -> it.userId() == userId)
                .findFirst()
                .map(Observable::just)
                .orElse(Observable.error(new RepositoryException("No such user!")));
    }
    
    @Override
    public Observable<Success> createUser(final User user) {
        if (users.stream()
                .anyMatch(it -> it.userId() == user.userId())) {
            return Observable.error(new RepositoryException("User already exist!"));
        }
        users.add(user);
        return Observable.just(Success.SUCCESS);
    }
    
    @Override
    public Observable<Success> upsertUser(final long userId, final Function<User, User> updater) {
        users.replaceAll(updater::apply);
        return Observable.just(Success.SUCCESS);
    }
}
