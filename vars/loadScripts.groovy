
def call(List scripts) {
  for (item in scripts) {
    def scriptcontents = libraryResource "scripts/${item}"    
    writeFile file: "${item}", text: scriptcontents 
    sh "chmod a+x ./${item}"
  }
} 