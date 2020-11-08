package com.gsg.youtubemonitor.youtube;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeData {

    private String mostPopularVideoUrl;

    private String mostPopularVideoThumbnailUrl;

    private String mostPopularCommentUrlOnTheVideo;
}
