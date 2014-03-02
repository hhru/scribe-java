package org.scribe.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

public class JsonTokenExtractor implements AccessTokenExtractor {
  private Pattern accessTokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");
  private Pattern idTokenPattern = Pattern.compile("\"id_token\"\\s*:\\s*\"(\\S*?)\"");

  public Token extract(String response) {
      Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
      Matcher matcher = accessTokenPattern.matcher(response);
      if (!matcher.find()) {
          throw new OAuthException("Cannot extract an access token. Response was: " + response);
      }

      Token token = new Token(matcher.group(1), "", response);
      matcher = idTokenPattern.matcher(response);
      if (matcher.find()) {
          token.setIdToken(matcher.group(1));
      }
      return token;
  }
}
