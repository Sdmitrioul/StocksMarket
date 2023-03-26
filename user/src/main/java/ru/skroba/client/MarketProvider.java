package ru.skroba.client;

import com.mongodb.rx.client.Success;
import ru.skroba.model.stocks.CompanyStocks;
import rx.Observable;

import java.util.List;

public class MarketProvider {
    public Observable<Success> buyStocks(String companyName, long count) {
        //TODO
        return null;
    }
    
    public Observable<Success> sellStocks(String companyName, long count) {
        //TODO
        return null;
    }
    
    public Observable<List<CompanyStocks>> getStocks() {
        //TODO
        return null;
    }
}
