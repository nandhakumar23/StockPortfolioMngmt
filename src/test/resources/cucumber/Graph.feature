Feature: Graph
   Scenario: Adjust date to view past performance  
    Given I am on the home page
    When I click the 1 week button of the home page.
    Then the graph should re-adjust on the home page.
 
   Scenario: Adjust date to view past performance 2  
    Given I am on the home page
    When I click the 1 month button of the home page.
    Then the graph should re-adjust on the home page.

   Scenario: Adjust date to view past performance 3  
    Given I am on the home page
    When I click the 1 year button of the home page.
    Then the graph should re-adjust on the home page.
    
   Scenario: Adjust date to view past performance 4  
    Given I am on the home page
    When I click the 1 day button of the home page.
    Then the graph should re-adjust on the home page.
    
   Scenario: Zoom in button  
    Given I am on the home page
    When I click the zoom in button of the home page.
    Then the graph should zoom in on the home page.
    
   Scenario: Zoom in button text
    Given I am on the home page
    Then the zoom in button should have a plus symbol.
      
   Scenario: Zoom out button  
    Given I am on the home page
    When I click the zoom out button of the home page.
    Then the graph should zoom out on the home page.
    
   Scenario: Zoom out button text
    Given I am on the home page
    Then the zoom out button should have a minus symbol.
    
   Scenario: Custom range working  
    Given I am on the home page
    When I click the custom range button.
    And I enter proper start,end dates n submit
    Then the graph should re-adjust on the home page.
    
   Scenario: Custom range incorrect input  
    Given I am on the home page
    When I click the custom range button.
    And I enter incorrect start,end dates n submit
    Then I should see an error message.
    
   Scenario: Custom range empty start  
    Given I am on the home page
    When I click the custom range button.
    And I enter a start date,leave end empty n submit
    Then the graph should re-adjust on the home page.
    
   Scenario: Custom range empty end  
    Given I am on the home page
    When I click the custom range button.
    And I enter a end date,leave start empty n submit
    Then the graph should re-adjust on the home page.
    
   Scenario: Custom range calendar popup 
    Given I am on the home page
    When I click the custom range button.
    And I enter proper start date, end date using the popup n submit
    Then the graph should re-adjust on the home page.
    
   Scenario: Default 3 months  
    Given I am on the home page
    Then the graph should have a default range of 3 months.
    
   Scenario: clearly visible dates  
    Given I am on the home page
    Then the graph should have distinct, clearly visible dates.    
 
   Scenario: SNP checkbox  
    Given I am on the home page
    When I click the snp checkbox of the home page.
    Then the graph should re-adjust on the home page.
    
   Scenario: Portfolio toggle  
    Given I am on the home page
    When I click on the toggle button of a portfolio stock.
    Then the graph should re-adjust on the home page.
    
   Scenario: Portfolio toggle all  
    Given I am on the home page
    When I click on the toggle-all button of a portfolio stock.
    Then the graph should re-adjust on the home page. 
    
   Scenario: Viewed Stock toggle  
    Given I am on the home page
    When I click on the toggle button for the viewed portfolio stock.
    Then the graph should re-adjust on the home page. 
    
   Scenario: Proper Start, end followed by empty start n end
   	Given I am on the home page
   	When I click the custom range button.
    And I enter proper start date, end date using the popup n submit
    And I click the custom range button.
    And I leave the end date,start empty empty n submit.
    Then the graph should have start as earliest start and end as curr day.
    
   Scenario: Porfolio Text color and Triangle
    Given I am on the home page
    Then the color of text should be green n up if positive or red n down if negative.