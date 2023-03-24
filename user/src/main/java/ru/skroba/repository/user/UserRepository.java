package ru.skroba.repository.user;

import com.mongodb.Function;
import com.mongodb.rx.client.Success;
import ru.skroba.model.user.User;
import rx.Observable;

public interface UserRepository {
    Observable<User> getUser(long userId);
    
    Observable<Success> createUser(User user);
    
    Observable<Success> upsertUser(long userId, Function<User, User> updater);
}
