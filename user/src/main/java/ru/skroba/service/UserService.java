package ru.skroba.service;

import com.mongodb.rx.client.Success;
import ru.skroba.client.MarketProvider;
import ru.skroba.exceptions.ServiceException;
import ru.skroba.model.stocks.CompanyStocks;
import ru.skroba.model.stocks.StocksWithCost;
import ru.skroba.model.stocks.UserStocks;
import ru.skroba.model.user.User;
import ru.skroba.repository.stocks.StocksRepository;
import ru.skroba.repository.user.UserRepository;
import rx.Observable;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    public final StocksRepository stocksRepository;
    private final UserRepository userRepository;
    
    private final MarketProvider market;
    
    public UserService(final UserRepository userRepository, final StocksRepository stocksRepository,
                       final MarketProvider market) {
        this.userRepository = userRepository;
        this.stocksRepository = stocksRepository;
        this.market = market;
    }
    
    public Observable<User> addUser(String userName, double userMoney) {
        final User user = new User(userName.hashCode(), userName, userMoney);
        return userRepository.createUser(user)
                .map(it -> user);
    }
    
    public Observable<Double> getUserAmountOfSavings(long userId) {
        return getUserStocksWithCost(userId).map(stocks -> stocks.stream()
                        .map(it -> it.cost() * it.count())
                        .reduce(0., Double::sum))
                .flatMap(stocksCost -> getUser(userId).map(user -> user.money() + stocksCost));
    }
    
    public Observable<List<StocksWithCost>> getUserStocksWithCost(long userId) {
        return market.getStocks()
                .map(stocks -> stocks.stream()
                        .collect(Collectors.toMap(CompanyStocks::companyName, CompanyStocks::price)))
                .flatMap(map -> stocksRepository.getUserStocks(userId)
                        .map(userStocks -> userStocks.stream()
                                .map(it -> new StocksWithCost(it.companyName(), it.amount(),
                                        map.getOrDefault(it.companyName(), Double.NaN)))
                                .toList()));
    }
    
    public Observable<User> getUser(long userId) {
        return userRepository.getUser(userId);
    }
    
    public Observable<Success> sellUserStocks(long userId, String companyName) {
        return stocksRepository.deleteStocks(userId, companyName)
                .flatMap(userStocks -> market.sellStocks(companyName, userStocks.amount())
                        .doOnError(e -> stocksRepository.addStocks(userStocks)
                                .flatMap(it -> Observable.error(new ServiceException("Can't sell stocks"))))
                        .flatMap(money -> addToUserBalance(userId, money).map(it -> Success.SUCCESS)));
    }
    
    public Observable<User> addToUserBalance(long userId, double amount) {
        return userRepository.upsertUser(userId, user -> user.addMoney(amount))
                .flatMap(res -> userRepository.getUser(userId));
    }
    
    public Observable<Success> buyStocks(long userId, String companyName, Long count) {
        return market.buyStocks(companyName, count)
                .flatMap(cost -> userRepository.upsertUser(userId,
                                prev -> new User(userId, prev.userName(), prev.money() - cost))
                        .flatMap(res -> stocksRepository.addStocks(new UserStocks(userId, companyName, count))
                                .map(it -> Success.SUCCESS)));
    }
}
