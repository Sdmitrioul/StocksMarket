package ru.skroba.repository.stocks;

import ru.skroba.model.stocks.UserStocks;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LocalStocksRepository implements StocksRepository {
    private final List<UserStocks> stocks = new ArrayList<>();
    
    @Override
    public Observable<UserStocks> addStocks(final UserStocks userStocks) {
        Predicate<UserStocks> predicate = it -> it.userId() == userStocks.userId() && it.companyName()
                .equals(userStocks.companyName());
        
        var dup = stocks.stream()
                .filter(predicate)
                .findAny()
                .orElse(null);
        
        var res = dup == null ? userStocks : new UserStocks(dup.userId(), userStocks.companyName(),
                userStocks.amount() + dup.amount());
        
        if (dup != null) {
            stocks.removeIf(predicate);
        }
        
        stocks.add(res);
        
        return Observable.just(res);
    }
    
    @Override
    public Observable<UserStocks> deleteStocks(final long userId, final String companyName) {
        Predicate<UserStocks> predicate = it -> it.userId() == userId && it.companyName()
                .equals(companyName);
        var dup = stocks.stream()
                .filter(predicate)
                .findAny()
                .orElse(null);
        
        if (dup != null) {
            stocks.removeIf(predicate);
        }
        
        return Observable.just(dup);
    }
    
    @Override
    public Observable<List<UserStocks>> getUserStocks(final long userId) {
        return Observable.just(stocks.stream()
                .filter(it -> it.userId() == userId)
                .toList());
    }
}
