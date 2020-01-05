package com.sutao.myspider.crawl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobResource {

    @Autowired
    private JobCrawlBuilder jobCrawlBuilder;

    @RequestMapping("/job/{keyword}")
    @ResponseBody
    String crawlJob(@PathVariable String keyword, @RequestParam Integer pages) {
        return jobCrawlBuilder.crawl(keyword, pages, Runtime.getRuntime().availableProcessors());
    }
}
