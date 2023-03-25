package ru.skroba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.CompanyStocks;
import ru.skroba.service.CompanyStocksService;
import rx.Observable;

import java.util.List;

public class UpdateCompanyStocksController extends BaseStockController {
    private static final String COMPANY = "company";
    
    public UpdateCompanyStocksController(final CompanyStocksService service) {
        super("/stocks/update", HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(COMPANY)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        String companyName = getQueryParam(request, COMPANY);
        
        return service.updateStocksRate(companyName)
                .map(stocks -> new Result(200, stocks))
                .flatMap(res -> {
                    try {
                        return Observable.just(json.writeValueAsString(res));
                    } catch (JsonProcessingException e) {
                        return Observable.error(new ControllerException(500, "Can't parse json!", e));
                    }
                });
    }
    
    private record Result(int code, CompanyStocks stocks) {
    }
}
