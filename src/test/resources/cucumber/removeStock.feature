Feature: Removing stocks from homepage

  Scenario: Showing Delete Stock button
    Given I am on the home page
    When I click the trash icon on a stock row in the Portfolio list
    Then I should see the Delete Stock button
    
  Scenario: Showing Cancel button
    Given I am on the home page
    When I click the trash icon on a stock row in the Portfolio list
    Then I should see the Cancel button
  
  Scenario: Removing a stock from the portfolio list
    Given I am on the home page
    When I click the trash icon on a stock row in the Portfolio list
    And I click Delete Stock
    Then the stock should be removed from the Portfolio list and graph
  
  Scenario: Removing a stock from the viewed list
    Given I am on the home page
    When I click the trash icon on a stock row in the Viewed Stocks list
    And I click Delete Stock
    Then the stock should be removed from the Viewed Stocks list and graph