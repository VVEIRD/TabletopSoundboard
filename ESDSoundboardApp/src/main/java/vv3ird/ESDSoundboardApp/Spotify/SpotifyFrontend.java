package vv3ird.ESDSoundboardApp.Spotify;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import com.wrapper.spotify.requests.data.player.GetUsersAvailableDevicesRequest;
import com.wrapper.spotify.requests.data.player.PauseUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.player.SetVolumeForUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.player.ToggleShuffleForUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

import vv3ird.ESDSoundboardApp.AudioApp;

public class SpotifyFrontend {
	
	private static Logger logger = LogManager.getLogger(SpotifyFrontend.class);

	static class SpotifyResponseHandler implements HttpHandler {
		

		HttpServer server = null;
		SpotifyFrontend sf = null;

		public SpotifyResponseHandler(HttpServer server, SpotifyFrontend sf) {
			this.server = server;
			this.sf = sf;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			System.out.println("Request: " + t.getRequestURI().toString());
			Map<String, String> gets = queryToMap(t.getRequestURI().getQuery());
//			System.out.println("Code: " + gets.get("code"));
//			System.out.println("State: " + gets.get("state"));
			sf.authCode = gets.get("code");
			if ("access_denied".equalsIgnoreCase(gets.get("error")))
				sf.accessDenied = true;
			String response = sf.authCode != null ? "Authorization aquired, this page can be closed"
					: "Invalid Spotify response, cannot login further"
							+ (sf.accessDenied ? ": Access Denied by user" : "");
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			server.removeContext("/spotify-redirect");
		}

		public Map<String, String> queryToMap(String query) {
			Map<String, String> result = new HashMap<>();
			for (String param : query.split("&")) {
				String[] entry = param.split("=");
				if (entry.length > 1) {
					result.put(entry[0], entry[1]);
				} else {
					result.put(entry[0], "");
				}
			}
			return result;
		}
	}
	
	private class PlayListUpdater implements Runnable {
		@Override
		public void run() {
			if(AudioApp.isSpotifyEnabled())
			currentUserPlaylists = getListOfCurrentUsersPlaylists_Sync(api);
			try {
				Thread.sleep(5_000);
			} catch (Exception e) {
				logger.error("Error reading Spotify Playlists", e);
				e.printStackTrace();
			}
		}
	}

	private static SpotifyFrontend instance = null;

	private static HttpServer server = null;

