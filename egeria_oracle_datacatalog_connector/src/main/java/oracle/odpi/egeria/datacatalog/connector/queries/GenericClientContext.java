package oracle.odpi.egeria.datacatalog.connector.queries;

import oracle.odpi.egeria.datacatalog.connector.mapper.ODCEgeriaMapper;

/**
 *
 */
public final class GenericClientContext {
    private final String queryTemplate;
    private final Object[] queryArgs;
    private final ODCEgeriaMapper mapper;
    
    
    public GenericClientContext(
            final String queryTemplate,
            final Object[] queryArgs,
            final ODCEgeriaMapper mapper) {
        this.queryTemplate = queryTemplate;
        this.queryArgs = queryArgs;
        this.mapper = mapper;
    }
    
    public String getQueryTemplate() {
        return queryTemplate;
        
    }
    
    public Object[] getQueryArgs() {
        return queryArgs;
    }
    
    public ODCEgeriaMapper getMapper() {
        return mapper;
    }
}
