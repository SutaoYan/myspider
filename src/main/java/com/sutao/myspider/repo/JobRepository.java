package com.sutao.myspider.repo;

import com.sutao.myspider.entities.JobEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<JobEntity, String> {
  JobEntity findOneByHref(String s);
}