	public static AuthorizationCodeCredentials authorizationCodeRefresh_Sync(SpotifyApi api) {
		try {
			final AuthorizationCodeCredentials authorizationCodeCredentials = api.authorizationCodeRefresh().build()
					.execute();

			// Set access and refresh token for further "spotifyApi" object usage
			api.setAccessToken(authorizationCodeCredentials.getAccessToken());
			if (authorizationCodeCredentials.getRefreshToken() != null)	
				api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

			System.out.println("New access token: " + authorizationCodeCredentials.getAccessToken());
			System.out.println("New refresh token: " + authorizationCodeCredentials.getRefreshToken());
			System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
			return authorizationCodeCredentials;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public static void authorizationCodeUri_Async(AuthorizationCodeUriRequest authorizationCodeUriRequest) {
		try {
			final Future<URI> uriFuture = authorizationCodeUriRequest.executeAsync();
			// ...

			final URI uri = uriFuture.get();

			System.out.println("URI: " + uri.toString());
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: " + e.getCause().getMessage());
		}
	}

	private static void authorizationCodeUri_Sync(AuthorizationCodeUriRequest authorizationCodeUriRequest) {
		final URI uri = authorizationCodeUriRequest.execute();

		System.out.println("Auth-URI: " + uri.toString());
		openWebpage(uri);
	}

	private static AuthorizationCodeCredentials authorizationCode_Sync(SpotifyApi api, String authCode) {
		try {
			AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(authCode).build();
			final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
			// Set access and refresh token for further "spotifyApi" object usage
			api.setAccessToken(authorizationCodeCredentials.getAccessToken());
			api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
			System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
			return authorizationCodeCredentials;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public static SpotifyFrontend createInstance(String clientId, String clientSecret, String responseUrl,
			boolean autoLogin) {
		if (instance != null && instance.clientId.equals(clientId) && instance.clientSecret.equals(clientSecret)
				&& instance.responseUri.equals(responseUrl)) {
			if (!instance.isLoggedIn() && autoLogin)
				instance.login();
			return instance;
		}
		instance = new SpotifyFrontend(clientId, clientSecret, responseUrl);
		if (!instance.isLoggedIn() && autoLogin)
			instance.login();
		return instance;
	}
	
	public static SpotifyFrontend getInstance() {
		return instance;
	}

	public static List<PlaylistSimplified> getListOfCurrentUsersPlaylists_Sync(SpotifyApi api) {
		try {
			GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = api
					.getListOfCurrentUsersPlaylists().limit(20).offset(0).build();
			Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

			System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
			System.out.println();
			List<PlaylistSimplified> playlists = new ArrayList<>(playlistSimplifiedPaging.getTotal());
			for (int i = 0; i < playlistSimplifiedPaging.getTotal(); i += 20) {
				PlaylistSimplified[] asas = playlistSimplifiedPaging.getItems();
				for (PlaylistSimplified playlistSimplified : asas) {
					playlists.add(playlistSimplified);
//					System.out.println("Playlist :" + playlistSimplified.getName());
//					System.out.println("ID :      " + playlistSimplified.getId());
//					System.out.println();
				}
				getListOfCurrentUsersPlaylistsRequest = api.getListOfCurrentUsersPlaylists().limit(20).offset(i)
						.build();
				playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();
			}
			return playlists;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public static Playlist getPlaylist_Sync(SpotifyApi api, String playlistId) {
		GetPlaylistRequest getPlaylistRequest = api.getPlaylist(playlistId)
//		          .fields("description")
				.build();
		try {
			final Playlist playlist = getPlaylistRequest.execute();
			return playlist;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public CurrentlyPlayingContext getInformationAboutUsersCurrentPlayback() {
		return getInformationAboutUsersCurrentPlayback_Sync(api);
	}

	public static CurrentlyPlayingContext getInformationAboutUsersCurrentPlayback_Sync(SpotifyApi api) {
		GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest = api
				.getInformationAboutUsersCurrentPlayback().build();
		try {
			return getInformationAboutUsersCurrentPlaybackRequest.execute();
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	private static Device[] getUsersAvailableDevices_Sync(SpotifyApi api) {
		try {

			GetUsersAvailableDevicesRequest getUsersAvailableDevicesRequest = api.getUsersAvailableDevices().build();
			final Device[] devices = getUsersAvailableDevicesRequest.execute();

			System.out.println("Length: " + devices.length);
			return devices;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void pauseUsersPlayback_Sync(SpotifyApi api) {
		PauseUsersPlaybackRequest pauseUsersPlaybackRequest = api.pauseUsersPlayback()
//			          .device_id(deviceId)
				.build();
		try {
			final String string = pauseUsersPlaybackRequest.execute();
			System.out.println("Null: " + string);
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void setVolumeForUsersPlayback_Sync(SpotifyApi api, int volumePercent) {
		SetVolumeForUsersPlaybackRequest setVolumeForUsersPlaybackRequest = api.setVolumeForUsersPlayback(volumePercent)
//			          .device_id("5fbb3ba6aa454b5534c4ba43a8c7e8e45a63ad0e")
				.build();
		try {
			setVolumeForUsersPlaybackRequest.execute();
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static boolean startResumeUsersPlaybackPlaylist_Sync(SpotifyApi api, String playlistId, String deviceId) {
		final StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest = api.startResumeUsersPlayback()
				.context_uri("spotify:playlist:" + playlistId).device_id(deviceId)
//		          .offset(new JsonParser().parse("{\"uri\":\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"}").getAsJsonObject())
//		          .uris(new JsonParser().parse("[\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"]").getAsJsonArray())
				.build();
		try {
			final String string = startResumeUsersPlaybackRequest.execute();
			System.out.println("Null: " + string);
			return true;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return false;
	}

	public static boolean toggleShuffleForUsersPlayback_Sync(SpotifyApi api, boolean enable, String deviceId) {
		ToggleShuffleForUsersPlaybackRequest toggleShuffleForUsersPlaybackRequest = api
				.toggleShuffleForUsersPlayback(enable).device_id(deviceId).build();
		try {
			final String string = toggleShuffleForUsersPlaybackRequest.execute();

			System.out.println("Returnvalue: " + string);
			return true;
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return false;
	}

	private String clientId = null;

	private String clientSecret = null;

	private Device activeDevice = null;

	private String responseUri = "http://localhost:5000/spotify-redirect";

	private String reqiredAccesssTokens = "user-read-birthdate,user-read-email,playlist-read-private,user-modify-playback-state,user-read-playback-state";

	private transient Timer refershTimer = null;

	private transient String refreshToken = null;

	private transient String accessToken = null;

	private transient SpotifyApi api = null;

	private transient String authCode = null;

	private transient boolean accessDenied = false;

	private transient boolean loggedIn = false;

	public SpotifyFrontend(String clientId, String clientSecret, String redirectUri) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.responseUri = redirectUri;
	}

	public AuthorizationCodeCredentials authorizationCode() {
		return authorizationCode_Sync(api, authCode);
	}

	public AuthorizationCodeCredentials authorizationCode(String authCode) {
		return authorizationCode_Sync(api, authCode);
	}

	public Device getActiveDevice() {
		return activeDevice;
	}

	public Playlist getPlaylist(String playlistId) {
		return getPlaylist_Sync(api, playlistId);
	}

	public Device[] getUsersAvailableDevices() {
		return getUsersAvailableDevices_Sync(api);
	}
	
	public void setActiveDevice(Device device) {
		activeDevice = Objects.requireNonNull(device);
		AudioApp.addConfig("spotify.device", device.getName());
	}
	
	
	List<PlaylistSimplified> currentUserPlaylists = new LinkedList<PlaylistSimplified>();

	public List<PlaylistSimplified> getListOfCurrentUsersPlaylists() {
		return currentUserPlaylists;//getListOfCurrentUsersPlaylists_Sync(api);
	}

	private void init() {
		refershTimer = new Timer(true);
		refershTimer.schedule(new TimerTask() {
			@Override
			public void run() {
//				api.setRefreshToken(refreshToken);
				AuthorizationCodeCredentials acc = authorizationCodeRefresh_Sync(api);
				accessToken = acc.getAccessToken();
				if(acc.getRefreshToken() != null)	
					refreshToken = acc.getRefreshToken();
//				api.setAccessToken(accessToken);
//				api.setRefreshToken(refreshToken);
			}
		}, 600_000, 600_000);
		initActiveDevice();
		initDaemon();
	}


	private void initDaemon() {
		Thread updater = new Thread(new PlayListUpdater());
		updater.setDaemon(true);
		updater.start();
	}

	private void initActiveDevice() {
		Device[] devices = getUsersAvailableDevices_Sync(api);
		String activeDeviceName = AudioApp.getConfig("spotify.device");
		
		if (activeDeviceName != null && devices != null && devices.length > 0) { 
			for (Device device : devices) {
				if (device.getName().equals(activeDeviceName)) {
					this.setActiveDevice(device);
				}
			}
		} 
		if (this.activeDevice  == null && devices != null && devices.length > 0)
			this.activeDevice = devices[0];
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void login() {
		this.api = new SpotifyApi.Builder().setClientId(this.clientId).setClientSecret(this.clientSecret)
				.setRedirectUri(SpotifyHttpManager.makeUri(responseUri)).build();
		AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri().state("x4xkmn9pu3j6ukrs8n")
				.scope(reqiredAccesssTokens).show_dialog(true).build();
		this.authCode = null;
		this.accessDenied = false;
		try {
			if (server == null) {
				server = HttpServer.create(new InetSocketAddress("localhost", 5000), 0);
				server.start();
			}
			server.createContext("/spotify-redirect", new SpotifyResponseHandler(server, this));
			authorizationCodeUri_Sync(authorizationCodeUriRequest);
			long waited = 0;
			while (authCode == null && waited < 180_000 && !accessDenied) {
				Thread.sleep(50);
			}
			if (authCode != null && !accessDenied) {
				AuthorizationCodeCredentials acc = this.authorizationCode(authCode);
				// clear code
				authCode = null;
				if (acc != null) {
					accessToken = acc.getAccessToken();
					refreshToken = acc.getRefreshToken();
//					System.out.println();
//					System.out.println("Access Token: " + acc.getAccessToken());
//					System.out.println("Refresh Token: " + acc.getRefreshToken());
//					System.out.println("Expires in: " + acc.getExpiresIn());
					if (accessToken != null && refreshToken != null) {
						loggedIn = true;
						System.out.println("Logged into spotify");
					}
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if (loggedIn)
			init();
	}

	public void pauseUsersPlayback() {
		pauseUsersPlayback_Sync(api);
	}

	public void setDefaultDevice(Device defaultDevice) {
		defaultDevice = Objects.requireNonNull(defaultDevice, "Device cannot be null");
		this.activeDevice = defaultDevice;
	}

	public void setVolumeForUsersPlayback(int volumePercent) {
		volumePercent = volumePercent > 100 ? 100 : volumePercent < 0 ? 0 : volumePercent;
		setVolumeForUsersPlayback_Sync(api, volumePercent);
	}

	public boolean startResumeUsersPlaybackPlaylist(String playlistId) {
		if(activeDevice == null)
			initActiveDevice();
		return startResumeUsersPlaybackPlaylist_Sync(api, playlistId, activeDevice != null ? activeDevice.getId() : null);
	}

	public boolean startResumeUsersPlaybackPlaylist(String playlistId, Device device) {
		return startResumeUsersPlaybackPlaylist_Sync(api, playlistId, device.getId());
	}

	public boolean toggleShuffleForUsersPlayback(boolean enable) {
		return toggleShuffleForUsersPlayback_Sync(api, enable, activeDevice.getId());
	}

	public boolean toggleShuffleForUsersPlayback(boolean enable, Device device) {
		return toggleShuffleForUsersPlayback_Sync(api, enable, device.getId());
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		String clientId = "9c236ce791684c0788d2b84ff8e2f6a7";
		String clientSecret = "???";
		String redirectUri = "http://localhost:5000/spotify-redirect";
		SpotifyFrontend sf = new SpotifyFrontend(clientId, clientSecret, redirectUri);
		sf.login();
		System.out.println("===============================================");
		System.out.println(" Playlists");
		System.out.println("===============================================");
		List<PlaylistSimplified> playlists = sf.getListOfCurrentUsersPlaylists();
		System.out.println();
		System.out.println();
		System.out.println("===============================================");
		System.out.println(" Devices ");
		System.out.println("===============================================");
		Device[] devices = sf.getUsersAvailableDevices();
		for (Device device : devices) {
			System.out.println("Device: " + device.getName());
			System.out.println("    ID: " + device.getId());
			System.out.println();
		}
		while (true) {
			int playlistIndex = (int) Math.round(Math.random() * playlists.size());
			System.out.println("===============================================");
			System.out.println(" Playing playlist \"" + playlists.get(playlistIndex).getName() + "\"");
			System.out.println("===============================================");
			System.out.println();
			sf.startResumeUsersPlaybackPlaylist(playlists.get(playlistIndex).getId());
			Thread.sleep(60_000);
		}
//		System.exit(0);
	}
}