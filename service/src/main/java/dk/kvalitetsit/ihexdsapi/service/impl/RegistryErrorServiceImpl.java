package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.service.RegistryErrorService;
import org.openapitools.model.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;

import java.util.LinkedList;
import java.util.List;

public class RegistryErrorServiceImpl implements RegistryErrorService {
    private List<RegistryError> errors;

    public RegistryErrorServiceImpl() {
        errors = new LinkedList<>();
    }

    @Override
    public void createListOfErrors(QueryResponse queryResponse) {
        for (ErrorInfo errorInfo : queryResponse.getErrors()) {
            RegistryError registryError = new RegistryError();

            registryError.setCodeContext(errorInfo.getCodeContext());
            registryError.setErrorCode(errorInfo.getErrorCode().getOpcode() + " , " + errorInfo.getErrorCode().name());
            registryError.setSeverity(errorInfo.getSeverity().getOpcode30()+ " , " +errorInfo.getSeverity().name());

            errors.add(registryError);
        }
    }

    @Override
    public List<RegistryError> getList(){
        LinkedList<RegistryError> temp = new LinkedList<>();
        temp.addAll(errors);

        return temp;
    }

    @Override
    public void cleanup() {
        errors.clear();
    }
}
