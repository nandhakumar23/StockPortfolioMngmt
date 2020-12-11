Feature: Adding stocks to the portfolio by importing a CSV file
    
  Scenario: Showing Upload CSV Popup
    Given I am on the home page
    When I click Import CSV
    Then I should see an Upload File button in CSV Popup
  
  Scenario: Showing Cancel CSV Popup
    Given I am on the home page
    When I click Import CSV
    Then I should see a Cancel button in CSV Popup
    
  Scenario: Successfully adding stocks to Portfolio using CSV file
    Given I am on the home page
    When I click Import CSV
    And I upload a valid CSV file
    And I click Upload File
    Then the stocks in the CSV should be added to the Portfolio list
    
  Scenario: Adding stocks to Portfolio using an incorrectly formatted CSV file
    Given I am on the home page
    When I click Import CSV
    And I upload an invalid CSV file
    And I click Upload File
    Then I should see an error for invalid CVS