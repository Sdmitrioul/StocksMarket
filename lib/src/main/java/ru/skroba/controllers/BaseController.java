package ru.skroba.controllers;

import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import rx.Observable;

import java.util.List;
import java.util.Optional;

public abstract class BaseController implements Controller {
    private final String controllerPath;
    
    private final HttpMethod controllerMethod;
    
    public BaseController(final String controllerPath, final HttpMethod controllerMethod) {
        this.controllerPath = controllerPath;
        this.controllerMethod = controllerMethod;
    }
    
    protected static <T> Optional<String> checkRequestParameters(HttpServerRequest<T> request,
                                                                 List<String> requiredParameters) {
        return requiredParameters.stream()
                .filter(key -> !request.getQueryParameters()
                        .containsKey(key))
                .reduce((s1, s2) -> s1 + ", " + s2)
                .map(s -> s + " parameters not found");
    }
    
    protected static <T> long getLongParam(HttpServerRequest<T> request, String param) {
        return Long.parseLong(getQueryParam(request, param));
    }
    
    protected static <T> String getQueryParam(HttpServerRequest<T> request, String param) {
        return request.getQueryParameters()
                .get(param)
                .get(0);
    }
    
    protected static <T> double getDoubleParam(HttpServerRequest<T> request, String param) {
        return Double.parseDouble(getQueryParam(request, param));
    }
    
    @Override
    public <T> Observable<String> processRequest(final HttpServerRequest<T> request) throws ControllerException {
        if (!canAccept(request)) {
            throw new ControllerException(500, "Can't process request!");
        }
        
        return process(request);
    }
    
    @Override
    public <T> boolean canAccept(final HttpServerRequest<T> request) {
        return request.getDecodedPath()
                .equals(controllerPath) && request.getHttpMethod()
                .equals(controllerMethod);
    }
    
    protected abstract <T> Observable<String> process(final HttpServerRequest<T> request) throws ControllerException;
}
