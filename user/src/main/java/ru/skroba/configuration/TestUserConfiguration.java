package ru.skroba.configuration;

import ru.skroba.repository.stocks.LocalStocksRepository;
import ru.skroba.repository.stocks.StocksRepository;
import ru.skroba.repository.user.LocalUserRepository;
import ru.skroba.repository.user.UserRepository;

public class TestUserConfiguration extends UserConfiguration {
    public TestUserConfiguration() {
        super();
    }
    
    @Override
    public UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new LocalUserRepository();
        }
        
        return userRepository;
    }
    
    @Override
    public StocksRepository getStockRepository() {
        if (stocksRepository == null) {
            stocksRepository = new LocalStocksRepository();
        }
        return stocksRepository;
    }
}
