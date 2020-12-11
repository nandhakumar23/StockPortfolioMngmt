Feature: The webapp resizes responsively

  Scenario: Navbar visible on SI
  	Given I am on the sign in page
  	When I resize to mobile dimensions
  	Then the navbar should still be visible
  	
  Scenario: login form visible on SI
  	Given I am on the sign in page
  	When I resize to mobile dimensions
  	Then the login form should still be visible in SI
  	
  Scenario: sign in button visible on SI
  	Given I am on the sign in page
  	When I resize to mobile dimensions
  	Then the button should still be visible in SI
  
   Scenario: submit in button visible on SU
  	Given I am on the sign up page
  	When I resize to mobile dimensions
  	Then the submit button should still be visible in SU
  	
   Scenario: cancel button visible on SU
  	Given I am on the sign up page
  	When I resize to mobile dimensions
  	Then the cancel button should still be visible in SU
  	
  Scenario: reg form visible on SU
  	Given I am on the sign up page
  	When I resize to mobile dimensions
  	Then the reg form should still be visible in SU
  	
  Scenario: Navbar still visible on SU
   	Given I am on the sign up page
    When I resize to mobile dimensions
    Then the navbar should still be visible
  
  Scenario: Navbar still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the navbar should still be visible
    
  Scenario: Sign out button still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the signout button should still be visible
 
  Scenario: Graph container still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the graph container should still be visible

  Scenario: Portfolio list still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the portfolio list should still be visible
    
  Scenario: Viewed stocks list still visible on homepage
    Given I am on the home page
    When I resize to mobile dimensions
    Then the viewed stocks list should still be visible