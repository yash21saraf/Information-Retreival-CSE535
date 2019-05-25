def processAdd(cmd):
  import re
  import time
  doc = cmd.solrDoc

  emoji_pattern = re.compile("["
                       u"\U0001F600-\U0001F64F"  # emoticons
                       u"\U0001F300-\U0001F5FF"  # symbols & pictographs
                       u"\U0001F680-\U0001F6FF"  # transport & map symbols
                       u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
                       u"\U00002702-\U000027B0"
                       u"\U000024C2-\U0001F251"
                       u"\U0001f926-\U0001f937"
                       u"\U0001F900-\U0001F992"
                    
                       u"\u200d"
                       u"\u2640-\u2642"
                       u"\u2600-\u26FF"
                       "]+", flags=re.UNICODE)
    
  created_at = doc.getFieldValue("created_at")
  aaa = time.strftime('%Y-%m-%dT%H:00:00Z', time.strptime(created_at,'%a %b %d %H:%M:%S +0000 %Y'))
  doc.setField("tweet_date", aaa);
  
  field_names = doc.getFieldNames().toArray();
  for i in field_names:
       if i == "text_en":
         text_ena = doc.getFieldValue("text_en")
         emo_en = emoji_pattern.sub(r'', text_ena) # no emoji
         emo = emoji_pattern.findall(text_ena)
         str1 = ' '.join(e for e in emo)
         if str1:
           doc.setField("tweet_emoticons", str1);
         doc.setField("text_en", emo_en);
       if i == "text_es":
         text_esa = doc.getFieldValue("text_es")
         emo_es = emoji_pattern.sub(r'', text_esa) # no emoji
         emo = emoji_pattern.findall(text_esa)
         str1 = ' '.join(e for e in emo)
         if str1:
           doc.setField("tweet_emoticons", str1);
         doc.setField("text_es", emo_es);
       if i == "text_fr":
         text_fra = doc.getFieldValue("text_fr")
         emo_fr = emoji_pattern.sub(r'', text_fra) # no emoji
         emo = emoji_pattern.findall(text_fra)
         str1 = ' '.join(e for e in emo)
         if str1:
           doc.setField("tweet_emoticons", str1);
         doc.setField("text_fr", emo_fr);
       if i == "text_th":
         text_tha = doc.getFieldValue("text_th")
         emo_th = emoji_pattern.sub(r'', text_tha) # no emoji
         emo = emoji_pattern.findall(text_tha)
         str1 = ' '.join(e for e in emo)
         if str1:
           doc.setField("tweet_emoticons", str1);
         doc.setField("text_th", emo_th);
       if i == "text_hi":
         text_hia = doc.getFieldValue("text_hi")
         emo_hi = emoji_pattern.sub(r'', text_hia) # no emoji
         emo = emoji_pattern.findall(text_hia)
         str1 = ' '.join(e for e in emo)
         if str1:
           doc.setField("tweet_emoticons", str1);
         doc.setField("text_hi", emo_hi);
         

def processDelete(cmd):
    logger.info("update-script#processDelete")

def processMergeIndexes(cmd):
    logger.info("update-script#processMergeIndexes")

def processCommit(cmd):
    logger.info("update-script#processCommit")

def processRollback(cmd):
    logger.info("update-script#processRollback")

def finish():
    logger.info("update-script#finish")
