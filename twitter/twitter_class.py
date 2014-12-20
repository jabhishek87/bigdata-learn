# !/usr/bin/python

import tweepy

# Go to http://apps.twitter.com and create an app.
# The consumer key and secret will be generated for you after
consumer_key="kp0g4dyIKuLFUpsOkncMWg"
consumer_secret="wLkcTQtrK9FJ5mN4gUj1Z8vmo9Ca2wVlrbJeZ7pbU"

# After the step above, you will be redirected to your app's page.
# Create an access token under the the "Your access token" section
access_token="102034996-byWD21oLEmNLMloktKvyd7mKqFlyhDhaXwD19pkq"
access_token_secret="wm1RpJ5kDeG3iiPiUtt1FUkt01q8GdqjIdQxefaL7I"

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth)

public_tweets = api.home_timeline()
for tweet in public_tweets:
    print tweet.text

print api.get_user('twitter')
