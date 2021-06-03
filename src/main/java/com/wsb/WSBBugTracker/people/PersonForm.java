package com.wsb.WSBBugTracker.people;

import com.wsb.WSBBugTracker.auth.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PersonForm {

    Long id;

    @NotBlank
    @Size(min = 5, max = 255)
    String username;

    @NotBlank
    @Size(min = 5, max = 255)
    String name;

    @NotBlank
    String email;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "person_authorities",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    Set<Authority> authorities;

    PersonForm(Person person) {
        this.id = person.id;
        this.username = person.username;
        this.name = person.name;
        this.email = person.email;
        this.authorities = person.getAuthorities();
    }

}
