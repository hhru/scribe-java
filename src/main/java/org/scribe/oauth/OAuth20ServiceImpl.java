package org.scribe.oauth;

import java.io.IOException;
import java.util.concurrent.Future;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.AbstractRequest;
import org.scribe.model.OAuthAsyncRequestCallback;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthRequestAsync;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

public class OAuth20ServiceImpl extends OAuthService {

    private static final String VERSION = "2.0";
    private final DefaultApi20 api;

    /**
     * Default constructor
     *
     * @param api OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(config);
        this.api = api;
    }

    /**
     * {@inheritDoc}
     */
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        Response response = createAccessTokenRequest(verifier, new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this)).send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public Future<Token> getAccessTokenAsync(Token requestToken, Verifier verifier, final OAuthAsyncRequestCallback<Token> callback) {
        OAuthRequestAsync request = createAccessTokenRequest(verifier, new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(),
                this));
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<Token>() {
            @Override
            public Token convert(com.ning.http.client.Response response) throws IOException {
                return api.getAccessTokenExtractor().extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        });
    }

    protected <T extends AbstractRequest> T createAccessTokenRequest(final Verifier verifier, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, verifier.getValue());
        request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }
        if (config.hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
        }
        return request;
    }

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    public void signRequest(Token accessToken, AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthorizationUrl(Token requestToken) {
        return api.getAuthorizationUrl(getConfig());
    }

}
