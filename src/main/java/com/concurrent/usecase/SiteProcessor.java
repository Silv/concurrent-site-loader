package com.concurrent.usecase;

import com.google.common.collect.ImmutableList;

public interface SiteProcessor {
    void process(String url);

    void process(ImmutableList<String> urls);
}
