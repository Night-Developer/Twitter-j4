package com.example.TwitterNight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    static String TWITTER_CONSUMER_KEY = "api key";
    static String TWITTER_CONSUMER_SECRET = "api secret";

    static final String TWITTER_CALLBACK_URL = "oauth://url-example";

    private static RequestToken requestToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri data = getIntent().getData();

        if(data != null){
            String token = data.getQueryParameter("oauth_token");
            String oauthVerified = data.getQueryParameter("oauth_verifier"); //

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

            Configuration conf = builder.build();

            TwitterFactory factory = new TwitterFactory(conf);

            Twitter twitter = factory.getInstance();

            RequestToken r = requestToken;

            if(twitter != null){
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(r, oauthVerified);

                    this.setTitle("Twitter " + accessToken.getScreenName().toString());
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }else{
            loginTwitter();
        }
    }

    public void loginTwitter(){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
