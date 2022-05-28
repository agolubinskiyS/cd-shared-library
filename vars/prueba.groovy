import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.json.JsonBuilder 



if (args.size() < 1) {
    println("Missing filename")
    System.exit(1)
}
    
filename = args[0]
filenameDescriptor = args[1]

def schemaJson = new JsonSlurper().parse(new File(filename))
def descriptorJson = new JsonSlurper().parse(new File(filenameDescriptor))

// def p = new tools.RecursiveJson(this, schemaJson, descriptorJson)
def p = new tools.RecursiveJson(this, schemaJson, descriptorJson)
p.runParseJson()