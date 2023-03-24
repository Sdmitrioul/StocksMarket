package ru.skroba.model.user;

import org.bson.Document;

public class UserFactory {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_MONEY = "user_money";
    
    public static Document toDocument(User user) {
        return new Document().append(USER_ID, user.userId())
                .append(USER_NAME, user.userName())
                .append(USER_MONEY, user.money());
    }
    
    public static User of(Document document) {
        return new User(document.getLong(USER_ID), document.getString(USER_NAME), document.getLong(USER_MONEY));
    }
}
