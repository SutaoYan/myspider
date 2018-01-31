package com.sutao.myspider.resource;

import com.sutao.myspider.crawl.JobCrawlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobResource {
  @Autowired
  JobCrawlBuilder crawlBuilder;

  @RequestMapping("/job/{keyword}")
  @ResponseBody
  String crawlJob(@PathVariable String keyword){
    crawlBuilder.setMaxPage(100);
    crawlBuilder.setThreadCount(50);
    crawlBuilder.setKeyword(keyword);
    return crawlBuilder.build();
  }
}
