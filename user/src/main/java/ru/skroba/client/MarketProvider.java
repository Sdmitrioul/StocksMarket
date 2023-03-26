package ru.skroba.client;

import com.mongodb.rx.client.Success;
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
    
    public MarketProvider(final BuyStocksClient buyClient, final SellStocksClient sellClient,
                          final StocksClient stocksClient) {
        this.buyClient = buyClient;
        this.sellClient = sellClient;
        this.stocksClient = stocksClient;
    }
    
    public Observable<Success> buyStocks(String companyName, long count) {
        return buyClient.sendRequest(companyName, count);
    }
    
    public Observable<Success> sellStocks(String companyName, long count) {
        return sellClient.sendRequest(companyName, count);
    }
    
    public Observable<List<CompanyStocks>> getStocks() {
        return stocksClient.sendRequest();
    }
}
