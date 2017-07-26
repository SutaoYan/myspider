package com.sutao.myspider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Crawl<E> {
  protected int maxPage = 50;
  private Comparator<E> comparator;

  public int threadCount = 10;

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }
  public abstract ArrayList<E> run (int page);
  public abstract void toHtml(ArrayList<E> total, BufferedWriter bw ) throws IOException;

  public Crawl(Comparator<E> comparator, int maxPage) {
    this.comparator = comparator;
    this.maxPage = maxPage;
  }

  public Crawl(Comparator<E> comparator) {
    this.comparator = comparator;
  }

  public void crawl(String fileName) {
    ArrayList<E> total = new ArrayList<>();
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
    toHtmlAbstract(total, fileName);
  }

  public void toHtmlAbstract(ArrayList<E> total, String fileName) {
    File f = new File(fileName);
    if (f.exists()) {
      f.delete();
    }
    try (FileWriter fw = new FileWriter(fileName);
         BufferedWriter bw = new BufferedWriter(fw);) {
      bw.write("<!DOCTYPE html>\n" +
              "<html>\n" +
              "<head lang=\"en\">\n" +
              "    <meta charset=\"UTF-8\">\n" +
              "    <title></title>\n" +
              "</head>\n" +
              "<body align=\"center\">\n" +
              "    <table  border=\"1\" align=\"center\" >");
      toHtml(total, bw);
      bw.write("    </table>" +
              "  </body>\n" +
              "</html>");
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
