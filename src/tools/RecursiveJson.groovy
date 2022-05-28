package tools
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 
import groovy.json.JsonSlurperClassic 


class RecursiveJson {

    def schemaJson
    def descriptorJson
    List filtered = []
    Map<String, String> result = new LinkedHashMap();

    RecursiveJson() {
    }
    @NonCPS
    void toMap(Object json, String key, Map result){
        json = [(key): json]
        json.each {k, v ->
        if( v instanceof Map ){
            v.each { k1, v1 -> 
                if (v1 instanceof Map)
                toMap(v1, k + "." + k1 , result)
                else if(v1 instanceof List){
                    v1.each{ v2 ->
                        toMap(v2, k + k1, result)
                    }
                }
                else
                    result.put(k + "." + k1, v1)
            }
        }
        }
    }
    @NonCPS
    void filteredValues(String crude, Boolean value, List filtered) {
        String pattern = /.*\.(.*?)\.ui.visible\.(.*)/   
        def m = crude =~ pattern
        List f = [m[0][1], m[0][2], value]
        filtered.add(f)
    }

    @NonCPS
    void pruneDescriptorHelper(String a, String b, Boolean c, Map descriptorJson) {
        //NEED TO FIND b in fields and verify that b + c is the same in the descriptorJson othewise delete a
        if (descriptorJson instanceof Map) {
            if (descriptorJson.containsKey(b)) {  
            if (descriptorJson[b] == c) {
                //NEED TO KEEP AS IS
                return
            }      
            else {
                //NEED TO REMOVE
                descriptorJson.remove(a)
            }
            }
            else {
                descriptorJson.each { k, v -> 
                    if (v instanceof Map) {
                        pruneDescriptorHelper(a, b, c, v)
                    }
                    else {
                        return
                    } 
                    
                }      
            }
        }
        else if (descriptorJson instanceof List) {
            return
        }
        else {
            println("Neither map nor list")
            return
        }
    }
    
    @NonCPS
    void pruneDescriptor(List filtered, Object descriptorJson){
        filtered.each { a, b, c ->
            pruneDescriptorHelper(a, b, c, descriptorJson)
        }
    }

    @NonCPS
    def readJsonMap(String path) {
        def object = new JsonSlurper().parseText(path)
        if(object instanceof groovy.json.internal.LazyMap) {
            return new HashMap<>(object)
        }
        return object
    }



    @NonCPS
    def runParseJson(String schemaJsonPath, String descriptorJsonPath) {
        schemaJson = getJson(schemaJsonPath)
        descriptorJson = getJson(descriptorJsonPath)
        
        toMap(schemaJson.properties, "data", result)

        result.each{ k, v ->
            k.contains('visible') ? filteredValues(k, v, filtered) : null
        }

        pruneDescriptor(filtered, descriptorJson)
        def resultjson = JsonOutput.toJson(descriptorJson)
        String s = resultjson

        return s
    } 

    @NonCPS
    def getJson(rawJSON) {
        def slurper = new JsonSlurper()
        def json = slurper.parseText(rawJSON)
        return json
    }
}


   
// filename = 'schema-eureka.json' 
// filenameDescriptor = 'descriptor-api.json'

// def schemaJson = new JsonSlurper().parse(new File(filename))
// def descriptorJson = new JsonSlurper().parse(new File(filenameDescriptor))

// def p = new tools.RecursiveJson()
// print(p.runParseJson(schemaJson, descriptorJson))