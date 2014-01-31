package com.concurrent.features;

import com.concurrent.gateway.SiteReader;
import com.concurrent.gateway.SiteWriter;
import com.concurrent.retreiver.HttpSiteProcessor;
import com.concurrent.usecase.SiteProcessor;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

//@RunWith(Suite.class)
//@Suite.SuiteClasses({
//        SiteLoaderStepDefs.RetrieveAndSave.class
//})
public class SiteLoaderStepDefs {

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
            Element h1 = writer.getLastDocument().getElementsByTag("h1").first();
            assertThat(h1.text(), is(equalTo("Example Domain")));
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

    private Document lastDocument;

    @Override
    public void write(Document document) {
        this.lastDocument = document;
    }
    Document getLastDocument() {
        return lastDocument;
    }
}