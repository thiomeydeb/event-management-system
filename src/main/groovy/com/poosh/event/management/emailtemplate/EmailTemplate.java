package com.poosh.event.management.emailtemplate;

import javax.persistence.*;

@Entity(name = "EmailTemplate")
@Table(name = "email_template")
public class EmailTemplate {
    @SequenceGenerator(
            name = "email_template_sequence",
            sequenceName = "email_template_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "email_template_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "type",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private String type;

    @Column(
            name = "subject",
            columnDefinition = "VARCHAR(50)"
    )
    private String subject;

    @Column(
            name = "message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    public EmailTemplate() {
    }

    public EmailTemplate(String type, String subject, String message) {
        this.type = type;
        this.subject = subject;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
