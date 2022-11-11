package org.wlcp.wlcpgameserverapi.client;

import android.util.Log;
import android.view.Display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.wlcp.wlcpgameserverapi.dto.CombinedMessage;
import org.wlcp.wlcpgameserverapi.dto.DisplayPhotoMessage;
import org.wlcp.wlcpgameserverapi.dto.DisplayTextMessage;
import org.wlcp.wlcpgameserverapi.dto.GameInstance;
import org.wlcp.wlcpgameserverapi.dto.KeyboardInputMessage;
import org.wlcp.wlcpgameserverapi.dto.MessageType;
import org.wlcp.wlcpgameserverapi.dto.PlaySoundMessage;
import org.wlcp.wlcpgameserverapi.dto.PlayVideoMessage;
import org.wlcp.wlcpgameserverapi.dto.PlayerAvailableMessage;
import org.wlcp.wlcpgameserverapi.dto.SequenceButtonPressMessage;
import org.wlcp.wlcpgameserverapi.dto.SingleButtonPressMessage;
import org.wlcp.wlcpgameserverapi.dto.TimerDurationMessage;

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

    private String httpBaseURL = "http://10.0.2.2";
    private String wsBaseURL = "ws://10.0.2.2";
    private int port = 8050;
    public String gameInstanceId;
    public String usernameId;
    public PlayerAvailableMessage player;

    private StompClient stompClient;

    public interface WLCPGameClientCallback {
        void callback();
    }

    public interface WLCPGameClientCallbackDisplayText {
        void callback(DisplayTextMessage msg);
    }

    public interface WLCPGameClientCallbackDisplayPhoto {
        void callback(DisplayPhotoMessage msg);
    }

    public interface WLCPGameClientCallbackPlaySound {
        void callback(PlaySoundMessage msg);
    }

    public interface WLCPGameClientCallbackPlayVideo {
        void callback(PlayVideoMessage msg);
    }

    public interface WLCPGameClientCallbackDisplayTextDisplayPhoto {
        void callback(DisplayTextMessage displayTextMessage, DisplayPhotoMessage displayPhotoMessage);
    }

    public interface WLCPGameClientCallbackDisplayTextPlaySound {
        void callback(DisplayTextMessage displayTextMessage, PlaySoundMessage playSoundMessage);
    }

    public interface WLCPGameClientCallbackDisplayTextPlayVideo {
        void callback(DisplayTextMessage displayTextMessage, PlayVideoMessage playVideoMessage);
    }

    public interface WLCPGameClientCallbackDisplayPhotoPlaySound {
        void callback(DisplayPhotoMessage displayPhotoMessage, PlaySoundMessage playSoundMessage);
    }

    public interface WLCPGameClientCallbackTimerDelay {
        void callback(int duration);
    }

    public interface WLCPGameCLientCallbackTimer {
        void callback(int duration, MessageType type);
    }

    public WLCPGameClientCallback connectionOpenedCallback = () -> {};
    public WLCPGameClientCallback connectionClosedCallback = () -> {};
    public WLCPGameClientCallback connectionErrorCallback = () -> {};
    public WLCPGameClientCallback connectionFailedServerHeartbeatCallback = () -> {};

    public WLCPGameClientCallback connectToGameInstanceCallback = () -> {};
    public WLCPGameClientCallback disconnectFromGameInstanceCallback = () -> {};

    public WLCPGameClientCallback noStateRequestCallback = () -> {};
    public WLCPGameClientCallbackDisplayText displayTextRequestCallback = (DisplayTextMessage msg) -> {};
    public WLCPGameClientCallbackDisplayPhoto displayPhotoRequestCallback = (DisplayPhotoMessage msg) -> {};
    public WLCPGameClientCallbackPlaySound playSoundRequestCallback = (PlaySoundMessage msg) -> {};
    public WLCPGameClientCallbackPlayVideo playVideoRequestCallback = (PlayVideoMessage msg) -> {};
    public WLCPGameClientCallbackDisplayTextDisplayPhoto displayTextDisplayPhotoRequestCallback = (DisplayTextMessage displayTextMessage, DisplayPhotoMessage displayPhotoMessage) -> {};
    public WLCPGameClientCallbackDisplayTextPlaySound displayTextPlaySoundRequestCallback = (DisplayTextMessage displayTextMessage, PlaySoundMessage playSoundMessage) -> {};
    public WLCPGameClientCallbackDisplayTextPlayVideo displayTextPlayVideoRequestCallback = (DisplayTextMessage displayTextMessage, PlayVideoMessage playVideoMessage) -> {};
    public WLCPGameClientCallbackDisplayPhotoPlaySound displayPhotoPlaySoundRequestCallback = (DisplayPhotoMessage displayPhotoMessage, PlaySoundMessage playSoundMessage) -> {};

    public WLCPGameClientCallback noTransitionRequestCallback = () -> {};
    public WLCPGameClientCallback singleButtonPressRequestCallback = () -> {};
    public WLCPGameClientCallback sequenceButtonPressRequestCallback = () -> {};
    public WLCPGameClientCallback keyboardInputRequestCallback = () -> {};
    public WLCPGameClientCallback randomInputRequestCallback = () -> {};
    public WLCPGameClientCallbackTimerDelay timerDurationRequestCallback = (int duration) -> {};
    public WLCPGameCLientCallbackTimer timerRequestCallback = (int duration, MessageType type) -> {};

    private static WLCPGameClient instance = null;
    private WLCPGameClient() {}

    public static WLCPGameClient getInstance()
    {
        if (instance == null)
            instance = new WLCPGameClient();

        return instance;
    }

    public static WLCPGameClient getInstance(String httpBaseURL, String wsBaseURL, int port)
    {
        if (instance == null)
            instance = new WLCPGameClient();
            instance.httpBaseURL = httpBaseURL;
            instance.wsBaseURL = wsBaseURL;
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
        URL url = new URL(httpBaseURL + ":" + port + "/wlcp-gameserver/gameInstanceController/allGameInstances");
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
        URL url = new URL(httpBaseURL + ":" + port + "/wlcp-gameserver/gameInstanceController/playersAvaliable/" + gameInstanceId + "/" + usernameId);
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
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsBaseURL + ":" + port + "/wlcp-gameserver/wlcpGameServer-ws/0");
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
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/noTransition/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            noStateRequestCallback.callback();
        });
        stompClient.topic("/subscription/gameInstance/" + this.gameInstanceId + "/combinedMessage/" + this.usernameId + "/" + this.player.team + "/" + this.player.player).subscribe(subscriptionMessage -> {
            Gson gson = new GsonBuilder().create();
            CombinedMessage msg = gson.fromJson(subscriptionMessage.getPayload(), CombinedMessage.class);

            //Parse the output messages
            if(msg.outputMessages.size() == 1) {
                switch(msg.outputMessages.get(0).type) {
                    case DISPLAY_TEXT:
                        DisplayTextMessage displayTextMessage = gson.fromJson(msg.outputMessages.get(0).msg, DisplayTextMessage.class);
                        displayTextRequestCallback.callback(displayTextMessage);
                        break;
                    case DISPLAY_PHOTO:
                        DisplayPhotoMessage displayPhotoMessage = gson.fromJson(msg.outputMessages.get(0).msg, DisplayPhotoMessage.class);
                        displayPhotoRequestCallback.callback(displayPhotoMessage);
                        break;
                    case PLAY_SOUND:
                        PlaySoundMessage playSoundMessage = gson.fromJson(msg.outputMessages.get(0).msg, PlaySoundMessage.class);
                        playSoundRequestCallback.callback(playSoundMessage);
                        break;
                    case PLAY_VIDEO:
                        PlayVideoMessage playVideoMessage = gson.fromJson(msg.outputMessages.get(0).msg, PlayVideoMessage.class);
                        playVideoRequestCallback.callback(playVideoMessage);
                        break;
                }
            } else if(msg.outputMessages.size() == 2) {
                switch(msg.outputMessages.get(0).type) {
                    case DISPLAY_TEXT:
                        DisplayTextMessage displayTextMessage = gson.fromJson(msg.outputMessages.get(0).msg, DisplayTextMessage.class);
                        switch(msg.outputMessages.get(1).type) {
                            case DISPLAY_PHOTO:
                                DisplayPhotoMessage displayPhotoMessage = gson.fromJson(msg.outputMessages.get(1).msg, DisplayPhotoMessage.class);
                                displayTextDisplayPhotoRequestCallback.callback(displayTextMessage, displayPhotoMessage);
                                break;
                            case PLAY_SOUND:
                                PlaySoundMessage playSoundMessage = gson.fromJson(msg.outputMessages.get(1).msg, PlaySoundMessage.class);
                                displayTextPlaySoundRequestCallback.callback(displayTextMessage, playSoundMessage);
                                break;
                            case PLAY_VIDEO:
                                PlayVideoMessage playVideoMessage = gson.fromJson(msg.outputMessages.get(1).msg, PlayVideoMessage.class);
                                displayTextPlayVideoRequestCallback.callback(displayTextMessage, playVideoMessage);
                                break;
                        }
                        break;
                    case DISPLAY_PHOTO:
                        DisplayPhotoMessage displayPhotoMessage = gson.fromJson(msg.outputMessages.get(0).msg, DisplayPhotoMessage.class);
                        switch(msg.outputMessages.get(1).type) {
                            case PLAY_SOUND:
                                PlaySoundMessage playSoundMessage = gson.fromJson(msg.outputMessages.get(1).msg, PlaySoundMessage.class);
                                displayPhotoPlaySoundRequestCallback.callback(displayPhotoMessage, playSoundMessage);
                                break;
                        }
                        break;
                }
            } else {
                //Not supported
                throw new Exception("More than 2 output states is not supported.");
            }

            //Parse the input messages
            if(msg.inputMessages.size() == 1) {
                switch(msg.inputMessages.get(0).type) {
                    case SINGLE_BUTTON_PRESS:
                        singleButtonPressRequestCallback.callback();
                        break;
                    case SEQUENCE_BUTTON_PRESS:
                        sequenceButtonPressRequestCallback.callback();
                        break;
                    case KEYBOARD_INPUT:
                        keyboardInputRequestCallback.callback();
                        break;
                    case RANDOM:
                        randomInputRequestCallback.callback();
                        break;
                    case TIMER_DURATION:
                        TimerDurationMessage timerDurationMessage = gson.fromJson(msg.inputMessages.get(0).msg, TimerDurationMessage.class);
                        if(!timerDurationMessage.isTimer) {
                            timerDurationRequestCallback.callback(timerDurationMessage.duration);
                        }
                        break;
                }
            } else if(msg.inputMessages.size() == 2) {
                switch(msg.inputMessages.get(0).type) {
                    case TIMER_DURATION:
                        TimerDurationMessage timerDurationMessage = gson.fromJson(msg.inputMessages.get(0).msg, TimerDurationMessage.class);
                        if(timerDurationMessage.isTimer) {
                            timerRequestCallback.callback(timerDurationMessage.duration, msg.inputMessages.get(1).type);
                        }
                        break;
                }
            } else {
                //Not supported
                throw new Exception("More than 2 input transitions is not supported.");
            }
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

    public void sendRandomInput() {
        stompClient.send("/app/gameInstance/" + this.gameInstanceId + "/randomInput/" + this.usernameId + "/" + this.player.team + "/" + this.player.player, "{}").subscribe();
    }

}
