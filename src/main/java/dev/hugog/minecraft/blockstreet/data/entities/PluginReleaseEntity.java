package dev.hugog.minecraft.blockstreet.data.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PluginReleaseEntity implements DataEntity {

    public String authorName;
    public String authorUrl;

    public String releaseName;
    public String releaseUrl;
    public Date releaseDate;
    public String releaseVersion;
    public long releaseSize;
    public int releaseDownloadCount;
    public boolean isPreRelease;
    public boolean isDraft;

    public String downloadUrl;

}
