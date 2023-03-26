package ru.skroba.client;

import ru.skroba.client.market.BuyStocksClient;
import ru.skroba.client.market.SellStocksClient;
import ru.skroba.client.market.StocksClient;
import ru.skroba.model.stocks.CompanyStocks;
import rx.Observable;

import java.util.List;

public class MarketProvider {
    private final BuyStocksClient buyClient;
    private final SellStocksClient sellClient;
    private final StocksClient stocksClient;
    
    public MarketProvider(final String host) {
        this.buyClient = new BuyStocksClient(host);
        this.sellClient = new SellStocksClient(host);
        this.stocksClient = new StocksClient(host);
    }
    
    public Observable<Double> buyStocks(String companyName, long count) {
        return buyClient.sendRequest(companyName, count);
    }
    
    public Observable<Double> sellStocks(String companyName, long count) {
        return sellClient.sendRequest(companyName, count);
    }
    
    public Observable<List<CompanyStocks>> getStocks() {
        return stocksClient.sendRequest();
    }
}
