package com.sutao.myspider.crawl.job;

import com.sutao.myspider.crawl.AbstractCrawl;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Component
@NoArgsConstructor
public class JobCrawlExecutor extends AbstractCrawl<JobEntity> {

    @Autowired
    private JobRepository jobRepository;

    private String keyword = "";
    private String search = "https://search.51job.com/list/020000,000000,0000,00,9,99,_keyword_,2,_pageNum_.html?" +
            "lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=03%2C04%2C05%2C06%2C07" +
            "&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    public void setKeyword(String keyword) {
        try {
            this.keyword = URLEncoder.encode(URLEncoder.encode(keyword, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static Comparator<JobEntity> comparator = (JobEntity o1, JobEntity o2) -> {
        try {
            return Float.valueOf(o2.getSalary()).compareTo(Float.valueOf(o1.getSalary()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    };


    public JobCrawlExecutor(String keyword, JobRepository jobRepository) {
        setComparator(comparator);
        this.keyword = URLEncoder.encode(keyword);
        this.jobRepository = jobRepository;
    }

    private ArrayList<JobEntity> getJobList(int page) {
        ArrayList<JobEntity> list = new ArrayList<>();
        try {
            String tmpUrl = search.replaceAll("_keyword_", this.keyword);
            String url = tmpUrl.replaceAll("_pageNum_", "" + page);
            Document doc = Jsoup.parse(String.valueOf(downloadHtml(url, "gbk")));
            Elements jobList = doc.select("div#resultList").first().select("div.el");
            for (Element e : jobList) {
                if (null == e.select("p") || e.select("p").size() == 0) continue;
                String title = e.select("p.t1").first().select("a").attr("title");
                String href = e.select("p.t1").first().select("a").attr("href");
                String description = getJobDetail(href);
                String company = e.select("span.t2").first().text();
                String location = e.select("span.t3").first().text();
                String salary = e.select("span.t4").first().text();
                String publishDate = e.select("span.t5").first().text();
                JobEntity jobEntity = new JobEntity(title, href, company, location, salary, publishDate, description, this.keyword);
                System.out.println(jobEntity.toString());
                list.add(jobEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String getJobDetail(String url) {
        Document doc = Jsoup.parse(String.valueOf(downloadHtml(url, "gb2312")));
        String description = doc.select("div.job_msg").first().text();
        return description;
    }

    @Override
    public ArrayList<JobEntity> run(int page) {
        ArrayList<JobEntity> pageJobEntities = getJobList(page);
        save2DB(pageJobEntities);
        return pageJobEntities;
    }

    public void save2DB(List<JobEntity> pageJobEntities) {
        for (JobEntity jobEntity : pageJobEntities) {
            if (null == jobRepository.findOneByHref(jobEntity.getHref())) {
                try {
                    jobRepository.save(jobEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void toHtmlImpl(ArrayList<JobEntity> total, StringBuilder bw) {
        for (JobEntity jobEntity : total) {
            bw.append("      <tr>\n");
            bw.append("        <td><a href=" + jobEntity.getHref() + ">" + jobEntity.getTitle() + "</a></td>\n");
            bw.append("        <td>" + jobEntity.getSalary() + jobEntity.getUnit() + "</td>\n");
            bw.append("        <td>" + jobEntity.getCompany() + "</td>\n");
            bw.append("        <td>" + jobEntity.getLocation() + "</td>\n");
            bw.append("        <td>" + jobEntity.getPublishDate() + "</td>\n");
            bw.append("      </tr>\n");
        }
    }
}
