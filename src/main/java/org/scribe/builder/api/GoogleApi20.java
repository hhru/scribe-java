package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class GoogleApi20 extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code";
    private static final String PARAM_REDIRECT_URI = "redirect_uri";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_STATE = "state";

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "https://accounts.google.com/o/oauth2/token";
  }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Google+ does not support OOB");
        StringBuilder sb = new StringBuilder(AUTHORIZE_URL);
        sb.append("&").append(PARAM_CLIENT_ID).append("=").append(config.getApiKey());
        sb.append("&").append(PARAM_REDIRECT_URI).append("=").append(OAuthEncoder.encode(config.getCallback()));
        // Append scope if present
        if (config.hasScope()) {
            sb.append("&").append(PARAM_SCOPE).append("=")
              .append(OAuthEncoder.encode(config.getScope()));
        }
        // Append state if present
        if (config.hasState()) {
            sb.append("&").append(PARAM_STATE).append("=")
              .append(OAuthEncoder.encode(config.getState()));
        }
        return sb.toString();
    }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }
}
