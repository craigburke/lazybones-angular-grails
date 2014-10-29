import org.openqa.selenium.firefox.FirefoxDriver

driver = {
    new FirefoxDriver()
}

waiting {
    timeout = 60
    retryInterval = 0.5
}

atCheckWaiting = true
