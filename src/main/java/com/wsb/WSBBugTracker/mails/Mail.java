package com.wsb.WSBBugTracker.mails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mail {

    String recipient;
    String subject;
    String content;
}
