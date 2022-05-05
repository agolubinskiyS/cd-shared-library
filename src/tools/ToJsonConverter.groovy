package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

class ToJsonConverter {
    
    def json
    
    ToJsonConverter(json) {
        this.json = json
    }
    
    @NonCPS
    @CompileStatic
    static def convertLazyMapToLinkedHashMap(def value) {
        if (value instanceof LazyMap) {
        Map copy = [:]
        for (pair in (value as LazyMap)) {
            copy[pair.key] = convertLazyMapToLinkedHashMap(pair.value)
        }
        copy
        } else {
        value
        }
    }
}