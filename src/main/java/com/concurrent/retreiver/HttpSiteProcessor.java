package com.concurrent.retreiver;

import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.usecase.SiteProcessor;
import com.google.common.collect.ImmutableList;
import org.jsoup.nodes.Document;

import java.util.List;

public class HttpSiteProcessor implements SiteProcessor {

    private final SiteReader reader;
    private final SiteWriter writer;

    public HttpSiteProcessor(SiteReader reader, SiteWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void process(String url) {
        Document doc = reader.retrieve(url);
        writer.write(doc);
    }

    @Override
    public void process(ImmutableList<String> urls) {
        for (String url : urls) {
            Document doc = reader.retrieve(url);
            writer.write(doc);
        }
    }
}
