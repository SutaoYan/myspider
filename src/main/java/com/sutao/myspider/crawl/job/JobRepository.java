package com.sutao.myspider.crawl.job;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<JobEntity, String> {
    JobEntity findOneByHref(String s);
}


