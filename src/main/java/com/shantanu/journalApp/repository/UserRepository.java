package com.shantanu.journalApp.repository;

import com.shantanu.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName); //abstract method declared in this Interface

    void deleteByUserName(String userName);
}
