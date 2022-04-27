import src.tools.Utilities


def params = [url: 'mi-url.com', tenant: 's000004', deploymentDescriptors: 'descriptor', model: 'basic', version: '11.0.1', service: 'grafana-eos']
def utilities = new src.tools.Utilities(this, params)

utilities.prueba("admin", "1234")