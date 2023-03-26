package ru.skroba.repository.stocks;

import com.mongodb.client.model.Filters;
import ru.skroba.model.stocks.UserStocks;
import ru.skroba.model.stocks.UserStocksFactory;
import ru.skroba.repository.AbstractRepository;
import ru.skroba.repository.Database;
import rx.Observable;

import java.util.List;

public class MongoStocksRepository extends AbstractRepository implements StocksRepository {
    
    private static final String COLLECTION_NAME = "user_stocks";
    
    public MongoStocksRepository(final Database database) {
        super(COLLECTION_NAME, database);
    }
    
    @Override
    public Observable<UserStocks> addStocks(final UserStocks userStocks) {
        var filter = Filters.and(Filters.eq(UserStocksFactory.USER_ID, userStocks.userId()),
                Filters.eq(UserStocksFactory.COMPANY_NAME, userStocks.companyName()));
        return getCollection().find(filter)
                .toObservable()
                .map(UserStocksFactory::of)
                .defaultIfEmpty(null)
                .flatMap(it -> {
                    var inserting = it == null ? userStocks : new UserStocks(it.userId(), it.companyName(),
                            it.amount() + userStocks.amount());
                    return getCollection().insertOne(UserStocksFactory.toDocument(inserting))
                            .map(res -> inserting);
                });
    }
    
    @Override
    public Observable<UserStocks> deleteStocks(final long userId, final String companyName) {
        return getCollection().findOneAndDelete(Filters.and(Filters.eq(UserStocksFactory.USER_ID, userId),
                        Filters.eq(UserStocksFactory.COMPANY_NAME, companyName)))
                .map(UserStocksFactory::of);
    }
    
    @Override
    public Observable<List<UserStocks>> getUserStocks(final long userId) {
        return getCollection().find(Filters.eq(UserStocksFactory.USER_ID, userId))
                .toObservable()
                .map(UserStocksFactory::of)
                .toList();
    }
}
