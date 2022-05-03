package com.impakt.social.login.servlet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class SocialLoginServlet extends HttpServlet {

    public static final String FACEBOOK_APP_ID = "enter facebook app id here";

    public static final String GOOGLE_CLIENT_ID = "enter google client id here";


    private static final JacksonFactory jacksonFactory = new JacksonFactory();

    private static final ObjectMapper mapper = new ObjectMapper();

    private static GoogleIdTokenVerifier verifier;

    public SocialLoginServlet() throws RuntimeException {
        try {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            verifier = new GoogleIdTokenVerifier.Builder( transport, jacksonFactory )
                    .setAudience( Collections.singletonList( GOOGLE_CLIENT_ID ) ).build();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws IOException, ServletException {
        resp.setContentType( "text/html" );
        //google
        String credential = req.getParameter( "credential" );

        //facebook
        String accessToken = req.getParameter( "accessToken" );
        if ( credential != null ) {
            try {
                GoogleIdToken idToken = verifier.verify( credential );
                if ( idToken != null ) {
                    Payload payload = idToken.getPayload();
                    // Print user identifier
                    String userId = payload.getSubject();
                    System.out.println( "User ID: " + userId );
                    // Get profile information from payload
                    System.out.println( payload );
                } else {
                    System.out.println( "Invalid ID token." );
                }
            } catch ( GeneralSecurityException e ) {
                throw new ServletException( e );
            }
        } else if ( accessToken != null ) {
            System.out.println( "fb token : " + accessToken );
            Map<String, Object> debugTokenMap = callFacebook( "https://graph.facebook.com/v13.0/debug_token?"
                                                                      + "input_token=" + accessToken
                                                                      + "&access_token=" + accessToken );
            System.out.println( debugTokenMap );

            Map<String, String> dataMap = (Map) debugTokenMap.get( "data" );
            if ( FACEBOOK_APP_ID.equals( dataMap.get( "app_id" ) ) ) {
                Map<String, String> meMap = callFacebook(
                        "https://graph.facebook.com/me?fields=first_name,last_name,email&access_token="
                                + accessToken );
                System.out.println( meMap );
                if ( !meMap.containsKey( "email" ) ) {
                    //handle no email address make sure they have a primary email set up and that it's confirmed
                }

            }
        }
    }

    private Map callFacebook( String url ) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet( url );
            CloseableHttpResponse response = httpClient.execute( request );
            try {
                HttpEntity entity = response.getEntity();
                if ( entity != null ) {
                    String responseBody = EntityUtils.toString( entity );
                    return mapper.readValue( responseBody, Map.class );
                }
                return null;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
