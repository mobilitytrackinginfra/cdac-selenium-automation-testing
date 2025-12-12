// test/google.test.js
const { Builder, By, Key, until } = require('selenium-webdriver');
const { expect } = require('chai');

describe('Google Search using Selenium + Mocha + Chai', function () {
  // Allow more time for browser startup + tests
  this.timeout(60000);

  let driver;

  before(async function () {
    this.timeout(60000); // extra safety for just the before hook
    console.log('Starting Chrome WebDriver...');

    try {
      driver = await new Builder()
        .forBrowser('chrome')   // Selenium Manager will now handle driver download
        .build();

      console.log('Chrome WebDriver started successfully.');
    } catch (err) {
      console.error('Error starting Chrome WebDriver:', err);
      throw err;
    }
  });

  after(async function () {
    console.log('Quitting Chrome WebDriver...');
    if (driver) {
      await driver.quit();
    }
  });

  it('should open Google and verify title', async function () {
    await driver.get('https://www.google.com');

    const title = await driver.getTitle();
    console.log('Page title is:', title);
    expect(title).to.contain('Google');
  });

  it('should search for "Mocha Chai Selenium"', async function () {
    const searchBox = await driver.findElement(By.name('q'));

    await searchBox.sendKeys('Mocha Chai Selenium', Key.RETURN);

    await driver.wait(until.titleContains('Mocha'), 10000);

    const title = await driver.getTitle();
    console.log('Search results title is:', title);
    expect(title.toLowerCase()).to.contain('mocha');
  });
});
