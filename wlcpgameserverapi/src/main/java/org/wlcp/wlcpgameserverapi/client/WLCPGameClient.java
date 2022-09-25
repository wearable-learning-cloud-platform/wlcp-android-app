package org.wlcp.wlcpgameserverapi.client;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.wlcp.wlcpgameserverapi.dto.DisplayTextMessage;
import org.wlcp.wlcpgameserverapi.dto.GameInstance;
import org.wlcp.wlcpgameserverapi.dto.KeyboardInputMessage;
import org.wlcp.wlcpgameserverapi.dto.PlayerAvailableMessage;
import org.wlcp.wlcpgameserverapi.dto.SequenceButtonPressMessage;
import org.wlcp.wlcpgameserverapi.dto.SingleButtonPressMessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WLCPGameClient {

    private String baseURL = "10.0.2.2";
    private int port = 8050;
    public String gameInstanceId;
    public String usernameId;
    public PlayerAvailableMessage player;

    private StompClient stompClient;

    public interface WLCPGameClientCallback {
        void callback();
    }

    public interface WLCPGameClientCallbackString {
        void callback(String s);
    }

    public WLCPGameClientCallback connectionOpenedCallback = () -> {};
    public WLCPGameClientCallback connectionClosedCallback = () -> {};
    public WLCPGameClientCallback connectionErrorCallback = () -> {};
    public WLCPGameClientCallback connectionFailedServerHeartbeatCallback = () -> {};

    public WLCPGameClientCallback connectToGameInstanceCallback = () -> {};
    public WLCPGameClientCallback disconnectFromGameInstanceCallback = () -> {};

    public WLCPGameClientCallback noStateRequestCallback = () -> {};
    public WLCPGameClientCallbackString displayTextRequestCallback = (String s) -> {};
    private WLCPGameClientCallback displayPhotoRequestCallback = null;
    private WLCPGameClientCallback playSoundRequestCallback = null;
    private WLCPGameClientCallback playVideoRequestCallback = null;

    public WLCPGameClientCallback noTransitionRequestCallback = () -> {};
    public WLCPGameClientCallback singleButtonPressRequestCallback = () -> {};
    public WLCPGameClientCallback sequenceButtonPressRequestCallback = () -> {};
    public WLCPGameClientCallback keyboardInputRequestCallback = () -> {};
    private WLCPGameClientCallback randomInputRequestCallback = null;
    private WLCPGameClientCallback timerDurationRequestCallback = null;

    private static WLCPGameClient instance = null;
    private WLCPGameClient() {}

    public static WLCPGameClient getInstance()
    {
        if (instance == null)
            instance = new WLCPGameClient();

        return instance;
    }

    public static WLCPGameClient getInstance(String baseURL, int port)
    {
        if (instance == null)
            instance = new WLCPGameClient();
            instance.baseURL = baseURL;
            instance.port = port;

        return instance;
    }

    public List<String> fetchGameInstanceList() throws InterruptedException {
        AtomicReference<List<String>> returnList = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                returnList.set(fetchGameInstanceListInternal());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        return returnList.get();
    }

    private List<String> fetchGameInstanceListInternal() throws IOException {
        URL url = new URL("http://" + baseURL + ":" + port + "/wlcp-gameserver/gameInstanceController/allGameInstances");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new GsonBuilder().create();
            Reader reader = new InputStreamReader(con.getInputStream());
            List<GameInstance> gameInstances = gson.fromJson(reader, new TypeToken<List<GameInstance>>(){}.getType());
            List<String> gameInstanceStrings = new ArrayList<>();
            for(GameInstance gameInstance : gameInstances) {
                gameInstanceStrings.add(gameInstance.gameInstanceId.toString());
            }
            con.disconnect();
            return gameInstanceStrings;
        } else {
            con.disconnect();
        }
        return null;
    }

    public List<PlayerAvailableMessage> fetchPlayersAvailableFromGamePin(String gameInstanceId, String usernameId) throws InterruptedException {
        AtomicReference<List<PlayerAvailableMessage>> returnList = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                returnList.set(fetchPlayersAvailableFromGamePinInternal(gameInstanceId, usernameId));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        return returnList.get();
    }

    private List<PlayerAvailableMessage> fetchPlayersAvailableFromGamePinInternal(String gameInstanceId, String usernameId) throws IOException {
        URL url = new URL("http://" + baseURL + ":" + port + "/wlcp-gameserver/gameInstanceController/playersAvaliable/" + gameInstanceId + "/" + usernameId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new GsonBuilder().create();
            Reader reader = new InputStreamReader(con.getInputStream());
            List<PlayerAvailableMessage> players = gson.fromJson(reader, new TypeToken<List<PlayerAvailableMessage>>(){}.getType());
            con.disconnect();
            return players;
        } else {
            con.disconnect();
        }
        return null;
    }

    public void connect(String gameInstanceId, String usernameId, int team, int player) {
        this.gameInstanceId = gameInstanceId;
        this.usernameId = usernameId;
        PlayerAvailableMessage m = new PlayerAvailableMessage();
        m.team = team;
        m.player = player;
        this.player = m;
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + baseURL + ":" + port + "/wlcp-gameserver/wlcpGameServer-ws/0");
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i("OPENED", "OPENED");
                    connectionOpenedCallback.callback();
                    break;
                case CLOSED:
                    Log.i("CLOSED", "CLOSED");
                    connectionClosedCallback.callback();
                    break;
                case ERROR:
                    Log.i("CLOSED", "CLOSED");
                    connectionErrorCallback.callback();
                case FAILED_SERVER_HEARTBEAT:
                    Log.i("CLOSED", "CLOSED");
                    connectionFailedServerHeartbeatCallback.callback();
            }
        }).toString();
        stompClient.connect();
    }

    public void connectToGameInstance() {
        stompClient.topic("/subscription/connectionResult/" + this.gameInstanceId + "/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            connectToGameInstanceCallback.callback();
        });
        subscribeToChannels();
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/connectToGameInstance/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, "{}").subscribe();
    }

    private void subscribeToChannels() {
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/noState/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            noStateRequestCallback.callback();
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/displayText/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            Gson gson = new GsonBuilder().create();
            DisplayTextMessage msg = gson.fromJson(subscriptionMessage.getPayload(), DisplayTextMessage.class);
            displayTextRequestCallback.callback(msg.displayText);
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/noTransition/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            noStateRequestCallback.callback();
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/singleButtonPressRequest/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            singleButtonPressRequestCallback.callback();
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/sequenceButtonPressRequest/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            sequenceButtonPressRequestCallback.callback();
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/keyboardInputRequest/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            keyboardInputRequestCallback.callback();
        });
    }

    public void disconnectFromGameInstance() {
        stompClient.topic("/subscription/disconnectionResult/" + this.gameInstanceId + "/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            disconnectFromGameInstanceCallback.callback();
        });
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/disconnectFromGameInstance/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, "{}").subscribe();
    }

    public void disconnectFromGameServer() {
        stompClient.disconnect();
    }

    public void sendSingleButtonPress(int buttonPress) {
        SingleButtonPressMessage msg = new SingleButtonPressMessage();
        msg.buttonPress = buttonPress;
        Gson gson = new GsonBuilder().create();
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/singleButtonPress/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, gson.toJson(msg)).subscribe();
    }

    public void sendSequenceButtonPress(String sequence) {
        SequenceButtonPressMessage msg = new SequenceButtonPressMessage();
        msg.sequenceButtonPress = sequence;
        Gson gson = new GsonBuilder().create();
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/sequenceButtonPress/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, gson.toJson(msg)).subscribe();
    }

    public void sendKeyboardInput(String keyboardInput) {
        KeyboardInputMessage msg = new KeyboardInputMessage();
        msg.keyboardInput = keyboardInput;
        Gson gson = new GsonBuilder().create();
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/keyboardInput/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, gson.toJson(msg)).subscribe();
    }

}
