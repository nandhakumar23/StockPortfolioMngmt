Feature: Sign Up Page

	Scenario: Sign in with username already taken
		Given I am on the sign up page
		When I enter an already used username in SU
		And I enter a password in SU
		And I enter a confirm password in SU
		And I click submit in SU
		Then I should see the SU error "Username is already been chosen"
		
	Scenario: click cancel button
		Given I am on the sign up page
		When I click the cancel button in SU
		Then I should land on the sign in page
	
	Scenario: Successful register
		Given I am on the sign up page
		When I enter a valid username that does not exist in SU
		And I enter a password in SU
		And I enter a confirm password in SU
		And I click submit in SU 
		Then I should land on the sign in page
	
	Scenario: Non-Matching passwords
		Given I am on the sign up page
		When I enter a valid username in SU
		And I enter a password in SU
		And I enter a non-matching password in confirm password in SU
		And I click submit in SU 
		Then I should see the SU error "Please make sure the two passwords inputted match each other"
		
	Scenario: No username input
		Given I am on the sign up page
		When I enter a password in SU
		And I enter a confirm password in SU
		And I click submit in SU 
		Then I should see the SU error "Please input a username of atleast 1 character"
		
	Scenario: No password input
		Given I am on the sign up page
		When I enter a valid username in SU
		And I click submit in SU 
		Then I should see the SU error "Please input a password of atleast 8 characters"
		
	Scenario: No input
		Given I am on the sign up page
		When I click submit in SU 
		Then I should see the SU error "Please input a username of atleast 1 character as well as a password of atleast 8 characters"
	
	Scenario: SignUp to SignIn
		Given I am on the sign up page
		When I enter a valid username that does not exist in SU
		And I enter a password in SU
		And I enter a confirm password in SU
		And I click submit in SU
		And I click submit on login
    Then I should be taken to the home page
    	
  Scenario: Cancel button text check
		Given I am on the sign up page
		Then I should see the text on the cancel button say cancel
		
	Scenario: Create user button text check
		Given I am on the sign up page
		Then I should see the text on the create user button say create user