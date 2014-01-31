package com.concurrent.retreiver;

import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.usecase.SiteProcessor;
import org.jsoup.nodes.Document;

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
}
