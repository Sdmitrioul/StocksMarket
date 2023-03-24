package ru.skroba.controllers;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.skroba.exceptions.ControllerException;
import ru.skroba.model.BaseMessage;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public final class MasterController implements Controller {
    private final List<Controller> controllers;
    
    public MasterController(final List<Controller> controllers) {
        this.controllers = new ArrayList<>(controllers);
        this.controllers.add(new NotFound());
    }
    
    @Override
    public <T> Observable<String> processRequest(final HttpServerRequest<T> request) throws ControllerException {
        try {
            return controllers.stream()
                    .filter(controller -> controller.canAccept(request))
                    .findAny()
                    .map(controller -> controller.processRequest(request))
                    .orElse(Observable.just(new BaseMessage(500, "Server error!").toString()));
        } catch (ControllerException e) {
            return Observable.just(new BaseMessage(e.getCode(), e.getMessage()).toString());
        }
    }
    
    @Override
    public <T> boolean canAccept(final HttpServerRequest<T> request) {
        return true;
    }
}
