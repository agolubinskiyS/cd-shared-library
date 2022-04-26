package cosas

class Utilities implements Serializable {

    private final def script

    Utilities(def script) {
        this.script = script
    }

    def get(String repo) {
        this.log.debug ("isReleaseActio")
        //this.script.sh("echo algo")
        //this.script.sh(script: "echo algo", returnStdout: true).trim()
    }
}