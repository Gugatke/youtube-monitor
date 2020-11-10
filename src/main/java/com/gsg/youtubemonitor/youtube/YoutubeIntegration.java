package com.gsg.youtubemonitor.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.gsg.youtubemonitor.model.CountryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class YoutubeIntegration {

    private static final String YOUTUBE_VIDEO_URL_PREFIX = "https://www.youtube.com/watch?v=";

    private static final Logger log = LoggerFactory.getLogger(YoutubeIntegration.class);

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final String API_KEY;

    private final String APPLICATION_NAME;

    public YoutubeIntegration(@Value("${com.gsg.youtube.apikey}") String API_KEY,
                              @Value("${com.gsg.youtube.application.name}") String APPLICATION_NAME) {
        this.API_KEY = API_KEY;
        this.APPLICATION_NAME = APPLICATION_NAME;
    }

    public CountryData getUserYoutubeData(String countryCode) {
        VideoListResponse videoList = getVideoList(countryCode);
        String mostPopularVideoId = getMostPopularVideoId(videoList);
        String mostPopularVideoThumbnailUrl = getVideoThumbnail(videoList);
        String mostPopularVideoUrl = YOUTUBE_VIDEO_URL_PREFIX + mostPopularVideoId;
        String mostPopularCommentOnTheVideo = getMostPopularCommentIdForVideo(mostPopularVideoId);
        String mostPopularCommentOnTheVideoUrl = mostPopularVideoUrl + "&lc=" + mostPopularCommentOnTheVideo;
        if (mostPopularCommentOnTheVideo == null) {
            mostPopularCommentOnTheVideoUrl = null;
        }
        CountryData countryData = CountryData.builder()
                .countryCode(countryCode)
                .mostPopularVideoUrl(mostPopularVideoUrl)
                .mostPopularVideoThumbnailUrl(mostPopularVideoThumbnailUrl)
                .mostPopularCommentUrlOnTheVideo(mostPopularCommentOnTheVideoUrl)
                .build();
        log.info("Obtained CountryData[{}] from youtube", countryData);
        return countryData;
    }

    private String getMostPopularVideoId(VideoListResponse videoList) {
        try {
            return videoList.getItems().get(0).getId();
        } catch (Exception e) {
            String errorMessage = String.format("Error occurred parsing videoId from videoList[%s]", videoList.toString());
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    private String getVideoThumbnail(VideoListResponse videoList) {
        List<Video> items = videoList.getItems();
        if (items != null && items.size() > 0) {
            VideoSnippet snippet = items.get(0).getSnippet();
            if (snippet != null && snippet.getThumbnails() != null) {
                if (snippet.getThumbnails().getMaxres() != null) {
                    return snippet.getThumbnails().getMaxres().getUrl();
                } else {
                    Thumbnail defaultThumbnail = snippet.getThumbnails().getDefault();
                    return defaultThumbnail != null ? defaultThumbnail.getUrl() : null;
                }
            }
        }
        return null;
    }

    private VideoListResponse getVideoList(String countryCode) {
        try {
            YouTube youtubeService = getService();
            return youtubeService.videos()
                    .list("snippet,contentDetails,statistics")
                    .setKey(API_KEY)
                    .setChart("mostPopular")
                    .setRegionCode(countryCode)
                    .execute();
        } catch (Exception e) {
            String errorMessage = String.format("Error occurred getting most popular video for [%s]", countryCode);
            log.info(errorMessage, e);
            throw new IllegalStateException(errorMessage);
        }
    }

    private String getMostPopularCommentIdForVideo(String videoId) {
        try {
            YouTube service = getService();
            CommentThreadListResponse response = service.commentThreads()
                    .list("snippet,replies")
                    .setKey(API_KEY)
                    .setOrder("relevance")
                    .setVideoId(videoId)
                    .execute();
            List<CommentThread> items = response.getItems();
            if (items == null || items.size() == 0) {
                log.info("No comment found for video[{}]", videoId);
                return null;
            }
            return items.get(0).getId();
        } catch (GeneralSecurityException | IOException e) {
            log.info("Error occurred getting most popular comment for video[{}]", videoId, e);
            return null;
        }
    }


    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}