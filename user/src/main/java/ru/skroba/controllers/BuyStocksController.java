package ru.skroba.controllers;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.BaseMessage;
import ru.skroba.service.UserService;
import rx.Observable;

import java.util.List;

public class BuyStocksController extends BaseUserController {
    private static final String COMPANY = "company";
    private static final String USER = "uid";
    private static final String AMOUNT = "amount";
    
    private static final String CONTROLLER_PATH = "/stocks/buy";
    
    public BuyStocksController(final UserService service) {
        super(CONTROLLER_PATH, HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(COMPANY, USER, AMOUNT)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        long uid = getLongParam(request, USER);
        String companyName = getQueryParam(request, COMPANY);
        long amount = getLongParam(request, AMOUNT);
        
        return userService.buyStocks(uid, companyName, amount)
                .map(it -> new BaseMessage(200, it.toString()).toString());
    }
}
