package ru.skroba.configuration;

import ru.skroba.client.MarketProvider;
import ru.skroba.controllers.AddMoneyController;
import ru.skroba.controllers.BuyStocksController;
import ru.skroba.controllers.Controller;
import ru.skroba.controllers.GetUserSavingsController;
import ru.skroba.controllers.GetUserStocks;
import ru.skroba.controllers.MasterController;
import ru.skroba.controllers.RegisterUserController;
import ru.skroba.controllers.SellStocksController;
import ru.skroba.repository.Database;
import ru.skroba.repository.stocks.MongoStocksRepository;
import ru.skroba.repository.stocks.StocksRepository;
import ru.skroba.repository.user.MongoUserRepository;
import ru.skroba.repository.user.UserRepository;
import ru.skroba.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserConfiguration {
    private static final String USER_PORT = "USER_PORT";
    private static final String DB_NAME = "USER_DB_NAME";
    private static final String DB_PORT = "USER_DB_PORT";
    private static final String MARKET_HOST = "MARKET_HOST";
    private static final String MARKET_PORT = "MARKET_PORT";
    private final String marketHost;
    private Database database;
    private UserRepository userRepository;
    private StocksRepository stocksRepository;
    private UserService userService;
    private MarketProvider marketProvider;
    
    public UserConfiguration() {
        this.marketHost = System.getenv(MARKET_HOST) + ":" + System.getenv(MARKET_PORT);
    }
    
    public UserConfiguration(String marketHost) {
        this.marketHost = marketHost;
    }
    
    public Controller getController() {
        List<Controller> controllers = new ArrayList<>();
        
        controllers.add(new AddMoneyController(getUserService()));
        controllers.add(new BuyStocksController(getUserService()));
        controllers.add(new GetUserSavingsController(getUserService()));
        controllers.add(new GetUserStocks(getUserService()));
        controllers.add(new RegisterUserController(getUserService()));
        controllers.add(new SellStocksController(getUserService()));
        
        return new MasterController(controllers);
    }
    
    private UserService getUserService() {
        if (userService == null) {
            userService = new UserService(getUserRepository(), getStockRepository(), getMarketProvider());
        }
        
        return userService;
    }
    
    private UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new MongoUserRepository(getDatabase());
        }
        
        return userRepository;
    }
    
    private StocksRepository getStockRepository() {
        if (stocksRepository == null) {
            stocksRepository = new MongoStocksRepository(getDatabase());
        }
        
        return stocksRepository;
    }
    
    private MarketProvider getMarketProvider() {
        if (marketProvider == null) {
            marketProvider = new MarketProvider(marketHost);
        }
        
        return marketProvider;
    }
    
    private Database getDatabase() {
        if (database == null) {
            database = new Database(Integer.parseInt(System.getenv(DB_PORT)), System.getenv(DB_NAME));
        }
        
        return database;
    }
    
    public int getServerPort() {
        return Integer.parseInt(System.getenv(USER_PORT));
    }
}
