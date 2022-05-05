package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    String stringJson
    
    ToJsonConverter(stringJson) {
        this.stringJson = stringJson
    }

    def parseJsonText(String stringJson) {
    def object = new JsonSlurper().parseText(json)
    if(object instanceof groovy.json.internal.LazyMap) {
        return new HashMap<>(object)
    }
    return object
    }
}