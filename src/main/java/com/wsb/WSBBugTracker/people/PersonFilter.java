package com.wsb.WSBBugTracker.people;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@NoArgsConstructor
public class PersonFilter {
    Boolean isEnabled;
    String globalSearch;

    private Specification<Person> isEnabled() {
        return (personRoot, query, builder) -> builder.equal(personRoot.get("enabled"), true);
    }

    private Specification<Person> globalSearching() {

        Specification<Person> hasUsername = (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("username")), "%" + globalSearch.toLowerCase() + "%");
        Specification<Person> hasName= (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("name")), "%" + globalSearch.toLowerCase() + "%");
        Specification<Person> hasEmail= (personRoot, query, builder) -> builder.like(builder.lower(personRoot.get("email")), "%" + globalSearch.toLowerCase() + "%");

        return hasUsername.or(hasName).or(hasEmail);
    }

    public Specification<Person> buildQuery() {
        Specification<Person> spec = Specification.where(isEnabled());

        if (globalSearch != null) {
            spec = spec.and(globalSearching());
        }

        return spec;
    }

    public String toQueryString(Integer page, Sort sort) {
        return "page=" + page +
                "&sort=" + toSortString(sort) +
                (globalSearch != null ? "&globalSearch=" + globalSearch : "");
    }

    public String toSortString(Sort sort) {
        Sort.Order order = sort.stream().findFirst().orElse(null);

        String sortString = "";
        if (order != null) {
            sortString += order.getProperty() + "," + order.getDirection();
        }

        return sortString;
    }

    public Sort findNextSorting(Sort currentSorting, String property) {
        Sort.Direction currentDirection = currentSorting.getOrderFor(property) != null ? currentSorting.getOrderFor(property).getDirection() : null;

        if (currentDirection == null) {
            return Sort.by(property).ascending();
        } else if (currentDirection.isAscending()) {
            return Sort.by(property).descending();
        } else {
            return Sort.unsorted();
        }
    }
}
