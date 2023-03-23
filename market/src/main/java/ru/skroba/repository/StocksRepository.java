package ru.skroba.repository;

import com.mongodb.rx.client.Success;
import ru.skroba.model.stocks.CompanyStocks;
import rx.Observable;

import java.util.function.Function;

public interface StocksRepository {
    Observable<Boolean> addCompany(CompanyStocks company);
    
    Observable<CompanyStocks> findAll();
    
    Observable<CompanyStocks> getCompany(String companyName);
    
    Observable<Success> updateCompanyStocks(Function<CompanyStocks, CompanyStocks> updater, final String companyName);
}
