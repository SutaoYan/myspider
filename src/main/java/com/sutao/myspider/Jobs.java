package com.sutao.myspider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;

@Component
public class Jobs extends Crawl<Jobs.Item>{
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    try {
      this.keyword = URLEncoder.encode(URLEncoder.encode(keyword, "UTF-8"), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private String keyword="";
  private String search = "http://search.51job.com/list/020000,000000,0000,00,9,99,_keyword_,2,_pageNum_.html?" +
      "lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=03%2C04%2C05%2C06%2C07" +
      "&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

  public JobRepository getJobRepository() {
    return jobRepository;
  }

  public void setJobRepository(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Autowired
  private  JobRepository jobRepository;

  private static Comparator<Item> comparator = (Item o1, Item o2) -> {
      try {
        return Float.valueOf(o2.getSalary()).compareTo(Float.valueOf(o1.getSalary()));
      } catch (Exception e) {
        e.printStackTrace();
        return 0;
      }
  };

  protected class Item {

    public String getPublishDate() {
      return publishDate;
    }

    public void setPublishDate(String publishDate) {
      this.publishDate = publishDate;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getHref() {
      return href;
    }

    public void setHref(String href) {
      this.href = href;
    }

    public String getCompany() {
      return company;
    }

    public void setCompany(String company) {
      this.company = company;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public Item(String title, String href, String company, String location, String salary, String publishDate, String description, String keyword) {
      this.title = title;
      this.href = href;
      this.company = company;
      this.location = location;
      this.description = description;
      this.keyword = keyword;
      this.unit="万";
      if (null != salary && !salary.isEmpty()) {
        //part0 as salary, part1 as per month or year
        String [] parts = salary.split("/");
        if (parts.length ==2) {
          float nSalary = 0;
          String[] salaryWithUnitParts = parts[0].split("-");
          String salaryWithUnit = salaryWithUnitParts.length > 1 ? salaryWithUnitParts[1]: salaryWithUnitParts[0];
          int index = 0;
          for (Character c : salaryWithUnit.toCharArray()) {
            if ((c >=Character.valueOf('0') && c <= Character.valueOf('9')) || c=='.') {
              index++;
            }else {
              break;
            }
          }
          salary = salaryWithUnit.substring(0, index);
          unit = salaryWithUnit.substring(index);
          if (unit.equals("千")) {
            salary = ""+Float.valueOf(salary)/10;
            unit="万";
          } else if(unit.equals("元")) {
            salary = ""+Float.valueOf(salary)/10000;
            unit="万";
          }

          if (parts[1].equals("年")) {
            nSalary = Float.valueOf(salary)/12;
            salary = "" + nSalary;
          }
        }
      }else {
        salary = "0";
      }
      this.salary = salary;
      this.publishDate = publishDate;
    }

    private String title;
    private String href;
    private String company;
    private String location;
    private String unit;
    private String salary;
    private String publishDate;
    private String keyword;

    public String getKeyword() {
      return keyword;
    }

    public void setKeyword(String keyword) {
      this.keyword = keyword;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    private String description;

    private int hash;

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    public String getSalary() {
      try {
        Float.valueOf(salary);
        return salary;
      }catch (Exception e) {
        return "0";
      }
    }

    public void setSalary(String salary) {
      this.salary = salary;
    }

    @Override
    public int hashCode() {
      int h = hash;
//      if (h == 0 && value.length > 0) {
//        char val[] = value;
//
//        for (int i = 0; i < value.length; i++) {
//          h = 31 * h + val[i];
//        }
//        hash = h;
//      }
      return h;
    }

    @Override
    public String toString() {
      return "CurrentThread="+Thread.currentThread().getId()+
              "-Item{" +
              "title='" + title + '\'' +
              ", href='" + href + '\'' +
              ", company='" + company + '\'' +
              ", location='" + location + '\'' +
              ", salary='" + salary + '\'' +
              ", publishDate='" + publishDate + '\'' +
              '}';
    }

  }

  public Jobs () {
    super(comparator);
  }

  public Jobs (String keyword) {
    super(comparator);
    setKeyword(keyword);
  }

  private ArrayList<Item> getJobList(int page) {
    ArrayList<Item> list = new ArrayList<>();
    try {
      String tmpUrl = search.replaceAll("_keyword_", this.keyword);
      String url = tmpUrl.replaceAll("_pageNum_", ""+page);
      Document doc = Jsoup.connect(url).get();
      Elements jobList = doc.select("div#resultList").first().select("div.el");
      for(Element e : jobList) {
        if (null == e.select("p") || e.select("p").size() ==0) continue;
        String title = e.select("p.t1").first().select("a").attr("title");
        String href = e.select("p.t1").first().select("a").attr("href");
        String description = getJobDetail(href);
        String company = e.select("span.t2").first().text();
        String location = e.select("span.t3").first().text();
        String salary = e.select("span.t4").first().text();
        String publishDate = e.select("span.t5").first().text();
        Item item = new Item(title, href, company, location, salary, publishDate, description, this.keyword);
        System.out.println(item.toString());
        list.add(item);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  private String getJobDetail (String url) {
    Document doc = null;
    try {
      doc = Jsoup.connect(url).get();
      String description = doc.select("div.job_msg").first().text();
//      System.out.println(description);
      return description;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public ArrayList<Item> run(int page) {
    return getJobList(page);
  }

  @Override
  public void toHtml(ArrayList<Item> total, StringBuilder bw) {
      for(Item item: total) {
        bw.append("      <tr>\n");
        bw.append("        <td><a href="+item.getHref()+">"+item.getTitle()+"</a></td>\n");
        bw.append("        <td>"+item.getSalary()+item.getUnit()+"</td>\n");
        bw.append("        <td>"+item.getCompany()+"</td>\n");
        bw.append("        <td>"+item.getLocation()+"</td>\n");
        bw.append("        <td>"+item.getPublishDate()+"</td>\n");
        bw.append("      </tr>\n");
      }
  }

  public void save2DB() {
    for(Jobs.Item item : getTotal()) {
      if (null == jobRepository.findOneByHref(item.getHref())) {
        JobEntity jobEntity = new JobEntity();
        jobEntity.setCompany(item.getCompany());
        jobEntity.setLocation(item.getLocation());
        jobEntity.setPosition(item.getTitle());
        jobEntity.setSalary(item.getSalary());
        jobEntity.setHref(item.getHref());
        jobEntity.setDesprition(item.getDescription());
        jobEntity.setKeyword(item.getKeyword());
        jobRepository.save(jobEntity);
      }
    }
  }
}
