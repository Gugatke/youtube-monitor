package com.gsg.youtubemonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private int id;

    private String username;

    private String passwordHash;

    private String countryCode;

    private int jobRunMinute;

    private LocalDateTime nextJobRunTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getJobRunMinute() {
        return jobRunMinute;
    }

    public void setJobRunMinute(int jobRunMinute) {
        this.jobRunMinute = jobRunMinute;
    }

    public LocalDateTime getNextJobRunTime() {
        return nextJobRunTime;
    }

    public void setNextJobRunTime(LocalDateTime nextJobRunTime) {
        this.nextJobRunTime = nextJobRunTime;
    }
}
