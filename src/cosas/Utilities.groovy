package cosas

class Utilities implements Serializable {

    def script
    

    Utilities(script) {
        this.script = script
    }

    void get() {
      this.script.sh("echo hola")
      //script.sh("echo algo")
      // this.log.debug ("isReleaseActio")
      //this.script.sh("echo algo")
      //this.script.sh(script: "echo algo", returnStdout: true).trim()
      return "repo"
    }
}