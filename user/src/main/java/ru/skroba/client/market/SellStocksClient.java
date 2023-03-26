package ru.skroba.client.market;

public final class SellStocksClient extends BuySellStocksClient {
    public SellStocksClient(final String host) {
        super(host, "sell");
    }
}
