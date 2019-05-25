#!/usr/bin/env python
# encoding: utf-8

import tweepy
import json
import time
import datetime

#Twitter API credentials
consumer_key = ""
consumer_secret = ""
access_key = ""
access_secret = ""

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)
api = tweepy.API(auth, wait_on_rate_limit=True, wait_on_rate_limit_notify=True)
#refer http://docs.tweepy.org/en/v3.2.0/api.html#API
#tells tweepy.API to automatically wait for rate limits to replenish

searchquery1 = ["\"Social unrest\"OR\"Riots\"OR\"terrorism\"OR\"civil unrest\"-RT",
"\"Environment\"OR\"Nature\"OR\"recycle reuse\"OR\"save nature\" -RT",
"\"crime\"OR\"murder\"OR\"killed\"OR\"tortured\"OR\"raped\"-RT",
"\"politics\"OR\"donald trump\"OR\"\hillary clinton\"OR\"white house\"OR\"US government\"-RT",
"\"infrastructure\"OR\"Construct bridge\"OR\"construct buildings\"OR\"USA development\"-RT"]

searchquery2 = ["\"Social unrest\"OR\"Riots\"OR\"terrorism\"OR\"civil unrest\"-RT"
,"\"Environment\"OR\"Nature\"OR\"recycle reuse\"OR\"save nature\"-RT"
,"\"crime\"OR\"murder\"OR\"killed\"OR\"tortured\"OR\"raped\"-RT"
,"\"politics\"OR\"Bangkok Metropolitan Administration\"OR\"\King Vajiralongkorn\"OR\"Rama X\"OR\"monarchy thailand\"-RT"
,"\"infrastructure\"OR\"Construct bridge\"OR\"construct buildings\"OR\"bangkok development\"-RT"]

searchquery3 = ["\"Social unrest\"OR\"Riots\"OR\"terrorism\"OR\"civil unrest\" -RT"
,"\"Environment\"OR\"Nature\"OR\"recycle reuse\"OR\"save nature\" -RT"
,"\"crime\"OR\"murder\"OR\"killed\"OR\"tortured\"OR\"raped\"-RT"
,"\"politics\"OR\"Institutional Revolutionary Party\"OR\"\Claudia Ruiz Massieu\"OR\"National Action Party\"-RT"
,"\"infrastructure\"OR\"Construct bridge\"OR\"construct buildings\"OR\"mexico development\"-RT" ]


langu=["en","es","th","fr","hi"]
count = 0
errorCount=0

for i in searchquery1:
    for j in langu:
            count = 0
            errorCount=0
            alpha = i[1:5]+"-"+j+"-"+i[-3:-1]+"-new_york"+".json"
            file = open(alpha, 'wb')
            users =tweepy.Cursor(api.search,q=i,lang =j,geocode="40.730610,-73.935242,40mi").items()
            if (i[1:5] == "Envi"):
                topic = "environment"
            if (i[1:5] == "crim"):
                topic = "crime"
            if (i[1:5] == "poli"):
                topic = "politics"
            if (i[1:5] == "Soci"):
                topic = "social unrest"
            if (i[1:5] == "infr"):
                topic = "infra"

            while True:
                try:
                    user = next(users)
                    user._json['city'] = "nyc"
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z', time.strptime(user._json['created_at'], '%a %b %d %H:%M:%S +0000 %Y'))
                    count += 1

                except tweepy.TweepError:
                    print "sleeping...."
                    time.sleep(60 * 16)
                    user = next(users)
                    user._json['city'] = 'nyc'
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z',
                                                             time.strptime(user._json['created_at'],
                                                                           '%a %b %d %H:%M:%S +0000 %Y'))

                except StopIteration:
                    break
                try:
                    print "Writing to JSON tweet number:" + str(count)
                    json.dump(user._json, file, sort_keys=True, indent=4)

                except UnicodeEncodeError:
                    errorCount += 1
                    print "UnicodeEncodeError,errorCount =" + str(errorCount)

            print "completed, errorCount =" + str(errorCount) + " total tweets=" + str(count)

print "New york done"
i = 0
j = 0
for i in searchquery2:
    for j in langu:
            count = 0
            errorCount=0
            alpha = i[1:5]+"-"+j+"-"+i[-3:-1]+"-bangkok"+".json"
            file = open(alpha, 'wb')
            users =tweepy.Cursor(api.search,q=i,lang =j,geocode="13.736717,100.523186,40mi").items()
            if (i[1:5] == "Envi"):
                topic = "environment"
            if (i[1:5] == "crim"):
                topic = "crime"
            if (i[1:5] == "poli"):
                topic = "politics"
            if (i[1:5] == "Soci"):
                topic = "social unrest"
            if (i[1:5] == "infr"):
                topic = "infra"
            while True:
                try:
                    user = next(users)
                    user._json['city'] = "bangkok"
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z',
                                                             time.strptime(user._json['created_at'],
                                                                           '%a %b %d %H:%M:%S +0000 %Y'))

                    count += 1

                except tweepy.TweepError:
                    print "sleeping...."
                    time.sleep(60 * 16)
                    user = next(users)
                    user._json['city'] = 'bangkok'
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z', time.strptime(user._json['created_at'],
                                                                                             '%a %b %d %H:%M:%S +0000 %Y'))

                except StopIteration:
                    break
                try:
                    print "Writing to JSON tweet number:" + str(count)
                    json.dump(user._json, file, sort_keys=True, indent=4)

                except UnicodeEncodeError:
                    errorCount += 1
                    print "UnicodeEncodeError,errorCount =" + str(errorCount)

            print "completed, errorCount =" + str(errorCount) + " total tweets=" + str(count)
print "bangkok done"
i = 0
j = 0
for i in searchquery3:
    for j in langu:
            count = 0
            errorCount=0
            alpha = i[1:5]+"-"+j+"-"+i[-3:-1]+"-mexico"+".json"
            file = open(alpha, 'wb')
            users =tweepy.Cursor(api.search,q=i,lang =j,geocode="19.432608,-99.133209,40mi").items()
            if (i[1:5] == "Envi"):
                topic = "environment"
            if (i[1:5] == "crim"):
                topic = "crime"
            if (i[1:5] == "poli"):
                topic = "politics"
            if (i[1:5] == "Soci"):
                topic = "social unrest"
            if (i[1:5] == "infr"):
                topic = "infra"
            while True:
                try:
                    user = next(users)
                    user._json['city'] = "mexico"
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z',
                                                             time.strptime(user._json['created_at'],
                                                                           '%a %b %d %H:%M:%S +0000 %Y'))

                    count += 1

                except tweepy.TweepError:
                    print "sleeping...."
                    time.sleep(60 * 16)
                    user = next(users)
                    user._json['city'] = 'mexico'
                    user._json['topic'] = topic
                    user._json['tweet_lang'] = j
                    user._json['tweet_date'] = time.strftime('%Y-%m-%dT%H:00:00Z',
                                                             time.strptime(user._json['created_at'],
                                                                           '%a %b %d %H:%M:%S +0000 %Y'))

                except StopIteration:
                    break
                try:
                    print "Writing to JSON tweet number:" + str(count)
                    json.dump(user._json, file, sort_keys=True, indent=4)

                except UnicodeEncodeError:
                    errorCount += 1
                    print "UnicodeEncodeError,errorCount =" + str(errorCount)

            print "completed, errorCount =" + str(errorCount) + " total tweets=" + str(count)
print "all done"
