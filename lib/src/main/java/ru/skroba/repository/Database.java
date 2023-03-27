package ru.skroba.repository;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;

public final class Database {
    private final String url;
    private final String dbName;
    
    private final MongoClient client;
    
    public Database(final int port, final String dbName) {
        this("mongodb://localhost:" + port, dbName);
    }
    
    public Database(final String url, final String dbName) {
        this.url = url;
        this.dbName = dbName;
        this.client = MongoClients.create(this.url);
    }
    
    public Database(final int port, final String host, final String dbName) {
        this("mongodb://" + host + ":" + port, dbName);
    }
    
    public MongoDatabase getDatabase() {
        return client.getDatabase(this.dbName);
    }
}
