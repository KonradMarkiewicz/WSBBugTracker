package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.people.Person;
import com.wsb.WSBBugTracker.enums.State;
import com.wsb.WSBBugTracker.projects.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

}
