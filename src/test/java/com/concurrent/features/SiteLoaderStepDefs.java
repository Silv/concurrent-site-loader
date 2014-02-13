package com.concurrent.features;

import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.retreiver.ConcurrentHttpSiteProcessor;
import com.concurrent.retreiver.HttpSiteProcessor;
import com.concurrent.usecase.SiteProcessor;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.table.DataTable;
import gherkin.formatter.model.DataTableRow;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@RunWith(Enclosed.class)
public class SiteLoaderStepDefs {

    public static class A_website_is_retrieved_and_saved {
        private SiteReader reader = new FakeReader("/example.html");
        private MockWriter writer = new MockWriter();
        private SiteProcessor siteProcessor = new HttpSiteProcessor(reader, writer);
        private String url;

        @Given("^the website \"([^\"]*)\"$")
        public void the_website(String url) {
            this.url = url;
        }

        @When("^the site is retrieved$")
        public void the_site_is_retrieved() {
            siteProcessor.process(url);
        }

        @Then("^the content is saved$")
        public void the_content_is_saved() {
            Element h1 = writer.getDocuments().get(0).getElementsByTag("h1").first();
            assertThat(h1.text(), is(equalTo("Example Domain")));
        }
    }

    public static class Multiple_websites_are_retrieved_and_saved {

        private SiteReader reader = new FakeReader("/example.html");
        private MockWriter writer = new MockWriter();
        private SiteProcessor siteProcessor = new HttpSiteProcessor(reader, writer);
        private ImmutableList<String> urls;

        @Given("^the following websites$")
        public void the_following_websites(DataTable urlTable) {
            urls = ImmutableList.copyOf(Lists.transform(urlTable.getGherkinRows(), new DataTableMapper()));
        }

        @When("^the sites are retrieved$")
        public void the_sites_are_retrieved() {
            siteProcessor.process(urls);
        }

        @Then("^all content is saved$")
        public void all_content_is_saved() {
            assertThat(writer.getDocuments().size(), is(equalTo(urls.size())));
        }
    }

    public static class Multiple_websites_are_retrieved_and_saved_concurrently {

        private SiteReader reader = new FakeReader("/example.html");
        private MockWriter writer = new MockWriter();
        private SiteProcessor siteProcessor = new ConcurrentHttpSiteProcessor(reader, writer, 2);
        private ImmutableList<String> urls;

        @Given("^the websites$")
        public void the_websites(DataTable urlTable) {
            urls = ImmutableList.copyOf(Lists.transform(urlTable.getGherkinRows(), new DataTableMapper()));
        }

        @When("^the sites are retrieved concurrently$")
        public void the_sites_are_retrieved_concurrently() {
            siteProcessor.process(urls);
        }

        @Then("^content is saved concurrently$")
        public void content_is_saved_concurrently() {
            assertThat(writer.getDocuments().size(), is(equalTo(urls.size())));
        }
    }
}

class DataTableMapper implements Function<DataTableRow, String> {
    @Override
    public String apply(DataTableRow dataTableRow) {
        return dataTableRow.getCells().get(0);
    }
}

class FakeReader implements SiteReader {

    private final String filename;

    FakeReader(String filename) {
        this.filename = filename;
    }

    @Override
    public Document retrieve(String url) {
            StringBuffer html = new StringBuffer("");
            try {
                InputStream stream = getClass().getResourceAsStream(filename);

                InputStreamReader isr = new InputStreamReader(stream);
                BufferedReader ir = new BufferedReader(isr);
                String line;
                while ((line = ir.readLine()) != null) {
                    html.append(line);
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        return Jsoup.parse(html.toString());
    }
}

class MockWriter implements SiteWriter {

    private List<Document> documents = new ArrayList<>();

    @Override
    public void write(Document d) {
        documents.add(d);
    }

    List<Document> getDocuments() {
        return ImmutableList.copyOf(documents);
    }
}