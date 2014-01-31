package com.concurrent.gateway;

import org.jsoup.nodes.Document;

public interface SiteReader {
    Document retrieve(String url);
}
