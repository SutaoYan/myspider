package com.sutao.myspider.crawl;

import java.util.stream.Stream;

public class MovieCrawlBuilder implements ICrawlBuilder{
  private String keyword;
  private int threadCount;
  private int maxPage;

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public int getMaxPage() {
    return maxPage;
  }

  public void setMaxPage(int maxPage) {
    this.maxPage = maxPage;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
  @Override
  public String build() {
    System.out.println("start crawl movies...");
    AbstractCrawl crawl = new MovieCrawl();
    String fileName = keyword +".html";
    crawl.setThreadCount(threadCount);
    crawl.setMaxPage(maxPage);
    crawl.crawl();
    crawl.save2DB();
    return crawl.toHtml();
  }

  public void main(String[]args){
    new MovieCrawlBuilder().build();
  }
}
