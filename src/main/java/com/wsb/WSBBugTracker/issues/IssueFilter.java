package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.people.Person;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.projects.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@NoArgsConstructor
public class IssueFilter {
    State state;
    Project project;
    Person assignee;
    Boolean isEnabled;

    private Specification<Issue> hasState() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("state"), state);
    }

    private Specification<Issue> hasProject() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("project"), project);
    }

    private Specification<Issue> hasAssignee() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("assignee"), assignee);
    }

    private Specification<Issue> isEnabled() {
        return (issueRoot, query, builder) -> builder.equal(issueRoot.get("enabled"), true);
    }

    public Specification<Issue> buildQuery() {
        Specification<Issue> spec = Specification.where(isEnabled());

        if (project != null) {
            spec = spec.and(hasProject());
        }

        if (assignee != null) {
            spec = spec.and(hasAssignee());
        }

        if (state != null) {
            spec = spec.and(hasState());
        }

        return spec;
    }

    public String toQueryString(Integer page, Sort sort) {
        return "page=" + page +
                "&sort=" + toSortString(sort) +
                (state != null ? "&state=" + state : "") +
                (project != null ? "&project=" + project.getId() : "") +
                (assignee != null ? "&assignee=" + assignee.getId() : "");
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
