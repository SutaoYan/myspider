package com.sutao.myspider.crawl.job;

import com.sutao.myspider.crawl.AbstractCrawl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@NoArgsConstructor
@AllArgsConstructor
public class JobCrawlBuilder {
    @Autowired
    JobRepository jobRepository;

    public String crawl(String keyword, int maxPage, int threadCount) {
        System.out.println("start crawl jobs...");
        AbstractCrawl jobCrawl = new JobCrawlExecutor(keyword, jobRepository);
//    String fileName = keyword +".html";
        jobCrawl.setMaxPage(maxPage);
        jobCrawl.setThreadCount(threadCount);
        jobCrawl.crawl();
        return jobCrawl.toHtml();
    }
}
