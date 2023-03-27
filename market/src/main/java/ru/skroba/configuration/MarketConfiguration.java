package ru.skroba.configuration;

import ru.skroba.controllers.BuyStocksController;
import ru.skroba.controllers.Controller;
import ru.skroba.controllers.CreateCompanyController;
import ru.skroba.controllers.GetAllStocksController;
import ru.skroba.controllers.MasterController;
import ru.skroba.controllers.SellStocksController;
import ru.skroba.controllers.UpdateCompanyStocksController;
import ru.skroba.repository.Database;
import ru.skroba.repository.MongoStocksRepository;
import ru.skroba.repository.StocksRepository;
import ru.skroba.service.CompanyStocksService;

import java.util.ArrayList;
import java.util.List;

public class MarketConfiguration {
    private static final String MARKET_PORT = "MARKET_PORT";
    private static final String DB_PORT = "MARKET_DB_PORT";
    private static final String DB_NAME = "MARKET_DB_NAME";
    
    private Database database;
    private StocksRepository repository;
    
    private CompanyStocksService stocksService;
    
    public int getServerPort() {
        return Integer.parseInt(System.getenv()
                .getOrDefault(MARKET_PORT, "8080"));
    }
    
    public Controller getController() {
        List<Controller> controllers = new ArrayList<>();
        
        controllers.add(new BuyStocksController(getStocksService()));
        controllers.add(new CreateCompanyController(getStocksService()));
        controllers.add(new GetAllStocksController(getStocksService()));
        controllers.add(new SellStocksController(getStocksService()));
        controllers.add(new UpdateCompanyStocksController(getStocksService()));
        
        return new MasterController(controllers);
    }
    
    public CompanyStocksService getStocksService() {
        if (stocksService == null) {
            stocksService = new CompanyStocksService(getStockRepository());
        }
        
        return stocksService;
    }
    
    public StocksRepository getStockRepository() {
        if (repository == null) {
            repository = new MongoStocksRepository(getDatabase());
        }
        
        return repository;
    }
    
    public Database getDatabase() {
        if (database == null) {
            database = new Database(Integer.parseInt(System.getenv()
                    .getOrDefault(DB_PORT, "27017")), "host.docker.internal", System.getenv()
                    .getOrDefault(DB_NAME, "market_dao"));
        }
        
        return database;
    }
}
