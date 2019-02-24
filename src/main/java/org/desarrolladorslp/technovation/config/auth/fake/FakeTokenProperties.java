package org.desarrolladorslp.technovation.config.auth.fake;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("fake-token-granter")
@Component
@ConfigurationProperties(prefix = "fake-authentication")
public class FakeTokenProperties {

    private Map<String, FakeToken> fakeTokens;

    public Map<String, FakeToken> getFakeTokens() {
        return fakeTokens;
    }

    public void setFakeTokens(Map<String, FakeToken> fakeTokens) {
        this.fakeTokens = fakeTokens;
    }
}
