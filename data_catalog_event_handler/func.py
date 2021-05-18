import io
import json
import logging
import sys
import oci
from fdk import response
from oci.config import validate_config


def handler(ctx, data: io.BytesIO = None):
    name = "World"


    oci_config = {}
    env = ctx.Config()

    for key in ['user', 'tenancy', 'region', 'key_content', 'fingerprint']:
        env_key = 'oci_config_%s' % key
        if env_key in env:
            value = env[env_key]
            if key == 'key_content':
                import base64
                value = base64.b64decode(value).decode('UTF-8')
            oci_config[key] = value


    validate_config(oci_config)

    catalog_client = oci.data_catalog.DataCatalogClient(oci_config)


    response_json = {
        "message": "Hello {0}".format(name),
        "python_version": sys.version_info,
        "oci_config": oci_config
    }
        
    return response.Response(
        ctx, response_data=json.dumps(response_json),
        headers={"Content-Type": "application/json"}
    )
