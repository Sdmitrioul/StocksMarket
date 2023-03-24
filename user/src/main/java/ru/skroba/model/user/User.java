package ru.skroba.model.user;

public record User(long userId, String userName, double money) {
    public User addMoney(double amount) {
        return new User(userId, userName, money() + amount);
    }
    
    public User reduceMoney(double amount) {
        return new User(userId, userName, money() - amount);
    }
}
