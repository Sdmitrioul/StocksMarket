package ru.skroba.controllers;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.BaseMessage;
import ru.skroba.service.UserService;
import rx.Observable;

import java.util.List;

public class SellStocksController extends BaseUserController {
    private static final String COMPANY = "company";
    private static final String USER = "uid";
    private static final String CONTROLLER_PATH = "/stocks/sell";
    
    public SellStocksController(final UserService service) {
        super(CONTROLLER_PATH, HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) {
        checkRequestParameters(request, List.of(COMPANY, USER)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        long uid = getLongParam(request, USER);
        String companyName = getQueryParam(request, COMPANY);
        
        return userService.sellUserStocks(uid, companyName)
                .map(it -> new BaseMessage(200, it.toString()).toString());
    }
}
