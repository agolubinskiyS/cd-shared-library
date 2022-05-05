def call(params) {
    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'
    //assert params.service ==~ $/http(s)?://.+?/$ : 'unexpected service format'
    assert params.model ==~ $/d*[.]\d*[.]\d*/$ : 'unexpected model format'
    //assert params.version ==~ $/http(s)?://.+?/$ : 'unexpected version format'
} 