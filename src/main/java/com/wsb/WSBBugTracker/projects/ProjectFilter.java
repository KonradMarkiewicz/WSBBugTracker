package com.wsb.WSBBugTracker.projects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@NoArgsConstructor
public class ProjectFilter {
    Boolean isEnabled;
    String globalSearch;

    private Specification<Project> isEnabled() {
        return (projectRoot, query, builder) -> builder.equal(projectRoot.get("enabled"), true);
    }

    private Specification<Project> globalSearching() {

        Specification<Project> hasName = (projectRoot, query, builder) -> builder.like(builder.lower(projectRoot.get("name")), "%" + globalSearch.toLowerCase() + "%");
        Specification<Project> hasContent= (projectRoot, query, builder) -> builder.like(builder.lower(projectRoot.get("content")), "%" + globalSearch.toLowerCase() + "%");

        return hasName.or(hasContent);
    }

    public Specification<Project> buildQuery() {
        Specification<Project> spec = Specification.where(isEnabled());

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
