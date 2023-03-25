package ru.skroba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.service.CompanyStocksService;
import rx.Observable;

import java.util.List;

public class GetAllStocksController extends BaseStockController {
    public GetAllStocksController(final CompanyStocksService service) {
        super("/", HttpMethod.GET, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        return service.getStocks()
                .map(stocks -> new Result<>(200, stocks))
                .flatMap(res -> {
                    try {
                        return Observable.just(json.writeValueAsString(res));
                    } catch (JsonProcessingException e) {
                        return  Observable.error(new ControllerException(500, "Can't parse json!", e));
                    }
                });
    }
    
    private record Result<T>(int code, List<T> stocks) {
    }
}
