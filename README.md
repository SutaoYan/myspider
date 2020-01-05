# myspider

usage:

java -jar myspider-1.0-SNAPSHOT.jar type keyword [max page] [thread count]

type: crawl job or movie list

keyword: it's search key word for job while file name for both job and movie

max page: how many result pages will be crawled, by default it's 100

thread count: how many threads will be started to crawl pages, by default it's 10

## startup mongodb
sudo mongod

the db file is under /data/db