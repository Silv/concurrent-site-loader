package com.concurrent.gateway;

import org.jsoup.nodes.Document;

public interface SiteWriter {
    void write(Document document);
}
