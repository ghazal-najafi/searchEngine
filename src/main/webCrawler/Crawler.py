from urllib.parse import urlparse
import requests
from bs4 import BeautifulSoup
import validators


class Crawler(object):
    def __init__(self, seedURL, fileName):
        self.seedURL = seedURL
        self.fileName = 'files/' + fileName
        self.visited = set()
        self.baseURL = urlparse(seedURL).netloc

    def getHTML(self, url):
        try:
            html = requests.get(url)
        except Exception as e:
            print(e)
            return ""
        soup = BeautifulSoup(html.content, "html.parser")
        # soup = BeautifulSoup(response, "html.parser", from_encoding=response.info().get_param('charset'),exclude_encodings='utf-8')
        linksTag = soup.find_all("a", href=True)
        links = set()
        for link in linksTag:
            links.add(link.get('href'))
        return links

    def getLinks(self, url):
        self.visited.add(url)
        links = self.getHTML(url)
        parsed = urlparse(url)
        base = f"{parsed.scheme}://{parsed.netloc}"

        urls = set()
        for i, link in enumerate(links):
            if validators.url(str(link)) == True and self.baseURL in link:
                urls.add(link)
            elif not link is None and not urlparse(link).netloc:
                link = base + link
                if validators.url(link) == True:
                    urls.add(link)
            else:
                print("invalid : ", link)

        urlSet = set(filter(lambda x: 'mailto' not in x, urls))
        for url in urlSet:
            if url.endswith("/"):
                url = url.rstrip(url[-1])
            self.visited.add(url)

        return urlSet

    def crawl(self, url):
        links = self.getLinks(url)
        for link in links:
            linkStep2 = self.getLinks(link)
            for link2 in linkStep2:
                if link2 in self.visited:
                    continue
                else:
                    self.getLinks(link2)
        self.print()

    def start(self):
        self.crawl(self.seedURL)

    def print(self):
        file = open(self.fileName, mode='w', encoding='utf-8')
        file.write("link")
        file.write("\n")
        for link in self.visited:
            try:
                file.write(link)
                file.write("\n")
            except Exception as e:
                print(e)
                return ""
        file.close()
