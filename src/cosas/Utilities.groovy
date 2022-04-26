class Utilities {
  static def get(script) {
    
  }
}

package cosas

class Utilities implements Serializable {

    private final def script

    Utilities(def script) {
        this.script = script
    }

    def checkout(String repo) {
        this.script.sh "echo GET:"
    }
}