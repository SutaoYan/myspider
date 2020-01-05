package com.sutao.myspider.crawl.job;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class JobEntity {
    public JobEntity(String title, String href, String company, String location, String salary, String publishDate, String description, String keyword) {
        this.title = title;
        this.href = href;
        this.company = company;
        this.location = location;
        this.description = description;
        this.keyword = keyword;
        this.unit = "万";
        if (null != salary && !salary.isEmpty()) {
            //part0 as salary, part1 as per month or year
            String[] parts = salary.split("/");
            if (parts.length == 2) {
                float nSalary = 0;
                String[] salaryWithUnitParts = parts[0].split("-");
                String salaryWithUnit = salaryWithUnitParts.length > 1 ? salaryWithUnitParts[1] : salaryWithUnitParts[0];
                int index = 0;
                for (Character c : salaryWithUnit.toCharArray()) {
                    if ((c >= Character.valueOf('0') && c <= Character.valueOf('9')) || c == '.') {
                        index++;
                    } else {
                        break;
                    }
                }
                salary = salaryWithUnit.substring(0, index);
                unit = salaryWithUnit.substring(index);
                if (unit.equals("千")) {
                    salary = "" + Float.valueOf(salary) / 10;
                    unit = "万";
                } else if (unit.equals("元")) {
                    salary = "" + Float.valueOf(salary) / 10000;
                    unit = "万";
                }

                if (parts[1].equals("年")) {
                    nSalary = Float.valueOf(salary) / 12;
                    salary = "" + nSalary;
                }
            }
        } else {
            salary = "0";
        }
        this.salary = salary;
        this.publishDate = publishDate;
        this.createAt = new Date();
    }

    @Id
    private String id;
    private String title;
    private String href;
    private String company;
    private String location;
    private String unit;
    private String salary;
    private String publishDate;
    private String keyword;
    private String description;
    private Date createAt;

    public String getSalary() {
        try {
            Float.valueOf(salary);
            return salary;
        } catch (Exception e) {
            return "0";
        }
    }

    @Override
    public String toString() {
        return "CurrentThread=" + Thread.currentThread().getId() +
                "-JobEntity{" +
                "title='" + title + '\'' +
                ", href='" + href + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", salary='" + salary + '\'' +
                ", publishDate='" + publishDate + '\'' +
                '}';
    }
}
