package ru.skroba.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import ru.skroba.service.UserService;

public abstract class BaseUserController extends BaseController {
    protected final UserService userService;
    
    protected final ObjectMapper json;
    
    protected BaseUserController(final String controllerPath, final HttpMethod controllerMethod,
                                 final UserService service) {
        super(controllerPath, controllerMethod);
        this.userService = service;
        this.json = new ObjectMapper();
    }
}
