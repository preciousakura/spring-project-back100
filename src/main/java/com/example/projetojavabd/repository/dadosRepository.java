package com.example.projetojavabd.repository;

import com.example.projetojavabd.model.Estados;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface dadosRepository extends MongoRepository<Estados, String> {
}
