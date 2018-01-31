package com.sutao.myspider.crawl;

import com.sutao.myspider.repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class JobCrawlBuilder implements ICrawlBuilder{
  @Autowired
  JobRepository jobRepository;

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

  public JobCrawlBuilder() {
  }

  public void main(String[]args){
    new JobCrawlBuilder().build();
  }

  @Override
  public String build() {
    System.out.println("start crawl jobs...");
    AbstractCrawl jobCrawl = new JobCrawl(keyword, jobRepository);
    String fileName = keyword +".html";
    jobCrawl.setMaxPage(maxPage);
    jobCrawl.setThreadCount(threadCount);
    jobCrawl.crawl();
    jobCrawl.save2DB();
    return jobCrawl.toHtml();
  }
}
