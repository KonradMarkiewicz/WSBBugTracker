package com.wsb.WSBBugTracker.issues;

import org.springframework.stereotype.Service;

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

}
