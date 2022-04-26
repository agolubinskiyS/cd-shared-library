package cosas

class Utilities implements Serializable {

    private final def script
    

    Utilities(def script) {
        this.script = script
    }

    void get() {
      this.script.sh script: "echo hola"
      //script.sh("echo algo")
      // this.log.debug ("isReleaseActio")
      //this.script.sh("echo algo")
      //this.script.sh(script: "echo algo", returnStdout: true).trim()
      return "repo"
    }
}