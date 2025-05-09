package TurismLab.controller;

import TurismLab.service.Service;

public class UserController {
    private Service service;


    public UserController(Service service) {
        this.service = service;
    }
}
