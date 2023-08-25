package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PluginReleaseEntity implements DataEntity {

    private String authorName;
    private String authorUrl;

    private String releaseName;
    private String releaseUrl;
    private Date releaseDate;
    private String releaseVersion;
    private long releaseSize;
    private int releaseDownloadCount;
    private boolean isPreRelease;
    private boolean isDraft;

    private String downloadUrl;

}
