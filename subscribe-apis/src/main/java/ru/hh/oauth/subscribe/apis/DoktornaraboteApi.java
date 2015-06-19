package ru.hh.oauth.subscribe.apis;

import ru.hh.oauth.subscribe.apis.service.DoktornaraboteOAuthServiceImpl;
import ru.hh.oauth.subscribe.core.builder.api.DefaultApi20;
import ru.hh.oauth.subscribe.core.extractors.AccessTokenExtractor;
import ru.hh.oauth.subscribe.core.extractors.JsonTokenExtractor;
import ru.hh.oauth.subscribe.core.model.OAuthConfig;
import ru.hh.oauth.subscribe.core.model.OAuthConstants;
import ru.hh.oauth.subscribe.core.model.Verb;
import ru.hh.oauth.subscribe.core.oauth.OAuthService;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

public class DoktornaraboteApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://auth.doktornarabote.ru/OAuth/Authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    private static final String TOKEN_URL = "http://auth.doktornarabote.ru/OAuth/Token";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(
            config.getCallback(),
            "Must provide a valid url as callback. Doktornarabote does not support OOB");
        final StringBuilder sb = new StringBuilder(
            String.format(
                AUTHORIZE_URL,
                config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()),
                OAuthEncoder.encode(config.getScope())
            )
        );

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new DoktornaraboteOAuthServiceImpl(this, config);
    }
}
