package ru.skroba.repository.stocks;

import ru.skroba.model.stocks.UserStocks;
import rx.Observable;

import java.util.List;

public interface StocksRepository {
    Observable<UserStocks> addStocks(UserStocks userStocks);
    
    Observable<UserStocks> deleteStocks(long userId, String companyName);
    
    Observable<List<UserStocks>> getUserStocks(long userId);
    
    Observable<List<UserStocks>> getUserStocks(long userId, String companyName);
}
