package com.wsb.WSBBugTracker.people;

import com.wsb.WSBBugTracker.auth.Authority;
import com.wsb.WSBBugTracker.auth.AuthorityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id użytkownika: " + id));
        person.setEnabled(false);
        personRepository.save(person);
    }

    protected Person editPerson(Long id){
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id użytkownika: " + id));
    }

    public Page<Person> findPaginated(Pageable pageable) {

        List<Person> people = personRepository.findPersonByEnabledIsTrue();
        List<Person> list;

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (people.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, people.size());
            list = people.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), people.size());
    }
}