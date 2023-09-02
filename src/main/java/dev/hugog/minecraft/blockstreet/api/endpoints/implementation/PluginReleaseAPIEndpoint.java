package dev.hugog.minecraft.blockstreet.api.endpoints.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.hugog.minecraft.blockstreet.api.endpoints.APIEndpointImpl;
import dev.hugog.minecraft.blockstreet.api.entities.PluginReleaseEntity;
import lombok.extern.log4j.Log4j2;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class PluginReleaseAPIEndpoint extends APIEndpointImpl<PluginReleaseEntity> {

    public PluginReleaseAPIEndpoint() {
        super("https://api.github.com/repos/Hugo1307/BlockStreet/releases/latest");
    }

    @Override
    public PluginReleaseEntity parseData() throws IOException {

        if (response == null) {
            log.warn("Error while executing request to API endpoint: " + this.endpointUrl + " - Response is null");
            return null;
        }

        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);

        if (response.isSuccessful()) {

            try {

                String apiResponseBody = responseBody.string();
                JsonObject jsonObject = new Gson().fromJson(apiResponseBody, JsonObject.class);

                if (jsonObject == null)
                    return null;

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                String authorName = jsonObject.get("author").getAsJsonObject().get("login").getAsString();
                String authorUrl = jsonObject.get("author").getAsJsonObject().get("html_url").getAsString();

                String releaseName = jsonObject.get("name").getAsString();
                String releaseUrl = jsonObject.get("html_url").getAsString();
                Date releaseDate = inputFormat.parse(jsonObject.get("published_at").getAsString());
                String releaseVersion = jsonObject.get("tag_name").getAsString();
                long releaseSize = jsonObject.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("size").getAsLong();
                int releaseDownloads = jsonObject.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("download_count").getAsInt();
                boolean isPreRelease = jsonObject.get("prerelease").getAsBoolean();
                boolean isDraft = jsonObject.get("draft").getAsBoolean();
                String downloadURL = jsonObject.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();

                return PluginReleaseEntity.builder()
                        .authorName(authorName)
                        .authorUrl(authorUrl)
                        .releaseName(releaseName)
                        .releaseUrl(releaseUrl)
                        .releaseDate(releaseDate)
                        .releaseVersion(releaseVersion)
                        .releaseSize(releaseSize)
                        .releaseDownloadCount(releaseDownloads)
                        .isPreRelease(isPreRelease)
                        .isDraft(isDraft)
                        .downloadUrl(downloadURL)
                        .build();

            } catch (IOException | ParseException e) {
                log.error("Error while parsing API response body", e);
            } finally {
                responseBody.close();
            }

        } else {
            log.warn("Error while executing request to API endpoint: " + this.endpointUrl + " - Response code: " + response.code());
        }

        response.close();

        return null;

    }

}
