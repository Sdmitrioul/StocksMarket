package ru.skroba.client.market;

public final class BuyStocksClient extends BuySellStocksClient {
    public BuyStocksClient(final String host) {
        super(host, "buy");
    }
}
