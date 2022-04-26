class Utilities implements Serializable {
  def steps
  Utilities(steps) {this.steps = steps}
  def mvn() {
    steps.sh "echo GET"
  }
}