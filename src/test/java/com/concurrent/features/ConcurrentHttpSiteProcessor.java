package com.concurrent.features;

import akka.dispatch.ExecutionContexts;
import akka.dispatch.*;
import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.usecase.SiteProcessor;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ConcurrentHttpSiteProcessor implements SiteProcessor {

    private final SiteReader reader;
    private final SiteWriter writer;
    private final int threadPool;

    public ConcurrentHttpSiteProcessor(SiteReader reader, SiteWriter writer, int threadPool) {
        this.reader = reader;
        this.writer = writer;
        this.threadPool = threadPool;
    }

    @Override
    public void process(String url) {
        process(Lists.newArrayList(url));
    }

    @Override
    public void process(List<String> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPool);
        ExecutionContext ec = ExecutionContexts.fromExecutorService(executor);

        List<Future<Document>> futures = new ArrayList<>();
        for (final String url : urls) {
            System.out.println("creating futures");
            futures.add(createFuture(ec, url));
        }

        Future<Iterable<Document>> futuresSequence = sequence(futures, ec);

        Iterator<Document> documents = Collections.<Document>emptyList().iterator();
        try {
            documents = Await.result(futuresSequence, Duration.create(20, SECONDS)).iterator();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        while (documents.hasNext()) {
            writer.write(documents.next());
        }

        executor.shutdown();
    }

    private Future<Document> createFuture(final ExecutionContext ec, final String url) {
        return future(new Callable<Document>() {
            public Document call() {
                System.out.println("before sleep url: " + url);
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException("Problem sleeping");
                }
                System.out.println("after sleep url: " + url);
                return reader.retrieve(url);
            }
        }, ec);
    }

}

