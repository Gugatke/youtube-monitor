package com.gsg.youtubemonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CountryData {

    private int id;

    private String countryCode;

    private String mostPopularVideoUrl;

    private String mostPopularVideoThumbnailUrl;

    private String mostPopularCommentUrlOnTheVideo;

    private int ownerUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMostPopularVideoUrl() {
        return mostPopularVideoUrl;
    }

    public void setMostPopularVideoUrl(String mostPopularVideoUrl) {
        this.mostPopularVideoUrl = mostPopularVideoUrl;
    }

    public String getMostPopularVideoThumbnailUrl() {
        return mostPopularVideoThumbnailUrl;
    }

    public void setMostPopularVideoThumbnailUrl(String mostPopularVideoThumbnailUrl) {
        this.mostPopularVideoThumbnailUrl = mostPopularVideoThumbnailUrl;
    }

    public String getMostPopularCommentUrlOnTheVideo() {
        return mostPopularCommentUrlOnTheVideo;
    }

    public void setMostPopularCommentUrlOnTheVideo(String mostPopularCommentUrlOnTheVideo) {
        this.mostPopularCommentUrlOnTheVideo = mostPopularCommentUrlOnTheVideo;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}
