package ru.skroba.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.model.CompanyStocks;
import ru.skroba.model.CompanyStocksFactory;
import rx.Observable;

import java.util.function.Function;
import java.util.function.Supplier;

import static ru.skroba.model.CompanyStocksFactory.COMPANY_NAME;

public class MongoStocksRepository extends AbstractRepository implements StocksRepository {
    private static final String COLLECTION_NAME = "company_stocks";
    
    public MongoStocksRepository(final Database database) {
        super(COLLECTION_NAME, database);
    }
    
    @Override
    public Observable<Success> addCompany(final CompanyStocks company) {
        return manageCompanyStocks(company.companyName(),
                v -> Observable.error(new RepositoryException("Company already exist!")),
                () -> getCollection().insertOne(CompanyStocksFactory.toDocument(company)));
    }
    
    @Override
    public Observable<CompanyStocks> findAll() {
        return getCollection().find()
                .toObservable()
                .map(CompanyStocksFactory::of);
    }
    
    @Override
    public Observable<CompanyStocks> getCompany(final String companyName) {
        return getCollection().find(Filters.eq(COMPANY_NAME, companyName))
                .toObservable()
                .map(CompanyStocksFactory::of)
                .defaultIfEmpty(null)
                .flatMap(stocks -> stocks == null ? Observable.error(
                        new RepositoryException("No such company!")) : Observable.just(stocks));
    }
    
    @Override
    public Observable<Success> updateCompanyStocks(final String companyName,
                                                   final Function<CompanyStocks, CompanyStocks> updater) {
        return manageCompanyStocks(companyName, prev -> {
            try {
                return getCollection().replaceOne(Filters.eq(COMPANY_NAME, companyName),
                                CompanyStocksFactory.toDocument(updater.apply(prev)))
                        .flatMap(res -> {
                            if (res.getModifiedCount() == 1) {
                                return Observable.just(Success.SUCCESS);
                            }
                            
                            return Observable.error(
                                    new RepositoryException("Can't update stocks of company: " + companyName));
                        });
            } catch (RepositoryException e) {
                return Observable.error(e);
            }
        });
    }
    
    private <T> Observable<T> manageCompanyStocks(String companyName,
                                                  Function<CompanyStocks, Observable<T>> ifPresent) {
        return manageCompanyStocks(companyName, ifPresent,
                () -> Observable.error(new RepositoryException("There's no" + " company with name: " + companyName)));
    }
    
    private <T> Observable<T> manageCompanyStocks(String companyName, Function<CompanyStocks, Observable<T>> ifPresent,
                                                  Supplier<Observable<T>> otherwise) {
        return getCompany(companyName).onErrorReturn(null)
                .flatMap(value -> {
                    if (value == null) {
                        return otherwise.get();
                    }
                    
                    return ifPresent.apply(value);
                });
    }
}
