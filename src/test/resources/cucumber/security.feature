Feature: Security

  Scenario: Signing out from home page
    Given I am on the sign in page
    When I enter an valid username "test2"
    And I enter the correct password "test2test"
    And I click submit on login
    And I click on the sign out button
    Then I should be signed out and taken back to the sign in page
    
  Scenario: Timing out user
    Given I am on the sign in page
    When I enter an valid username "test2"
    And I enter the correct password "test2test"
    And I click submit on login
    When 120 seconds of inactivity occurs
    Then I should see an alert that I am being logged out
    
  Scenario: Attempting to access homepage while not logged in
    Given I am not logged in
    When I attempt to access "https://localhost:8443/homepage.jsp"
    Then I should be redirected to the sign in page
    
  Scenario: Attempting to access without https
  	Given I have the brower open
  	When I attempt to access "http://localhost:8443/signIn.jsp"
  	Then I should not be able to access the page
  	