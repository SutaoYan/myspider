package com.sutao.myspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableMongoRepositories
@EnableAutoConfiguration
public class SpiderController {

  @Autowired
  private JobRepository jobRepository;

  @Autowired CrawlBuilder crawlBuilder;

  @RequestMapping("/")
  @ResponseBody
  String home() {
    return "Hello World!";
  }

  @RequestMapping("/job/{keyword}")
  @ResponseBody
  String crawlJob(@PathVariable String keyword){
//    JobEntity jobEntity = new JobEntity();
//    jobEntity.setCompany("HP");
//    jobEntity.setLocation("shanghai");
//    jobEntity.setPosition("engineer");
//    jobEntity.setSalary("5W");
//    jobRepository.save(jobEntity);
    crawlBuilder.setMaxPage(100);
    crawlBuilder.setThreadCount(50);
    crawlBuilder.setKeyword(keyword);
    return crawlBuilder.crawlJob();
  }

  public static void main(String[] args) throws Exception {
//    MongodStarter starter = MongodStarter.getDefaultInstance();
//
//    String bindIp = "localhost";
//    int port = 27017;
//    IMongodConfig mongodConfig = new MongodConfigBuilder()
//        .version(Version.Main.PRODUCTION)
//        .net(new Net(bindIp, port, Network.localhostIsIPv6()))
//        .build();
//
//    MongodExecutable mongodExecutable = null;
//    try {
//      mongodExecutable = starter.prepare(mongodConfig);
//      MongodProcess mongod = mongodExecutable.start();
//
////      MongoClient mongo = new MongoClient(bindIp, port);
////      DB db = mongo.getDB("test");
////      DBCollection col = db.createCollection("testCol", new BasicDBObject());
////      col.save(new BasicDBObject("testDoc", new Date()));
//
//    }catch (Exception e) {
//      e.printStackTrace();
//    }
    SpringApplication.run(SpiderController.class, args);
//    if (mongodExecutable != null)
//      mongodExecutable.stop();
  }
}
