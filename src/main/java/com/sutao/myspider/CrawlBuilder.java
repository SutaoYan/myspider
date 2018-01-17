package com.sutao.myspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CrawlBuilder {
  private String keyword;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getMaxPage() {
    return maxPage;
  }

  public void setMaxPage(int maxPage) {
    this.maxPage = maxPage;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  private String type="";
  private int maxPage = 100;
  private int threadCount = 10;

  public Jobs getJobs() {
    return jobs;
  }

  public void setJobs(Jobs jobs) {
    this.jobs = jobs;
  }

  @Autowired Jobs jobs;

  public CrawlBuilder () {

  }

  public CrawlBuilder(String keyword, int maxPage, int threadCount) {
    this.keyword = keyword;
    this.maxPage = maxPage;
    this.threadCount = threadCount;
  }

  public String crawlJob() {
    System.out.println("start crawl jobs...");
    jobs.setKeyword(keyword);
    String fileName = keyword +".html";
    jobs.setThreadCount(threadCount);
    jobs.setMaxPage(maxPage);
    String html = jobs.crawl();
    jobs.save2DB();
    return html;
  }

  public String crawlMovie() {
    System.out.println("start crawl movies...");
    Crawl crawl = new Movie_dydytt();
    String fileName = keyword +".html";
    crawl.setThreadCount(threadCount);
    crawl.setMaxPage(maxPage);
    return crawl.crawl();
  }

  public void main(String[]args){
    Stream.of(args).forEach(System.out::println);
    if (args.length >= 2) {
      type = args[0];
      keyword = args[1];
    }

    if (args.length >=3) {
      maxPage = Integer.valueOf(args[2]);
    }

    if (args.length >=4) {
      threadCount = Integer.valueOf(args[3]);
    }

    Crawl crawl = null;
    switch (type) {
      case "job":
        System.out.println("start crawl jobs...");
        crawl = new Jobs(keyword);
        break;
      case "movie":
        System.out.println("start crawl movies...");
        crawl = new Movie_dydytt();
        break;
      default:
        System.out.println("nothing is done.");
        System.out.println("usage:\n" +
                "java -jar myspider-1.0-SNAPSHOT.jar type keyword [max page] [thread count] \n" +
                " type: crawl job or movie list\n" +
                " keyword: it's search key word for job while file name for both job and movie\n" +
                " max page: how many result pages will be crawled, by default it's 100\n" +
                " thread count: how many threads will be started to crawl pages, by default it's 10\n");
        break;
    }

    if (null != crawl) {
      String fileName = keyword +".html";
      crawl.setThreadCount(threadCount);
      crawl.setMaxPage(maxPage);
      crawl.crawl();
    }
  }
}
