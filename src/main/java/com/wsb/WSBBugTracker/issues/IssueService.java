package com.wsb.WSBBugTracker.issues;

import com.wsb.WSBBugTracker.mails.Mail;
import com.wsb.WSBBugTracker.mails.MailService;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final MailService mailService;

    public IssueService(IssueRepository issueRepository, MailService mailService) {
        this.issueRepository = issueRepository;
        this.mailService = mailService;
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

    protected Issue editIssue(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe Id zgłoszenia: " + id));
    }

    public void sendNewIssueAssignedMail(Issue issue) {
        Mail mail = new Mail();
        var recipient = issue.getAssignee();
        if (recipient != null) {
            mail.setRecipient(recipient.getEmail());
            mail.setSubject("Zostało przypisane nowe zgłoszenie");
            mail.setContent("Tytuł przypisanego zadania: " + issue.getTitle() +
                    "\nProjekt przypisanego zadania: " + issue.getProject().getName() +
                    "\nTreść zadania: " + issue.getContent());
            mailService.send(mail);
            System.out.println("Wysyłam powiadomienie o przypisaniu do zgłoszenia na adres " + recipient.getEmail());
        } else
            System.out.println("Przypisany do zgłoszenia użytkownik nie posiada adresu email, nie wysyłam wiadomości");
    }

}
