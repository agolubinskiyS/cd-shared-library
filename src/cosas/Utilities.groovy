package cosas

class Utilities implements Serializable {

    // private final def script
    def script

    Utilities(script) {
        this.script = script
    }

    def get(String repo) {
      this.script.sh script: "echo hola"
      //script.sh("echo algo")
      // this.log.debug ("isReleaseActio")
      //this.script.sh("echo algo")
      //this.script.sh(script: "echo algo", returnStdout: true).trim()
      return "$repo"
    }
}