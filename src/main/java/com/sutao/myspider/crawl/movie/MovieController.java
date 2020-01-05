package com.sutao.myspider.crawl.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired private MovieCrawlBuilder builder;

    @RequestMapping("/fetch")
    public String fetch(@RequestParam Integer threadCount, @RequestParam Integer maxPage) {
        return builder.crawl(threadCount, maxPage, null);
    }
}
