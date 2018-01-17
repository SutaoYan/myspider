package com.sutao.myspider;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public abstract class Crawl<E> {
  protected int maxPage = 50;
  private Comparator<E> comparator;
  ArrayList<E> total = new ArrayList<>();

  protected int threadCount = 10;

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public int getMaxPage() {
    return maxPage;
  }

  public void setMaxPage(int maxPage) {
    this.maxPage = maxPage;
  }

  public abstract ArrayList<E> run (int page);
  public abstract void toHtml(ArrayList<E> total, StringBuilder builder );

  public Crawl(Comparator<E> comparator, int maxPage) {
    this.comparator = comparator;
    this.maxPage = maxPage;
  }

  public Crawl(Comparator<E> comparator) {
    this.comparator = comparator;
  }

  public Crawl () {

  }

  public String crawl() {
    total.clear();
    ExecutorService es = Executors.newWorkStealingPool(threadCount);
    List<Future<ArrayList<E>>> results = Stream.iterate(1, page -> page+1).limit(maxPage).map(page ->es.submit(()->run(page))).collect(Collectors.toList());
    results.forEach(item -> {
      try {
        total.addAll(item.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    });

    total.sort(comparator);
    return toHtmlAbstract(total);
  }

  public ArrayList<E> getTotal() {
    return total;
  }

  public String toHtmlAbstract(ArrayList<E> total ) {
    StringBuilder buffer = new StringBuilder();

    buffer.append("<!DOCTYPE html>\n" +
              "<html>\n" +
              "<head lang=\"en\">\n" +
              "    <meta charset=\"UTF-8\">\n" +
              "    <title></title>\n" +
              "</head>\n" +
              "<body align=\"center\">\n" +
              "    <table  border=\"1\" align=\"center\" >");
      toHtml(total, buffer);
      buffer.append("    </table>" +
              "  </body>\n" +
              "</html>");

      return buffer.toString();
  }
}
