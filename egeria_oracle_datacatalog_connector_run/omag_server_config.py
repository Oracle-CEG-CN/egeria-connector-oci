#!/usr/bin/env python

import requests
import json
from cmd import Cmd

import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)


CONTEXT = {
    'protocol': 'https',
    'server': 'localhost',
    'port': '9443',
    'username': 'odc-proxy-admin',
    'omag_server_name': 'oracle-datacatalog-proxy' }


from oci_config import OCI_CONFIG


def get_metadata_collection_id():
    import subprocess
    proc = subprocess.Popen('./get_metadata_collection_id.sh', stdout=subprocess.PIPE)
    out, err = proc.communicate()
    return out.decode('UTF-8').strip()

class EgeriaCommand(Cmd):
    def __init__(self, _context):
        Cmd.__init__(self)
        self._context = _context



    def _base_url(self):
        return '%s://%s:%s' % (
                self._context['protocol'],
                self._context['server'],
                self._context['port'])

    def _server_admin_url(self):
        return '%s/open-metadata/admin-services/users/%s/servers/%s' % (
                self._base_url(),
                self._context['username'],
                self._context['omag_server_name'])

    def _server_url(self):
        return '%s/servers/%s/open-metadata/repository-services/users/%s' % (
                self._base_url(),
                self._context['omag_server_name'],
                self._context['username'])

    def do_show_config(self, line):
        url = '%s/configuration' % (self._server_admin_url())
        response = requests.get(url, verify=False)
        print(json.dumps(response.json(), indent=4))

    def do_set_server_type(self, line):
        url = '%s/server-type?typeName="Oracle Data Catalog Proxy"' % (self._server_admin_url())
        response = requests.post(url, verify=False)
        print(json.dumps(response.json(), indent=4))

    def do_register_connector(self, line):
        url = '%s/local-repository/mode/repository-proxy/connection' % (self._server_admin_url())
        data = {
            'class': 'Connection',
            'connectorType': {
                'class': 'ConnectorType',
                'connectorProviderClassName': 'oracle.odpi.egeria.datacatalog.connector.OracleDataCatalogOMRSRepositoryConnectorProvider'
            },
            'configurationProperties': OCI_CONFIG
        }
        response = requests.post(url, json=data, verify=False)
        if response.status_code == 200:
            print(json.dumps(response.json(), indent=4))
        else:
            response.raise_for_status()

    def do_start_connector(self, line):
        url = '%s/instance' % (self._server_admin_url())
        response = requests.post(url, verify=False)
        print(json.dumps(response.json(), indent=4))

    def do_list_all_types(self, line):
        url = '%s/types/all' % (self._server_url())
        response = requests.get(url, verify=False)
        print(json.dumps(response.json(), indent=4))


    def do_get_entity(self, line):
        metadata_collection_id = get_metadata_collection_id()
        url = '%s/instances/entity/%s.data_asset.fd732d11-80e5-4f1b-8288-9102e147151d' % (self._server_url(), metadata_collection_id)
        response = requests.get(url, verify=False)
        print(json.dumps(response.json(), indent=4))

    def do_exit(self, line):
        return True
        


def main():
    EgeriaCommand(CONTEXT).cmdloop()


if __name__ == '__main__':
    main()
