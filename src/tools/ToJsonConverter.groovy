package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }

    def parseJsonText() {
        JsonSlurper parser = new groovy.json.JsonSlurper()
        Map parsedJson = parser.parseText(json)
        return parsedJson
    }
}