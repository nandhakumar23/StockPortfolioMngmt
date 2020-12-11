Feature: Portfolio value and percentage change upon adding or removing request
    
  Scenario: Porfolio value change upon adding a stock
    Given I am on the home page with a new account
    When I click Add Stock in the Portfolio box
    And I enter a valid Ticker symbol in the Add Stocks popup
    And I enter a valid number of shares in the Add Stocks popup
    And I enter a valid purchase date in the Add Stocks popup
    And I enter a future sell date in the Add Stocks popup
    And I click Add Stock
    Then the porfolio value should no longer be zero
  
  Scenario: Porfolio value change upon removing a stock
    Given I am on the home page with only one stock in the porfolio
    When I click the trash icon on a stock row in the Portfolio list
    And I click Delete Stock
    Then the porfolio value should be back to zero
    
  Scenario: Porfolio percentage change upon adding a stock
    Given I am on the home page with a new account
    When I click Add Stock in the Portfolio box
    And I enter a valid Ticker symbol in the Add Stocks popup
    And I enter a valid number of shares in the Add Stocks popup
    And I enter a valid purchase date in the Add Stocks popup
    And I enter a future sell date in the Add Stocks popup
    And I click Add Stock
    Then the porfolio percentage change should no longer be zero
    
  Scenario: Porfolio percentage change upon removing a stock
    Given I am on the home page with only one stock in the porfolio
    When I click the trash icon on a stock row in the Portfolio list
    And I click Delete Stock
    Then the porfolio percentage change should be back to zero
    
  Scenario: Porfolio value upon select all
    Given I am on the home page with deselect all pressed
    When I click the select all button
    Then the porfolio value should no longer be zero
    
  Scenario: Porfolio value upon deselect all
    Given I am on the home page with select all pressed
    When I click the deselect all button
    Then the porfolio value should be back to zero
    
  Scenario: Porfolio percentage change upon select all
    Given I am on the home page with deselect all pressed
    When I click the select all button
    Then the porfolio percentage change should no longer be zero
    
  Scenario: Porfolio percentage change upon deselect all
    Given I am on the home page with select all pressed
    When I click the deselect all button
    Then the porfolio percentage change should be back to zero