package com.concurrent.features;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;
import cucumber.runtime.PendingException;

public class SiteLoaderStepDefs {

    @Given("^the website \"([^\"]*)\" is available$")
    public void the_website_is_available(String arg1) {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
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
