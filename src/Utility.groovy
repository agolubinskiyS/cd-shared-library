class Utility {
    def url = "https://httpbin.org/get"

    def get(url) {
        sh """
        curl -k -X GET --url $url \
        -H 'accept: */*'
        """
    }   

}


