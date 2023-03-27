package ru.skroba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.user.User;
import ru.skroba.service.UserService;
import rx.Observable;

import java.util.List;

public class RegisterUserController extends BaseUserController {
    private final static String USER = "user";
    private final static String BALANCE = "balance";
    
    private static final String CONTROLLER_PATH = "/user/register";
    
    public RegisterUserController(final UserService service) {
        super(CONTROLLER_PATH, HttpMethod.POST, service);
    }
    
    @Override
    protected <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException {
        checkRequestParameters(request, List.of(USER, BALANCE)).ifPresent(res -> {
            throw new ControllerException(400, res);
        });
        
        double balance = getDoubleParam(request, BALANCE);
        String userName = getQueryParam(request, USER);
        
        return userService.addUser(userName, balance)
                .map(user -> new Result(200, user))
                .flatMap(res -> {
                    try {
                        return Observable.just(json.writeValueAsString(res));
                    } catch (JsonProcessingException e) {
                        return Observable.error(new ControllerException(500, "Can't parse json!", e));
                    }
                });
    }
    
    private record Result(int code, User user) {
    }
}
