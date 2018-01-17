package com.sutao.myspider;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface JobRepository extends MongoRepository<JobEntity, String> {
  JobEntity findOneByCompany(String s);
  JobEntity findOneByHref(String s);
}


