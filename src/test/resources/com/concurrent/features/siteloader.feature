Feature: Saving web site content
  In order to save web site content
  As the loading service
  I Want to save website content to be retrieved later

  Scenario: A website is retrieved and saved
    Given the website "http://www.example.com" is available
    When the site is retrieved
    Then the content is saved

#  Scenario: Customer eligible for rewards
#    Given the following list of rewards are valid
#      |Channel |RewardCode                          |
#      |SPORTS  |CHAMPIONS_LEAGUE_FINAL_TICKET       |
#      |KIDS    |                                    |
#      |MUSIC   |KARAOKE_PRO_MICROPHONE              |
#      |NEWS    |                                    |
#      |MOVIES  |PIRATES_OF_THE_CARIBBEAN_COLLECTION |
#    And the customer is "johndoe123"
#    And subscribed to channels "SPORTS,MUSIC,NEWS"
#    And eligible for rewards
#    When the rewards are requested
#    Then rewards "CHAMPIONS_LEAGUE_FINAL_TICKET,KARAOKE_PRO_MICROPHONE" are returned only

