import org.openqa.selenium.firefox.FirefoxDriver

driver = {
    new FirefoxDriver()
}

waiting {
    timeout = 45
    retryInterval = 0.5
}

atCheckWaiting = true
