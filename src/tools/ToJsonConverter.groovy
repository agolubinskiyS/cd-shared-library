package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    String stringJson
    
    ToJsonConverter(stringJson) {
        this.stringJson = stringJson
    }

    def parseJsonText() {
    def object = new JsonSlurper().parseText(stringJson)
    if(object instanceof groovy.json.internal.LazyMap) {
        return new HashMap<>(object)
    }
    return object
    }
}