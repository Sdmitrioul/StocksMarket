package ru.skroba.repository;

import com.mongodb.rx.client.Success;
import ru.skroba.model.stocks.CompanyStocks;
import rx.Observable;

import java.util.function.Function;

public interface StocksRepository {
    Observable<Success> addCompany(CompanyStocks company);
    
    Observable<CompanyStocks> findAll();
    
    Observable<CompanyStocks> getCompany(String companyName);
    
    Observable<Success> updateCompanyStocks(final String companyName, Function<CompanyStocks, CompanyStocks> updater);
}
