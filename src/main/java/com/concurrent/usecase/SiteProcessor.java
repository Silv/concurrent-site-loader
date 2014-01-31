package com.concurrent.usecase;

import org.jsoup.nodes.Document;

public interface SiteProcessor {
    void process(String url);
}
