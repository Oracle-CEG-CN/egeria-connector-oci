import io
import json
import logging
import sys

from fdk import response

import oci

def get_oci_config(env):
    '''
    Extract the oci configuration values from the given environment object.
    '''
    result = {}

    for key in ['user', 'tenancy', 'region', 'key_content', 'fingerprint']:
        env_key = 'oci_config_%s' % key
        if env_key in env:
            value = env[env_key]
            if key == 'key_content':
                import base64
                value = base64.b64decode(value).decode('UTF-8')
            result[key] = value

    return result

def do_catalog_job(catalog_client):
    '''
    '''
    pass

def handler(ctx, data: io.BytesIO = None):
    '''
    Called as Oracle Function.

    :param ctx the calling context, including environment and configuration of the function
    :param data the data provided by the caller
    '''
    oci_config = get_oci_config(ctx.Config())


    response_json = {
        "python_version": sys.version_info,
    }
        
    from oci.config import validate_config
    try:
        validate_config(oci_config)
        try:
            do_catalog_job(oci.data_catalog.DataCatalogClient(oci_config))
        except Exception as catalog_job_exception:
            logging.error('Error during catalog job.', exc_info=catalog_job_exception)

    except Exception as config_validation_exception:
        response_json['error'] = str(e)
        logging.error('Error during oci config validation.', exc_info=config_validation_exception)


    return response.Response(
        ctx, response_data=json.dumps(response_json),
        headers={"Content-Type": "application/json"}
    )
