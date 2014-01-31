package com.concurrent.features;

import com.concurrent.retreiver.HttpSiteRetriever;
import com.concurrent.usecase.SiteRetriever;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.runtime.PendingException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SiteLoaderStepDefs.RetrieveAndSave.class
})
public class SiteLoaderStepDefs {

    class RetrieveAndSave {

        private SiteRetriever siteRetriever = new HttpSiteRetriever();
        private String url;

        @Given("^the website \"([^\"]*)\"$")
        public void the_website(String url) {
            this.url = url;
        }

        @When("^the site is retrieved$")
        public void the_site_is_retrieved() {
            // Express the Regexp above with the code you wish you had
            throw new PendingException();
        }

        @Then("^the content is saved$")
        public void the_content_is_saved() {
            // Express the Regexp above with the code you wish you had
            throw new PendingException();
        }
    }

}
