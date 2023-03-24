package ru.skroba.model.stocks;

import org.bson.Document;

public class UserStocksFactory {
    public static final String USER_ID = "user_id";
    public static final String COMPANY_NAME = "company_name";
    public static final String STOCKS = "amount_of_stocks";
    
    public static Document toDocument(final UserStocks userStocks) {
        return new Document().append(USER_ID, userStocks.userId())
                .append(COMPANY_NAME, userStocks.companyName())
                .append(STOCKS, userStocks.amount());
    }
    
    public static UserStocks of(final Document document) {
        return new UserStocks(document.getLong(USER_ID), document.getString(COMPANY_NAME), document.getLong(STOCKS));
    }
}
