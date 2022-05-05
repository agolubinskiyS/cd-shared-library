def call(params) {
    assert params.url ==~ $/http(s)?://.+?/$ : 'unexpected CCT url format'
    assert params.version ==~ $/(\d+)\.(\d+)\.(\d+)/$ : 'unexpected version format: Should be a.b.c'
    assert params.model ==~ $/[a-z]*/$ : 'unexpected model format: Should be lowercase'
} 