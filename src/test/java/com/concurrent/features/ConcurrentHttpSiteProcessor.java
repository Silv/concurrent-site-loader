package com.concurrent.features;

import akka.dispatch.ExecutionContexts;
import akka.dispatch.*;
import akka.japi.JavaPartialFunction;
import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.usecase.SiteProcessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import scala.Function1;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.japi.Function;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static akka.dispatch.Futures.future;
import static akka.dispatch.Futures.sequence;
import static com.google.common.collect.ImmutableList.Builder;
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

        Builder<Future<Document>> builder = ImmutableList.builder();
        for (final String url : urls) {
            builder.add(createFuture(ec, url));
        }

        Future<Iterable<Document>> futuresSequence = sequence(builder.build(), ec);
        futuresSequence.foreach(new JavaPartialFunction<Iterable<Document>, Object>() {
            @Override
            public Object apply(final Iterable<Document> docs, final boolean isCheck) throws Exception {
                for (Document doc : docs) {
                    writer.write(doc);
                }
                return null;
            }
        }, ec);

        try {
            Await.result(futuresSequence, Duration.create(20, SECONDS));
        } catch (Exception e) {
            throw new RuntimeException();
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

