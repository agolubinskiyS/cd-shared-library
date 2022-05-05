def call(params) {
    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'
    assert params.version ==~ $/(\d+)\.(\d+)\.(\d+)/$ : 'unexpected version format'
    assert params.model ==~ $/[A-Z][a-zA-Z]*/$ : 'unexpected model format'
} 