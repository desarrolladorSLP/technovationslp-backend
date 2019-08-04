package org.desarrolladorslp.technovation.config.controller;

import java.lang.reflect.Type;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AccessTokenAdapter implements JsonSerializer<DefaultOAuth2AccessToken> {

    @Override
    public JsonElement serialize(DefaultOAuth2AccessToken accessToken, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject tokenInfo = new JsonObject();

        tokenInfo.addProperty("access_token", accessToken.getValue());
        tokenInfo.addProperty("expiresIn", accessToken.getExpiresIn());
        tokenInfo.addProperty("tokenType", accessToken.getTokenType());
        tokenInfo.addProperty("validated", (Boolean) accessToken.getAdditionalInformation().get("validated"));
        tokenInfo.addProperty("name", (String) accessToken.getAdditionalInformation().get("name"));
        tokenInfo.addProperty("email", (String) accessToken.getAdditionalInformation().get("email"));
        tokenInfo.addProperty("enabled", (Boolean) accessToken.getAdditionalInformation().get("enabled"));

        return tokenInfo;
    }
}
