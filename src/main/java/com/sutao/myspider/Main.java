package com.sutao.myspider;

public class Main {
  public static void main(String[]args){
//    crawlMovie();
    crawlJobs();
  }


  private static void crawlMovie() {
    String fileName = "movieList.html";
    Crawl crawl = new Movie_dydytt(300);
    crawl.setThreadCount(50);
    crawl.crawl(fileName);
  }

  private static void crawlJobs() {
//    String fileName = "jobList.html";
    String keyword= "财务";
    String fileName = keyword +".html";
    new Jobs(keyword,300).crawl(fileName);
  }
}
