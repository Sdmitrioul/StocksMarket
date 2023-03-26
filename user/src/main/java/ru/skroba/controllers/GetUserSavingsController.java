package ru.skroba.controllers;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.BaseMessage;
import ru.skroba.service.UserService;
import rx.Observable;

import java.util.List;

public class GetUserSavingsController extends BaseUserController {
    
    private static final String CONTROLLER_PATH = "/user/savings";
    private static final String USER = "uid";
    
    public GetUserSavingsController(final UserService service) {
        super(CONTROLLER_PATH, HttpMethod.GET, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(USER)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        long uid = getLongParam(request, USER);
        
        return userService.getUserAmountOfSavings(uid)
                .map(res -> new BaseMessage(200, res.toString()).toString());
    }
}
