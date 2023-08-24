package dev.hugog.minecraft.blockstreet.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.hugog.minecraft.blockstreet.Main;
import dev.hugog.minecraft.blockstreet.utils.httpclient.HTTPRequest;
import dev.hugog.minecraft.blockstreet.utils.httpclient.HTTPRequestType;
import lombok.Getter;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

@Singleton
@Getter
public class UpdateChecker {

	private final Main plugin;

	private boolean newVersionAvailable;
	private ComparableVersion currentVersion;
	private ComparableVersion lastVersion;

	@Inject
	public UpdateChecker(Main plugin) {
		this.plugin = plugin;
	}

	public String getRequestBody() {
		return new HTTPRequest.Builder(HTTPRequestType.GET, "https://api.github.com/repos/Hugo1307/BlockStreet/releases/latest").build()
				.sendRequest().responseAsString();
	}

	public boolean checkForUpdates() {

		String jsonString = getRequestBody();

		if (jsonString == null) {
			Main.getInstance().getLogger().warning("Unable to check for new updates...");
			return false;
		}

		JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);

		if (jsonObject == null)
			return false;

		lastVersion = new ComparableVersion(jsonObject.get("tag_name").getAsString());
		currentVersion = new ComparableVersion(plugin.getDescription().getVersion());

		newVersionAvailable = currentVersion.compareTo(lastVersion) < 0;
		return newVersionAvailable;
		
	}

}
