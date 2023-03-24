package ru.skroba.repository;

import com.mongodb.rx.client.MongoCollection;
import org.bson.Document;

public abstract class AbstractRepository {
    private final String collectionName;
    private final Database database;
    
    public AbstractRepository(final String collectionName, final Database database) {
        this.collectionName = collectionName;
        this.database = database;
    }
    
    protected MongoCollection<Document> getCollection() {
        return database.getDatabase()
                .getCollection(collectionName);
    }
}
