package com.wsb.WSBBugTracker.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {

    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${my.admin.username}")
    private String myAdminUsername;

    @Value("${my.admin.password}")
    private String myAdminPassword;

    public PersonService(AuthorityRepository authorityRepository,
                         PersonRepository personRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authorityRepository = authorityRepository;
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void prepareAdminUser() {

        if (personRepository.findByUsername(myAdminUsername) != null) {
            System.out.println("Użytkownik " + myAdminUsername + " już istnieje. Przerywamy tworzenie.");
            return;
        }

        System.out.println("Tworzymy administratora: " + myAdminUsername + "...");

        Person person = new Person(myAdminUsername, myAdminPassword, "Administrator","test@test.pl");

        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        person.setAuthorities(new HashSet<>(authorities));

        savePerson(person);
    }

    protected void savePerson(Person person) {
        String hashedPassword = bCryptPasswordEncoder.encode(person.password);
        person.setPassword(hashedPassword);

        personRepository.save(person);
    }

    protected List<Authority> findAuthorities() {
        return (List<Authority>) authorityRepository.findAll();
    }

    protected void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        person.setEnabled(false);
        personRepository.save(person);
    }

    protected Person editPerson(Long id){
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
    }

    List<Person> findAllUsers() {
        return personRepository.findAll();
    }

    List<Person> findAllEnabledUsers() {
        return (List<Person>) personRepository.findPersonByEnabledIsTrue();
    }
}