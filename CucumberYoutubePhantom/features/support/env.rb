require 'capybara/cucumber'
require 'capybara/poltergeist'
require 'rspec'

Capybara.default_driver=:poltergeist

Capybara.javascript_driver = :poltergeist

options = {js_errors: false}
Capybara.register_driver :poltergeist do |app|
  options = {
              js_errors: false,
              extensions: [],
              phantomjs_options:['--proxy-type=none', '--load-images=no', '--ignore-ssl-errors=true'],
              timeout: 180,
              window_size: ['1300', '1800'],
              inspector: true,
            }
  Capybara::Poltergeist::Driver.new(app, options)
end

RSpec.configure do |config|
  config.after(:each, :js) do
    Capybara.reset_sessions!
    Capybara.use_default_driver
  end
end
