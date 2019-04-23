Feature: Testing Youtube UI

	Scenario: Search for Software Agility videos
		Given I am on Youtube
		When I type in the search engine with "Software Agility"
		And I click Search
		Then I wait for 1 seconds
		And I should see url with "What is Agile?"

	Scenario: Play an Agility Software Video on Youtube
		Given I am on Youtube
		And I write in the search engine with "Software Agility"
		And I press Search button
		When I wait 5 seconds
		And I see url with "What is Agile?"
		And I click on the url "What is Agile?"
		Then I wait for 1 seconds
		And I should see title of the video "What is Agile?"
		And I wait for 5 seconds
	
	Scenario: Enter into someone's profile
		Given I am on Youtube
		And I write in the search engine with "Software Agility"
		And I press Search button
		When I wait 5 seconds
		And I see url with "What is Agile?"
		And I wait 4 seconds
		And I click on the url "Mark Shead"
		Then I wait for 1 seconds
		And I should see url with "Mark Shead"	
	
	Scenario: Play youtube video from someone's profile view
		Given I am on the profile view
		And I see link "Agile User Stories"
		When I click on the url "Agile User Stories"
		And I wait 1 seconds
		Then I should see title of the video "Agile User Stories"
		And I wait for 5 seconds

	Scenario: Play youtube video from another video view
		Given I am on the profile view
		And I see link "Agile User Stories"
		When I click on the url "Agile User Stories"
		And I wait 1 seconds
		And I see url with "Agile User Stories"
		And I wait 5 seconds
		Then I click on url "Splitting User Stories - Agile Practices"
		And I wait for 1 seconds
		And I should see title of the video "Splitting User Stories - Agile Practices"
		And I wait for 5 seconds


		
		
