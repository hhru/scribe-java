package org.scribe.examples;

import java.util.Random;
import java.util.Scanner;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi20;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

public class Google20Example {
  private static final String NETWORK_NAME = "G+";
  private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/%s";
  private static final Token EMPTY_TOKEN = null;

  public static void main(String[] args) {
    // Replace these with your own api key and secret
    final String clientId = "api key";
    final String apiSecret = "api secret";
    String secretState = "secret" + new Random(System.currentTimeMillis()).nextInt(999999);
    final OAuth20ServiceImpl service = (OAuth20ServiceImpl) new ServiceBuilder()
            .provider(GoogleApi20.class)
            .apiKey(clientId)
            .apiSecret(apiSecret)
            .scope("openid profile") // replace with desired scope
            .grantType("authorization_code")
            .state(secretState)
            .callback("http://example.com/callback")
            .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    System.out.println();

    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize Scribe here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the authorization code here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();

      System.out.println("And paste the state from server here");
      System.out.print(">>");
      String value = in.nextLine();
      if (!secretState.equals(value)) {
          System.out.println("Ooops, state value does not match!");
          System.out.println("Expected = " + secretState);
          System.out.println("Got      = " + value);
          System.out.println();
      }

    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    while (true) {
      System.out.println("Paste profile query for fetch, for example 'me' for your account");
      System.out.print(">>");
      final String query = in.nextLine();
      System.out.println();

      final OAuthRequest request = new OAuthRequest(Verb.GET, String.format(PROTECTED_RESOURCE_URL, query));
      service.signRequest(accessToken, request);
      final Response response = request.send();
      System.out.println();
      System.out.println(response.getCode());
      System.out.println(response.getBody());

      System.out.println();
    }
  }
}
