package hugog.blockstreet.update;

import hugog.blockstreet.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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

public abstract class AutoUpdate {

	private static boolean newVersionAvailable = false;
	private static String currentVersion;
	private static String lastVersion;

	private static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.setConnectTimeout(4000);
            
            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }catch (UnknownHostException ex) {
            System.out.println("[BlockStreet] No internet connection found");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

	
	public static void checkForUpdates() throws ParseException {
		
		String jsonString = getJSON("https://api.github.com/repos/Hugo1307/BlockStreet/releases/latest");
		JSONParser parser = new JSONParser();
		JSONObject root;
		
		if (jsonString == null) {
			System.out.println("[BlockStreet] Unable to search for new versions.");
			return;
		}

		root = (JSONObject) parser.parse(jsonString);
		
		lastVersion = (String) root.get("tag_name");
		currentVersion = Main.getInstance().getDescription().getVersion();
		
		String lastVersionNoDots = ((String) root.get("tag_name")).replaceAll("[a-z-.]", "");
		String currentVersionNoDots = Main.getInstance().getDescription().getVersion().replaceAll("[a-z-.]", "");
		
		StringBuilder versionBuilder = new StringBuilder();
		
		if (lastVersionNoDots.length() < 6) {
			
			versionBuilder.append(lastVersionNoDots);
			
			for (int i = 0; i < 6 - lastVersionNoDots.length(); i++) {
				versionBuilder.append("0");
			}
			
			lastVersionNoDots = versionBuilder.toString();
			versionBuilder.delete(0, versionBuilder.length()-1);
			
		}
		
		if (currentVersionNoDots.length() < 6) {
			
			versionBuilder.append(currentVersionNoDots);
			
			for (int i = 0; i < 6 - currentVersionNoDots.length(); i++) {
				versionBuilder.append("0");
			}
			
			currentVersionNoDots = versionBuilder.toString();
			
		}
		
		if (Integer.parseInt(lastVersionNoDots) > Integer.parseInt(currentVersionNoDots)) {

			newVersionAvailable = true;
			System.out.println("[BlockStreet] New version available!");
			
		}
		
	}

	public static boolean isNewVersionAvailable() {
		return newVersionAvailable;
	}
	
	public static String getCurrentVersion() {
		return currentVersion;
	}

	public static String getLastVersion() {
		return lastVersion;
	}
	
}
