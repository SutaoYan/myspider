package com.sutao.myspider.crawl.movie;

import com.sutao.myspider.crawl.AbstractCrawl;
import org.springframework.stereotype.Component;

@Component
public class MovieCrawlBuilder {

    public String crawl(Integer threadCount, Integer maxPage, String keyword) {
        System.out.println("start crawl movies...");
        AbstractCrawl crawl = new MovieCrawl();
//        String fileName = keyword + ".html";
        crawl.setThreadCount(threadCount);
        crawl.setMaxPage(maxPage);
        crawl.crawl();
        return crawl.toHtml();
    }

}
