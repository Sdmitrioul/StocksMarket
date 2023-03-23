package ru.skroba.service;

import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.exceptions.ServiceException;
import ru.skroba.model.stocks.CompanyStocks;
import ru.skroba.repository.StocksRepository;
import rx.Observable;

import java.util.List;

import static ru.skroba.model.stocks.CompanyStocksFactory.DEFAULT_COUNT_OF_STOCKS;

public final class CompanyStocksService {
    private final StocksRepository repository;
    
    public CompanyStocksService(final StocksRepository repository) {
        this.repository = repository;
    }
    
    public Observable<Success> addCompany(String companyName, double stocksRate) throws ServiceException {
        try {
            return repository.addCompany(new CompanyStocks(companyName, stocksRate, DEFAULT_COUNT_OF_STOCKS));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public Observable<Success> buyStocks(String companyName, long count) throws ServiceException {
        try {
            return repository.updateCompanyStocks(companyName, prev -> {
                if (prev.count() < count) {
                    throw new RepositoryException("Not enough stocks!");
                }
                
                return prev.buyStocks(count);
            });
        } catch (RepositoryException e) {
            throw new ServiceException(
                    "Can't buy %s stocks of company %s!\n".formatted(count, companyName) + e.getMessage(), e);
        }
    }
    
    public Observable<Success> sellStocks(String companyName, long count) throws ServiceException {
        try {
            return repository.updateCompanyStocks(companyName, prev -> prev.sellStocks(count));
        } catch (RepositoryException e) {
            throw new ServiceException(
                    "Can't sell %s stocks of company %s!\n".formatted(count, companyName) + e.getMessage(), e);
        }
    }
    
    public Observable<Success> updateStocksRate(String companyName) throws ServiceException {
        try {
            return repository.updateCompanyStocks(companyName, CompanyStocks::randomUpdatePrice);
        } catch (RepositoryException e) {
            throw new ServiceException("Can't update stocks of company %s!\n".formatted(companyName) + e.getMessage(),
                    e);
        }
    }
    
    public Observable<List<CompanyStocks>> getStocks() {
        return repository.findAll()
                .toList();
    }
}
