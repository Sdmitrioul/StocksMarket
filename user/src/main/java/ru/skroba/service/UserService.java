package ru.skroba.service;

import ru.skroba.exceptions.ServiceException;
import ru.skroba.model.user.User;
import ru.skroba.repository.stocks.StocksRepository;
import ru.skroba.repository.user.UserRepository;
import rx.Observable;

public class UserService {
    public final StocksRepository stocksRepository;
    private final UserRepository userRepository;
    
    public UserService(final UserRepository userRepository, final StocksRepository stocksRepository) {
        this.userRepository = userRepository;
        this.stocksRepository = stocksRepository;
    }
    
    public Observable<User> addUser(String userName, double userMoney) {
        final User user = new User(userName.hashCode(), userName, userMoney);
        return userRepository.createUser(user).map(it -> user);
    }
    
    public Observable<User> addToUserBalance(long userId, double amount) {
        return userRepository.upsertUser(userId, user -> user.addMoney(amount))
                .flatMap(res -> userRepository.getUser(userId));
    }
    
    
}
