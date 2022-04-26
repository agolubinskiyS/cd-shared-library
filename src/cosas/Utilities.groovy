package cosas

class Utilities implements Serializable {

    // private static final long serialVersionUID
    def script
    

    Utilities(script) {
        this.script = script
    }

    void get(String msg) {
      script.echo msg
      script.sh("pwd")
      script.sh("./resources/script.sh")
    }
}