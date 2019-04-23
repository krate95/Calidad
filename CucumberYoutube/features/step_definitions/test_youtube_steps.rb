Capybara.default_driver=:selenium

Given(/^I am on Youtube$/) do
  visit('http://www.youtube.com')
end

Given(/^I am on the profile view$/) do
  visit('https://www.youtube.com/user/markwshead')
end

Given(/^I see link \"([^\"]*)\"$/) do |string|
  expect(page).to have_content(string)
end

Given (/^I write in the search engine with \"([^\"]*)\"$/) do |string|
  fill_in('search', :with => string)
end

Given (/^I press Search button$/) do
  click_button('search-icon-legacy')
end

When(/^I type in the search engine with \"([^\"]*)\"$/) do |string|
  fill_in('search', :with => string)
end

When(/^I click Search$/) do
  click_button('search-icon-legacy')
end
When (/^I wait (\d+) seconds$/) do |seconds|
  sleep(seconds.to_i)
end

When(/^I see url with \"([^\"]*)\"$/) do |string|
  expect(page).to have_content(string)
end

When (/^I click on the url \"([^\"]*)\"$/) do |url|
  click_link url
end


When (/^I press Enter$/) do |url|
  @driver.action.send_keys(:enter).perform
end

When (/^I write in the input \"([^\"]*)\"$/) do |input|
	fill_in('input', :with => string)
end

Then (/^I click on url \"([^\"]*)\"$/) do |url|
  click_link url
end

Then (/^I wait for (\d+) seconds$/) do |seconds|
  sleep(seconds.to_i)
end

Then(/^I should see url with \"([^\"]*)\"$/) do |string|
  expect(page).to have_content(string)
end
Then (/^I should see title of the video \"([^\"]*)\"$/) do |url|
  expect(page).to have_content(url)
end

