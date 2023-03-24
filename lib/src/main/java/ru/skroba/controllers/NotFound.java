package ru.skroba.controllers;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import rx.Observable;

public final class NotFound implements Controller {
    @Override
    public <T> Observable<String> processRequest(final HttpServerRequest<T> request) throws ControllerException {
        throw new ControllerException(404, "Not found!");
    }
    
    @Override
    public <T> boolean canAccept(final HttpServerRequest<T> request) {
        return true;
    }
}
