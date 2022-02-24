package com.openhack.repository;

import com.openhack.domain.Person;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.hibernate.engine.spi.Status;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    // put your custom logic here as instance methods

    public Person findByName(String name){
        return find("name", name).firstResult();
    }

    public List<Person> findAlive(){
        return list("status", Status.SAVING);
    }

    public void deleteStefs(){
        delete("name", "Stef");
    }
}