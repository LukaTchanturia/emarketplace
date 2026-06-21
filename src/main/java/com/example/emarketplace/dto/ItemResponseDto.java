package com.example.emarketplace.dto;

import java.time.LocalDateTime;

public class ItemResponseDto {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private LocalDateTime submissionTime;
    private String photoUrl;
    private String username;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getSubmissionTime() { return submissionTime; }
    public void setSubmissionTime(LocalDateTime submissionTime) { this.submissionTime = submissionTime; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}