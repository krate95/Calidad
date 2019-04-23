Given("I am on the Renfe homepage") do
  visit('https://www.renfe.com')
end

Given("I am on the Renfe login page") do
  visit('https://venta.renfe.com/vol/home.do?c=_FpBn')
end

When("I click on the login link") do
  click_link('Clientes Renfe. Identifícate.')
end

Then("I should see the Ministerio de Fomento logo") do
  expect(page).to have_xpath("//img[contains(@src, \"#{logo_gob.jpg}\")]")
end

Then("I should see the CLIENTES RENFE. IDENTIFÍCATE. in a link") do
  expect(page).to have_content('Clientes Renfe. Identifícate.')
end

Then("I should see the login page") do
  expect(page).to have_content('https://venta.renfe.com/vol/home.do?c=_FpBn')
end

Then("I should see the field Usuario / email") do
  expect(page).to have_content('txtoUsuario')
end

Then("I should see the Ventas homepage") do
  expect(page).to have_content('https://venta.renfe.com/vol/home.do?c=_ZYS6')
end

And("I should see the field Contraseña") do
  expect(page).to have_content('txtoPass')
end

When("I fill in Usuario / email with email") do
  fill_in('txtoUsuario', :with => email)
end

And("I fill in Contraseña with pass") do
  fill_in('txtoPass', :with => pass)
end

And("I click entrar") do
  click_link('login-button')
end
