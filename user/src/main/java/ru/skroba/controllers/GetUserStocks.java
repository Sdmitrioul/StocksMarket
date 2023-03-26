package ru.skroba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.stocks.StocksWithCost;
import ru.skroba.service.UserService;
import rx.Observable;

import java.util.List;

public class GetUserStocks extends BaseUserController {
    
    private static final String CONTROLLER_PATH = "/user/stocks";
    private static final String USER = "uid";
    
    protected GetUserStocks(final UserService service) {
        super(CONTROLLER_PATH, HttpMethod.GET, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(USER)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
    
        long uid = getLongParam(request, USER);
        
        return userService.getUserStocksWithCost(uid)
                .map(stocks -> new Result(200, stocks))
                .flatMap(res -> {
                    try {
                        return Observable.just(json.writeValueAsString(res));
                    } catch (JsonProcessingException e) {
                        return Observable.error(new ControllerException(500, "Can't parse json!", e));
                    }
                });
    }
    
    private record Result(int code, List<StocksWithCost> userStocks) {
    }
}
