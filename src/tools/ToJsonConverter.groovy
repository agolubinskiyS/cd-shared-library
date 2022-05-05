package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }

    @NonCPS
    def parsejson() {
        def jsonSlurper = new JsonSlurper()
        def parsedJson = jsonSlurper.parseText(json)
        return parsedJson
    }
}