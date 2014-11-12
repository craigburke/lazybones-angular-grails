import org.openqa.selenium.phantomjs.PhantomJSDriver

System.setProperty('phantomjs.binary.path', new File('node_modules/phantomjs/bin/phantomjs').canonicalPath)

driver = {
    new PhantomJSDriver()
}

waiting {
    timeout = System.getProperty('geb.waiting.timeout') ?: 30
    retryInterval = 0.5
}

atCheckWaiting = true
