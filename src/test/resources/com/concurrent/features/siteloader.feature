Feature: Saving web site content
  In order to save web site content
  As the loading service
  I Want to save website content to be retrieved later

  Scenario: A website is retrieved and saved
    Given the website "http://www.example.com"
    When the site is retrieved
    Then the content is saved

  Scenario: Mutliple websites are retrieved and saved
    Given the following websites
      |http://www.example.com|
      |http://www.google.com|
      |http://www.yahoo.com|
    When the sites are retrieved
    Then all content is saved

  Scenario: Mutliple websites are retrieved and saved concurrently
    Given the websites
      |http://www.example.com|
      |http://www.example.com?1|
      |http://www.example.com?2|
      |http://www.example.com?3|
      |http://www.example.com?4|
      |http://www.example.com?5|
      |http://www.example.com?6|
      |http://www.example.com?7|
      |http://www.example.com?8|
      |http://www.example.com?9|
    When the sites are retrieved concurrently
    Then content is saved concurrently

