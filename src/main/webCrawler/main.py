from urllib.parse import urlparse
import Crawler
import Database
import pandas as pd
import requests
from bs4 import BeautifulSoup
import urllib
import csv
import validators

urls = [""]
filesName = [""]


def crawl():
    for i in range(len(urls)):
        crawler = Crawler.Crawler(urls[i], filesName[i])
        crawler.start()


def readFile(fileName):
    links = pd.read_csv("files/" + fileName, encoding='utf-8')
    return links


def insert():
    for name in filesName:
        links = readFile(name)
        i = 0
        for link in links.link:
            print(i, "  ", link)
            i = i + 1
            # insertResource(link)
            getContent(link)


def insertResource(link, title, body):
    try:
        if not title is None and not body is None:
            Database.insert_table('resources', link, title, body)

    except Exception as e:
        print(e)


def getContent(url):
    try:
        req = urllib.request.urlopen(url)
        response = requests.get(url)

        if str(url).endswith('png') or str(url).endswith('jpg') or str(url).endswith('jpeg') or str(url).endswith(
                'JPG'):
            title = urlparse(url).path.split("/")[-1].strip()
            body = url
            insertImage(url, title, body)
        elif not str(url).endswith('pdf') and not str(url).endswith('doc') and not str(url).endswith(
                'docx') and not str(url).endswith('PDF'):
            soup = BeautifulSoup(response.content, "html.parser", from_encoding=req.info().get_param('charset'))
            body = " ".join(soup.text.split())
            title = soup.find('title').text
            insertResource(url, title, body)

            images = soup.find_all('img')
            for link in images:
                link = link.get('src')
                if validators.url(str(link)):
                    insertImage(url, urlparse(link).path.split("/")[-1].strip(), link)


    except Exception as e:
        print(e)
        print("error ", url)
        return ""


def insertImage(url, title, body):
    if not title is None and not body is None:
        Database.insert_table("images", url, title, body)

# if __name__ == "__main__":
# crawl()
insert()
