package helpers;

public class Environment {
    public static final String
        weatherApiKey = System.getProperty("weather_api_key", null),
        weatherLang = System.getProperty("weather_lang","ru"),
        cityId = System.getProperty("city_id", "524901"),
        tlgBotIdAndSecret = System.getProperty("tlg_bot", null),
        tlgChatId = System.getProperty("tlg_chat_id", null);
}

