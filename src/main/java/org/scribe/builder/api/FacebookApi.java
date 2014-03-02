package org.scribe.builder.api;

import org.scribe.model.*;

import org.scribe.utils.*;

public class FacebookApi extends DefaultApi20 {
    private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?response_type=code";
    private static final String PARAM_REDIRECT_URI = "redirect_uri";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_STATE = "state";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://graph.facebook.com/oauth/access_token";
  }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

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
}
