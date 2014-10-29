import org.openqa.selenium.phantomjs.PhantomJSDriver

driver = {
    new PhantomJSDriver()
}

waiting {
    timeout = 30
    retryInterval = 0.5
}

atCheckWaiting = true
