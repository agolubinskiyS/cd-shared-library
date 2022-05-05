package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    ToJsonConverter() {}

    def parseJsonText(String json) {
    def object = new JsonSlurper().parseText(json)
    if(object instanceof groovy.json.internal.LazyMap) {
        return new HashMap<>(object)
    }
    return object
    }
}