package cosas

class Utilities implements Serializable {

    private final def script

    Utilities(def script) {
        this.script = script
    }

    def get(String repo) {
        this.script.sh "echo GET:"
    }
}