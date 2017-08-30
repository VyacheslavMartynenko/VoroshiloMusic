package mp3.music.download.downloadmp3.downloadmusic.util.preferences;

import android.content.Context;

import mp3.music.download.downloadmp3.downloadmusic.model.networking.DataBody;

public class UserPreferences extends AbstractPreferences {
    private static final String PREFERENCES = "UserPreferences";
    private static final String IS_APP_RATED = "IsAppRated";

    private static final String AD_NET_TYPE = "AdNetType";
    private static final String AD_NET_TUTORIAL = "AdNetTutorial";
    private static final String AD_NET_DOWNLOAD = "AdNetDownload";
    private static final String AD_NET_PLAY = "AdNetPlay";
    private static final String AD_NET_SEARCH = "AdNetSearch";
    private static final String AD_NET_BANNER = "AdNetBanner";
    private static final String POPUP_STATUS = "PopupStatus";
    private static final String POPUP_TEXT = "PopupText";
    private static final String POPUP_URL = "PopupUrl";
    private static final String BURST_STATUS = "BurstStatus";
    private static final String BURST_TEXT = "BurstText";
    private static final String BURST_URL = "BurstUrl";
    private static final String APPODEAL_KEY = "AppodealKey";
    private static final String STARTAPP_KEY = "StartappKey";
    private static final String TUTORIAL_STATUS = "TutorialStatus";
    private static final String MUSIC_URL = "MusicUrl";

    private static UserPreferences instance;

    public static UserPreferences getInstance() {
        return instance;
    }

    private UserPreferences(Context context, String name) {
        super(context, name);
    }

    static void init(Context context) {
        instance = new UserPreferences(context, PREFERENCES);
    }

    public boolean isAppRated() {
        return preferences.getBoolean(IS_APP_RATED, false);
    }

    public void setIsAppRated() {
        preferences.edit().putBoolean(IS_APP_RATED, true).apply();
    }

    public int getAdNetType() {
        return preferences.getInt(AD_NET_TYPE, DataBody.NO);
    }

    public void setAdNetType(int adNetType) {
        preferences.edit().putInt(AD_NET_TYPE, adNetType).apply();
    }

    public int getAdNetTutorial() {
        return preferences.getInt(AD_NET_TUTORIAL, DataBody.NO);
    }

    public void setAdNetTutorial(int adNetTutorial) {
        preferences.edit().putInt(AD_NET_TUTORIAL, adNetTutorial).apply();
    }

    public int getAdNetDownload() {
        return preferences.getInt(AD_NET_DOWNLOAD, DataBody.NO);
    }

    public void setAdNetDownload(int adNetDownload) {
        preferences.edit().putInt(AD_NET_DOWNLOAD, adNetDownload).apply();
    }

    public int getAdNetPlay() {
        return preferences.getInt(AD_NET_PLAY, DataBody.NO);
    }

    public void setAdNetPlay(int adNetPlay) {
        preferences.edit().putInt(AD_NET_PLAY, adNetPlay).apply();
    }

    public int getAdNetSearch() {
        return preferences.getInt(AD_NET_SEARCH, DataBody.NO);
    }

    public void setAdNetSearch(int adNetSearch) {
        preferences.edit().putInt(AD_NET_SEARCH, getAdNetSearch()).apply();
    }

    public int getAdNetBanner() {
        return preferences.getInt(AD_NET_BANNER, DataBody.NO);
    }

    public void setAdNetBanner(int adNetBanner) {
        preferences.edit().putInt(AD_NET_BANNER, adNetBanner).apply();
    }

    public int getPopupStatus() {
        return preferences.getInt(POPUP_STATUS, DataBody.NO);
    }

    public void setPopupStatus(int popupStatus) {
        preferences.edit().putInt(POPUP_STATUS, popupStatus).apply();
    }

    public String getPopupText() {
        return preferences.getString(POPUP_TEXT, null);
    }

    public void setPopupText(String popupText) {
        preferences.edit().putString(POPUP_TEXT, popupText).apply();
    }

    public String getPopupUrl() {
        return preferences.getString(POPUP_URL, null);
    }

    public void setPopupUrl(String popupUrl) {
        preferences.edit().putString(POPUP_URL, popupUrl).apply();
    }

    public int getBurstStatus() {
        return preferences.getInt(BURST_STATUS, DataBody.NO);
    }

    public void setBurstStatus(int adNetTutorial) {
        preferences.edit().putInt(BURST_STATUS, adNetTutorial).apply();
    }

    public String getBurstText() {
        return preferences.getString(BURST_TEXT, null);
    }

    public void setBurstText(String burstText) {
        preferences.edit().putString(BURST_TEXT, burstText).apply();
    }

    public String getBurstUrl() {
        return preferences.getString(BURST_URL, null);
    }

    public void setBurstUrl(String burstUrl) {
        preferences.edit().putString(BURST_URL, burstUrl).apply();
    }

    public String getAppodealKey() {
        return preferences.getString(APPODEAL_KEY, "ce1428dbf38eaecd37c305f088343f98eed87d5b22169426");
    }

    public void setAppodealKey(String appodealKey) {
        preferences.edit().putString(APPODEAL_KEY, appodealKey).apply();
    }

    public String getStartappKey() {
        return preferences.getString(STARTAPP_KEY, "207572485");
    }

    public void setStartappKey(String startappKey) {
        preferences.edit().putString(STARTAPP_KEY, startappKey).apply();
    }

    public int getTutorialStatus() {
        return preferences.getInt(TUTORIAL_STATUS, DataBody.NO);
    }

    public void setTutorialStatus(int adNetTutorial) {
        preferences.edit().putInt(TUTORIAL_STATUS, adNetTutorial).apply();
    }

    public String getMusicUrl() {
        return preferences.getString(MUSIC_URL, null);
    }

    public void setMusicUrl(String musicUrl) {
        preferences.edit().putString(MUSIC_URL, musicUrl).apply();
    }
}