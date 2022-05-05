def call(){
    node('cloner') {
        stage("Do Algo") {
            String cosas = 'cosas cosas'
            print('-----------')
            print(cosas)
        }
    }
}