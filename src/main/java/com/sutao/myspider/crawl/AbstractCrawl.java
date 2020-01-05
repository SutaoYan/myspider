package com.sutao.myspider.crawl;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Component
public abstract class AbstractCrawl<E> {
    private ArrayList<E> total = new ArrayList<>();

    protected Comparator<E> comparator;
    protected int maxPage = 50;
    protected int threadCount = 10;
    private int proxyPort = 8080;
    private String proxyUrl;

    public abstract ArrayList<E> run(int page);

    public abstract void toHtmlImpl(ArrayList<E> total, StringBuilder builder);

    public AbstractCrawl() {
        String http_Proxy = System.getenv("http_proxy");
        if (!StringUtils.isEmpty(http_Proxy)) {
            int index = http_Proxy.indexOf("http://");
            http_Proxy = http_Proxy.substring(7);
            String tmp[] = http_Proxy.split(":");
            proxyUrl = tmp[0];
            proxyPort = Integer.valueOf(tmp[1]);
        }
    }

    public String downloadHtml(String address, String codepage) {
        URL url = null;
        StringBuffer tmp = new StringBuffer();
        try {
            url = new URL(address);
            Proxy proxy = null;
            HttpURLConnection uc;
            if (null != proxyUrl) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
                uc = (HttpURLConnection) url.openConnection(proxy);
            } else {
                uc = (HttpURLConnection) url.openConnection();
            }

            uc.connect();
            String line = null;

            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), codepage));
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return tmp.toString();
    }

    public void crawl() {
        total.clear();
        ExecutorService es = Executors.newWorkStealingPool(threadCount);
        List<Future<ArrayList<E>>> results = Stream.iterate(1, page -> page + 1).limit(maxPage).map(page -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return es.submit(() -> run(page));
            }
        ).collect(Collectors.toList());
        results.forEach(item -> {
            try {
                total.addAll(item.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        total.sort(getComparator());
    }

    public String toHtml() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head lang=\"en\">\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body align=\"center\">\n" +
                "    <table  border=\"1\" align=\"center\" >");
        toHtmlImpl(getTotal(), buffer);
        buffer.append("    </table>" +
                "  </body>\n" +
                "</html>");
        return buffer.toString();
    }
}
