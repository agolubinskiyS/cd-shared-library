package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }

    @NonCPS
    def parsejson(String json) {
        def lazyMap = new JsonSlurper().parseText(json)

        // JsonSlurper returns a non-serializable LazyMap, so copy it into a regular map before returning
        def m = [:]
        m.putAll(lazyMap)
        return m
    }
}