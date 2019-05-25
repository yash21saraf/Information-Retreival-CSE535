# -*- coding: utf-8 -*-
import json
import urllib.request
from urllib.parse import quote
import re
from googletrans import Translator
translator = Translator()

languages = ['en', 'ru', 'de']
fname = "testqueries.txt"
outfn = 'output.txt'
IRModel='default'
qid = ''


def translation():
    querydict = []
    with open(fname, encoding = "utf8") as f:
        contents = f.readlines()

    for content in contents:
        query_en = ""
        query_de = ""
        query_ru = ""
        content.replace("\n", "")
        qid = content[:3]
        query = content[4:-1]
        print(query)
        # querylang = gs.detect(query)
        querylang = translator.detect([query])[0].lang
        print(querylang)

        if(querylang == "en"):
            query_en = query
        if (querylang == "de"):
            query_de = query
        if (querylang == "ru"):
            query_ru = query

        for lang in languages:
            if lang != querylang :
                translations = translator.translate(query, dest=lang).text
                # translations = gs.translate(query, lang)
                if (lang == "en"):
                    query_en = translations
                if (lang == "de"):
                    query_de = translations
                if (lang == "ru"):
                    query_ru = translations
        json_var = { "qid" : qid,
                     "text" : query,
                     "lang" : querylang,
                     "text_en" : query_en,
                     "text_de": query_de,
                     "text_ru": query_ru
                     }
        querydict.append(json_var)
        print(query)
        print(query_en)
        print(query_ru)
        print(query_de)
        print("-----------------------------------------------------------------------------")

    with open('data.json', 'w') as outfile:
        json.dump(querydict, outfile,ensure_ascii=False)

def querysolr():
    core = "dfr"
    with open('train_queries.json') as f:
        data = json.load(f)
    outf = open(outfn, 'r+')
    outf.truncate(0)
    for content in data:
        query_en = ""
        query_de = ""
        query_ru = ""
        qid = content['qid']
        query = content['text']
        querylang = content['lang']
        hashtags = re.findall(r"#(\w+)", query)

        query_en = content['text_en']
        query_de = content['text_de']
        query_ru = content['text_ru']
        query = query_en + " " + query_de + " " + query_ru
        query = query.replace(":", "\:")
        query = quote(query)
        query = query.replace(" ", "%20")

        query_en = query_en.replace(":", "\:")
        query_en = quote(query_en)
        query_en = query_en.replace(" ", "%20")

        query_de = query_de.replace(":", "\:")
        query_de = quote(query_de)
        query_de = query_de.replace(" ", "%20")

        query_ru = query_ru.replace(":", "\:")
        query_ru = quote(query_ru)
        query_ru = query_ru.replace(" ", "%20")

        hashtags = str(hashtags)
        hashtags = hashtags.replace("[", "")
        hashtags = hashtags.replace("]", "")
        hashtags = quote(hashtags)
        hashtags = hashtags.replace(" ", "")
        if(core == "bm25"):
            if(querylang == "en"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "text_en:" + query_en +  '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.1%20+text_en^1.8%20+%20text_de^2.0%20+%20text_ru^1.9&rows=20&wt=json'
            if(querylang == "de"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.1%20+text_en^2.0%20+%20text_de^3.0%20+%20text_ru^2.0&rows=20&wt=json'
            if(querylang == "ru"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.1%20+text_en^2.0%20+%20text_de^2%20+%20text_ru^3.0&rows=20&wt=json'
        if (core == "vsm"):
            if (querylang == "en"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "text_en:" + query_en + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^0.6%20+text_en^2.0%20+%20text_de^2.0%20+%20text_ru^2&rows=20&wt=json'
            if (querylang == "de"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^0.6%20+text_en^2%20+%20text_de^2.7%20+%20text_ru^2.0&rows=20&wt=json'
            if (querylang == "ru"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^0.6%20+text_en^2%20+%20text_de^2%20+%20text_ru^2.7&rows=20&wt=json'
        if (core == "dfr"):
            if (querylang == "en"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "text_en:" + query_en + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.5%20+text_en^1.9%20+%20text_de^2.0%20+%20text_ru^2.1&rows=20&wt=json'
            if (querylang == "de"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.5%20+text_en^2.0%20+%20text_de^2.6%20+%20text_ru^2.0&rows=20&wt=json'
            if (querylang == "ru"):
                inurl = 'http://localhost:8983/solr/' + core + '/select?fl=id,score&indent=on&q=' + "tweet_hashtags:" + hashtags + "%20OR%20" + "" + query + '&defType=dismax&qf=tweet_urls^0.0%20+tweet_hashtags^2.5%20+text_en^2.0%20+%20text_de^2%20+%20text_ru^2.6&rows=20&wt=json'

        data = urllib.request.urlopen(inurl)
        docs = json.load(data)['response']['docs']
        rank = 1
        for doc in docs:
            outf.write(qid + ' ' + 'Q0' + ' ' + str(doc['id']) + ' ' + str(rank) + ' ' + str(doc['score']) + ' ' + IRModel + '\n')
            rank += 1
    outf.close()

# translation()
querysolr()
