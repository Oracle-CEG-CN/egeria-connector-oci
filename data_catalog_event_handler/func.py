import io
import json
import logging
import sys
import oci
import requests
import yaml

from fdk import response

from oci.config import validate_config


def get_oci_config(env):
    '''
    Extract the oci configuration values from the given environment object.
    '''
    result = {}

    try:
        config_url = env['data_catalog_event_handler_config_url']
    except KeyError as missing_config_url:
        raise Exception('Missing configuration parameter ''data_catalog_event_handler_config_url''') from missing_config_url

    try:
        config_response = requests.get(config_url)
        if config_response.status_code == 200:
            logging.info('Got configuration data from %s.' % config_url)
            app_config = yaml.unsafe_load(io.StringIO(config_response.text))
            logging.debug('read app_config: %s' % (json.dumps(app_config)))
            result = app_config['oci_conf']
    except Exception as read_config_error:
        raise Exception('Error reading app config.') from read_config_error

    return result
        

def do_catalog_job(data_catalog_client, compartment_id, data_catalog_id):
    '''
    '''
    catalogs = data_catalog_client.list_catalogs(compartment_id).data
    for catalog_summary in catalogs:
        logging.debug('id: %s: %s' % (catalog_summary.id, catalog_summary.display_name))


def handle_harvest_end(event, oci_config):
    '''
    Handler function for harvest-end event.
    :param event the event object containing additional information in its 'data' member
    :param oci_config configuration used to create a DataCatalogClient instance.
    '''
    data_catalog_client = oci.data_catalog.DataCatalogClient(oci_config)
    event_data = event["data"]
    compartment_id = event_data["compartmentId"]
    data_catalog_id = event_data["resourceId"]
    do_catalog_job(data_catalog_client, compartment_id, data_catalog_id)

ROUTING = {
    "com.oraclecloud.datacatalog.harvestjob.end": handle_harvest_end
}

def route_event(event, oci_config):
    '''
    Used to route events based on the 'eventType' member of the given 'event'.
    :param event the event for which to route to our specific handler function
    :param oci_config configuration to create the appropriate Oracle Cloud Infrastructure client.
    '''
    event_type = event['eventType']
    try:
        handler = ROUTING[event_type]
    except KeyError as key_error:
        raise Exception('No routing for eventType %s available.' % (eventType)) from key_error

    try:
        handler(event, oci_config)
    except Exception as handler_error:
        raise Exception('Error handling event for eventType %s.' % (eventType)) from handler_error
    

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
        
    try:
        try:
            validate_config(oci_config)
        except Exception as validate_config_error:
            raise Exception('Error validating oci_config.') from validate_config_error

        if data != None:
            try:
                received_event = json.loads(data.getvalue())
            except Exception as json_loads_error:
                raise Exception('Error parsing request data: %s.' % (data.getvalue())) from json_loads_error

            try:
                route_event(received_event, oci_config)
            except Exception as route_event_error:
                raise Exception('Error while handling event.') from route_event_error


    except Exception as e:
        response_json['error'] = str(e)
        logging.error('Error while calling handler.', exc_info=e)


    return response.Response(
        ctx, response_data=json.dumps(response_json),
        headers={"Content-Type": "application/json"}
    )
