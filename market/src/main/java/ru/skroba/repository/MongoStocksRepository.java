package ru.skroba.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.Success;
import ru.skroba.exceptions.RepositoryException;
import ru.skroba.model.stocks.CompanyStocks;
import ru.skroba.model.stocks.CompanyStocksFactory;
import rx.Observable;

import java.util.function.Function;
import java.util.function.Supplier;

import static ru.skroba.model.stocks.CompanyStocksFactory.COMPANY_NAME;

public class MongoStocksRepository extends AbstractRepository implements StocksRepository {
    private static final String COLLECTION_NAME = "company_stocks";
    
    public MongoStocksRepository(final Database database) {
        super(COLLECTION_NAME, database);
    }
    
    @Override
    public Observable<Boolean> addCompany(final CompanyStocks company) {
        return manageCompanyStocks(company.companyName(),
                v -> Observable.error(new RepositoryException("Company already exist!")),
                () -> getCollection().insertOne(CompanyStocksFactory.toDocument(company))
                        .map(s -> s.equals(Success.SUCCESS)));
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
                .map(CompanyStocksFactory::of);
    }
    
    @Override
    public Observable<Success> updateCompanyStocks(final Function<CompanyStocks, CompanyStocks> updater,
                                                   final String companyName) {
        return manageCompanyStocks(companyName,
                prev -> getCollection().replaceOne(Filters.eq(COMPANY_NAME, companyName),
                                CompanyStocksFactory.toDocument(updater.apply(prev)))
                        .map(res -> {
                            if (res.getModifiedCount() == 1) {
                                return Success.SUCCESS;
                            }
                            
                            throw new RepositoryException("Can't update stocks of company: " + companyName);
                        }));
    }
    
    private <T> Observable<T> manageCompanyStocks(String companyName,
                                                  Function<CompanyStocks, Observable<T>> ifPresent) {
        return manageCompanyStocks(companyName, ifPresent,
                () -> Observable.error(new RepositoryException("There's no" + " company with name: " + companyName)));
    }
    
    private <T> Observable<T> manageCompanyStocks(String companyName, Function<CompanyStocks, Observable<T>> ifPresent,
                                                  Supplier<Observable<T>> otherwise) {
        return getCompany(companyName).defaultIfEmpty(null)
                .flatMap(value -> {
                    if (value == null) {
                        return otherwise.get();
                    }
                    
                    return ifPresent.apply(value);
                });
    }
}
