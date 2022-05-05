package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }

    def parseJsonText() {
        def request = new JsonSlurper().parseText(json);
        return request
    }
}