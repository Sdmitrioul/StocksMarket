package ru.skroba.service;

import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.exceptions.ServiceException;
import ru.skroba.model.CompanyStocks;
import ru.skroba.repository.StocksRepository;
import rx.Observable;

import java.util.List;

import static ru.skroba.model.CompanyStocksFactory.DEFAULT_COUNT_OF_STOCKS;

public final class CompanyStocksService {
    private final StocksRepository repository;
    
    public CompanyStocksService(final StocksRepository repository) {
        this.repository = repository;
    }
    
    public Observable<Success> addCompany(String companyName, double stocksRate) {
        return repository.addCompany(new CompanyStocks(companyName, stocksRate, DEFAULT_COUNT_OF_STOCKS));
    }
    
    public Observable<Double> buyStocks(String companyName, long count) {
        return repository.getCompany(companyName)
                .flatMap(it -> repository.updateCompanyStocks(companyName, prev -> {
                            if (prev.count() < count) {
                                Observable.error(new RepositoryException("Not enough stocks!"));
                            }
                            
                            return prev.buyStocks(count);
                        })
                        .flatMap(res -> res == Success.SUCCESS ? Observable.just(it.price() * count) : Observable.error(
                                new ServiceException("Can't buy stocks!"))));
    }
    
    public Observable<Double> sellStocks(String companyName, long count) {
        return repository.getCompany(companyName)
                .flatMap(it -> repository.updateCompanyStocks(companyName, prev -> prev.sellStocks(count))
                        .flatMap(res -> res == Success.SUCCESS ? Observable.just(it.price() * count) : Observable.error(
                                new ServiceException("Can't sell stocks!"))));
    }
    
    public Observable<CompanyStocks> updateStocksRate(String companyName) {
        return repository.updateCompanyStocks(companyName, CompanyStocks::randomUpdatePrice)
                .flatMap(it -> repository.getCompany(companyName));
    }
    
    public Observable<List<CompanyStocks>> getStocks() {
        return repository.findAll()
                .toList();
    }
}
