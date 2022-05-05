package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }

    def parseJsonText() {
        def slurper = new groovy.json.JsonSlurper()
        def parsedJson = slurper.parseText(json)
        return parsedJson
    }
}