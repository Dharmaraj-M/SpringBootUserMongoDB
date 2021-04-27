package com.example.springbootusermongodb.repository;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MongoTemplate {
    public UserRepository(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }
}
