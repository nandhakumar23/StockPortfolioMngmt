Feature: Mobile resize

  Scenario: Making sure navbar is still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the navbar should still be visible
 
   Scenario: Making sure graph container is still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the graph container should still be visible

   Scenario: Making sure portfolio list is still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the portfolio list should still be visible
    
   Scenario: Making sure viewed stocks list is still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the viewed stocks list should still be visible