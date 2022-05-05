def call(List params) {
    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'
    //assert params.service ==~ $/http(s)?://.+?/$ : 'unexpected service format'
    assert params.model ==~ $/[0-9]+\.[0-9]+\.[0-9]/$ : 'unexpected model format'
    //assert params.version ==~ $/http(s)?://.+?/$ : 'unexpected version format'
} 