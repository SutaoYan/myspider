package com.sutao.myspider;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
public class JobEntity {

  @Id
  public String id;

  public String position;
  public String salary;

  public String href;
  public String desprition;
  public String company;
  public String location;
  public String keyword;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
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

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getDesprition() {
    return desprition;
  }

  public void setDesprition(String desprition) {
    this.desprition = desprition;
  }

  public JobEntity() {}


}
