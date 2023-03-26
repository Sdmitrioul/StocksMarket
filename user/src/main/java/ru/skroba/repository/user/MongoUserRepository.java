package ru.skroba.repository.user;

import com.mongodb.Function;
import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.model.user.User;
import ru.skroba.model.user.UserFactory;
import ru.skroba.repository.AbstractRepository;
import ru.skroba.repository.Database;
import rx.Observable;

import java.util.function.Supplier;

public class MongoUserRepository extends AbstractRepository implements UserRepository {
    
    private static final String COLLECTION_NAME = "users";
    
    public MongoUserRepository(final Database database) {
        super(COLLECTION_NAME, database);
    }
    
    private <T> Observable<T> manageUser(long uid, Function<User, Observable<T>> ifPresent,
                                         Supplier<Observable<T>> otherwise) {
        return getUser(uid).onErrorReturn(null)
                .flatMap(value -> {
                    if (value == null) {
                        return otherwise.get();
                    }
                    
                    return ifPresent.apply(value);
                });
    }
    
    @Override
    public Observable<User> getUser(final long userId) {
        return getCollection().find(Filters.eq(UserFactory.USER_ID, userId))
                .toObservable()
                .map(UserFactory::of)
                .defaultIfEmpty(null)
                .flatMap(user -> user == null ? Observable.error(
                        new RepositoryException("No such user")) : Observable.just(user));
    }
    
    @Override
    public Observable<Success> createUser(final User user) {
        return manageUser(user.userId(),
                (u) -> Observable.error(new RepositoryException("User with name " + u.userName() + " already exist!")),
                () -> getCollection().insertOne(UserFactory.toDocument(user)));
    }
    
    @Override
    public Observable<Success> upsertUser(final long userId, final Function<User, User> updater) {
        return manageUser(userId, u -> Observable.just(updater.apply(u))
                .map(it -> Success.SUCCESS), () -> Observable.error(new RepositoryException("No  such  user!")));
    }
}
