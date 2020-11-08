package com.gsg.youtubemonitor.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.gsg.youtubemonitor.model.CountryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class YoutubeIntegration {

    private static final String  YOUTUBE_VIDEO_URL_PREFIX = "https://www.youtube.com/watch?v=";

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
        String mostPopularCommentOnTheVideo = getMostPopularCommentIdForVideo(mostPopularVideoId);
        String mostPopularVideoUrl = YOUTUBE_VIDEO_URL_PREFIX + mostPopularVideoId;
        String mostPopularCommentOnTheVideoUrl = mostPopularVideoUrl + "&lc=" + mostPopularCommentOnTheVideo;
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
        try {
            return videoList.getItems().get(0).getSnippet().getThumbnails().getMaxres().getUrl();
        } catch (Exception e) {
            log.error("Error occurred parsing thumbnail url from videoList[{}]", videoList);
            return null;
        }
    }

    private VideoListResponse getVideoList(String countryCode) {
        try {
            YouTube youtubeService = getService();
            YouTube.Videos.List request = youtubeService.videos()
                    .list("snippet,contentDetails,statistics")
                    .setKey(API_KEY);

            return request.setChart("mostPopular")
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
            YouTube.CommentThreads.List request = service.commentThreads()
                    .list("snippet,replies");
            CommentThreadListResponse response = request.setKey(API_KEY)
                                                        .setOrder("relevance")
                                                        .setVideoId(videoId)
                                                        .execute();
            return response.getItems().get(0).getId();
        } catch (Exception e) {
            log.info("Error occurred getting most popular comment for video[{}]", videoId, e);
            throw new IllegalStateException("Error occurred getting most popular comment for video:" + videoId);
        }
    }


    private YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}