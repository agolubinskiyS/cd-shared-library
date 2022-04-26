package cosas

class Utilities implements Serializable {

    // private static final long serialVersionUID
    def script
    

    Utilities(script) {
        this.script = script
    }

    void prueba(String msg) {
      script.echo msg
      script.sh("pwd")
      script.sh("ls")
      script.sh("./script.sh")
    }
    void post(Map params = [:]) {
      script.sh("echo ${params.name}")
    }
}