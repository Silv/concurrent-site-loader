package com.concurrent.usecase;

import java.util.List;

public interface SiteProcessor {
    void process(String url);

    void process(List<String> urls);
}
