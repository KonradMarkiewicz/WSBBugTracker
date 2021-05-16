package com.wsb.WSBBugTracker.issues;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    protected void saveIssue(Issue issue) {
        issueRepository.save(issue);
    }

    protected void deleteIssue(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id zgłoszenia: " + id));
        issue.setEnabled(false);
        issueRepository.save(issue);
    }

    protected Issue editIssue (Long id){
        return issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id zgłoszenia: " + id));
    }

    public Page<Issue> findPaginated(List<Issue> issues, Pageable pageable) {

        List<Issue> list;

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (issues.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, issues.size());
            list = issues.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), issues.size());
    }
}
