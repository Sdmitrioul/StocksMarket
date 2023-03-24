package ru.skroba.model;

import org.bson.Document;

public final class CompanyStocksFactory {
    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_STOCK_PRICE = "stock_price";
    public static final String COMPANY_STOCKS_COUNT = "stock_count";
    
    public static final long DEFAULT_COUNT_OF_STOCKS = 100;
    
    public static CompanyStocks of(Document document) {
        return new CompanyStocks(document.getString(COMPANY_NAME), document.getDouble(COMPANY_STOCK_PRICE),
                document.getLong(COMPANY_STOCKS_COUNT));
    }
    
    public static Document toDocument(CompanyStocks companyStocks) {
        return new Document().append(COMPANY_NAME, companyStocks.companyName())
                .append(COMPANY_STOCK_PRICE, companyStocks.price())
                .append(COMPANY_STOCKS_COUNT, companyStocks.count());
    }
}