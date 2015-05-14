import org.openqa.selenium.phantomjs.PhantomJSDriver

System.setProperty('phantomjs.binary.path', new File('node_modules/phantomjs/bin/phantomjs').canonicalPath)

driver = {
    new PhantomJSDriver()
}

waiting {
    timeout = 10
    retryInterval = 0.5
}

baseUrl = 'http://localhost:8080/'
reportsDir = 'build/geb-reports'
atCheckWaiting = true
