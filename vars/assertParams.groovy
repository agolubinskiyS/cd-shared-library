def call(params) {
    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'
    assert params.version ==~ $/(\d+)\.(\d+)\.(\d+)/$ : 'unexpected version format'
} 