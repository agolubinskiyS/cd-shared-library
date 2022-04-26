package cosas

class Utilities implements Serializable {

    private static final long serialVersionUID
    def script
    

    Utilities(script) {
        this.script = script
    }

    void get(String msg) {
      //script.echo msg
      script.sh("echo algo")
      // this.log.debug ("isReleaseActio")
      //this.script.sh("echo algo")
      //this.script.sh(script: "echo algo", returnStdout: true).trim()
    }
}