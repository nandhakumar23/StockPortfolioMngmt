Feature: Adding stocks to the viewed stocks list
  
  Scenario: Successfully adding a stock to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I enter a valid purchase date in the View Stocks popup
    And I enter a valid sell date in the View Stocks popup
    And I click View Stock
    Then the stock should be added to the Viewed Stocks list
    
  Scenario: Adding a stock with an invalid ticker symbol to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter an invalid Ticker symbol in the View Stocks popup
    And I enter any number of shares in the View Stocks popup
    And I enter any valid purchase date in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Illegal ticker symbol" in the View Stocks popup
    
  Scenario: Adding a stock with zero shares to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter zero shares in the View Stocks popup
    And I enter any valid purchase date in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Negative or zero values of quantity" in the View Stocks popup
   
  Scenario: Adding a stock with negative shares to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a negative number of shares in the View Stocks popup
    And I enter any valid purchase date in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Negative or zero values of quantity" in the View Stocks popup
   
  Scenario: Adding a stock without a purchase date to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I do not enter a purchase date in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Purchase date is required" in the View Stocks popup
    
  Scenario: Adding a stock with a sold date and no purchase date to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I do not enter a purchase date in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Sold date with no purchase date" in the View Stocks popup
    
  Scenario: Adding a stock with a sold date prior to purchase date to the viewed list
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I enter any valid purchase date in the View Stocks popup
    And I enter a sell date before the purchase date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Sold date prior to purchase date" in the View Stocks popup
    
  Scenario: Adding a stock with a purchase date older than one year ago
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I enter a purchase date older than one year ago in the View Stocks popup
    And I enter any valid sell date in the View Stocks popup
    And I click View Stock
    Then I should see the error "Purchase date cannot be older than 1 year ago" in the View Stocks popup

  Scenario: Adding a stock with a sold date older than one year ago
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I enter a purchase date older than one year ago in the View Stocks popup
    And I enter a sell date older than one year ago in the View Stocks popup
    And I click View Stock
    Then I should see the error "Sold date cannot be older than 1 year ago" in the View Stocks popup
    
  Scenario: Showing the View Stock button
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    Then I should see the View Stock button in the View Stocks popup
    
  Scenario: Showing the Cancel button
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    Then I should see the Cancel button in the View Stocks popup
    
  Scenario: Successfully adding a stock to the viewed list using calendar popups
    Given I am on the home page
    When I click Add Stock in the Viewed Stocks box
    And I enter a valid Ticker symbol in the View Stocks popup
    And I enter a valid number of shares in the View Stocks popup
    And I enter a valid purchase date using the calendar popup in the View Stocks popup
    And I enter a valid sell date using the calendar popup in the View Stocks popup
    And I click View Stock
    Then the stock should be added to the Portfolio list