package ru.skroba.controllers;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import rx.Observable;

public interface Controller {
    <T> Observable<String> processRequest(HttpServerRequest<T> request) throws ControllerException;
    
    <T> boolean canAccept(HttpServerRequest<T> request);
}
