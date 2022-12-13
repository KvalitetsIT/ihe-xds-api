package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.util.*;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.utility.ValutGenerator;
import org.openapitools.model.CredentialInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.GenericCredentialVault;

// TODO Consider making UUID A utility helper class (refactor)

public class CredentialServiceImpl implements CredentialService {


    private CredentialRepository credentialRepository;

    public CredentialServiceImpl(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }


    private static final String DEFAULT_OWNER = " ";
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private Map<String, List<String[]>> idsForOwner = new HashMap<>();
    private Map<String, CredentialInfo> credentialInfoForID = new HashMap<>();


    @Override
    public synchronized CredentialInfo createAndAddCredentialInfo(String owner, String displayName, String publicCertStr, String privateKeyStr) throws DgwsSecurityException {

        String id = UUID.randomUUID().toString();

        // Test purpose
        // TODO Probably not the correct way to do it
        if (displayName.equals("Sonja Bech")) {
            id = "D:" + "9038f177-d345-4c42-b2b4-6e27314e713e";
        }
        else if (displayName.equals("Default VOCES")) {
            id = "D:" + "9038f177-d345-4c42-b2b4-6e27314e714f";
        }

        if (credentialInfoForID.containsKey(id) || this.credentialRepository.findCredentialInfoByID(id) != null) {
            throw new DgwsSecurityException(3, "A credential vault with id " + id + " is already registered.");
        }

        String ownerKey = DEFAULT_OWNER;
        if (owner != null && owner.trim().length() >= 0) {
            ownerKey = owner.trim();
        }

        if (ownerKey.equals(DEFAULT_OWNER)) {
            List<String[]> owning = null;
            if (!idsForOwner.containsKey(ownerKey)) {
                owning = new LinkedList<>();
            } else {
                owning = idsForOwner.get(ownerKey);
            }
            // Emil
            owning.add(new String[]{id, displayName});
            idsForOwner.put(ownerKey, owning);
        }

        // Need displayName??
        CredentialInfo credentialInfo = generateCredientialInfoFromKeys(displayName, publicCertStr, privateKeyStr);


        if (ownerKey.equals(DEFAULT_OWNER)) {
            credentialInfoForID.put(id, credentialInfo);
        }
        credentialRepository.saveCredentialsForID(new CredentialInfoEntity(ownerKey,
                id, displayName, publicCertStr, privateKeyStr, credentialInfo.getSerialNumber(), credentialInfo.getType()));


        return credentialInfo;
    }

    private CredentialInfo generateCredientialInfoFromKeys(String displayName, String publicCertStr,
                                                           String privateKeyStr) throws DgwsSecurityException {
        GenericCredentialVault generic = ValutGenerator.generateGenericCredentialVault(publicCertStr, privateKeyStr);
        CredentialInfo credentialInfo = new CredentialInfo(generic, displayName);

        return credentialInfo;
    }

    @Override
    public List<String[]> getIds(String owner) {

        List<String[]> result = new LinkedList<>();
        String ownerKey = null;


        result.addAll(idsForOwner.get(DEFAULT_OWNER));

        if (owner != null) {
            ownerKey = owner.trim();
            try {
                result.addAll(credentialRepository.FindListOfIDsForOwner(ownerKey));
            } catch (Exception e) {

            }

        }
        return result;
    }

    @Override
    public CredentialInfo getCredentialInfoFromId(String id) {

        if (id == null) {
            return credentialInfoForID.get(idsForOwner.get(DEFAULT_OWNER).get(0));
        }

        CredentialInfoEntity credsEnitity = credentialRepository.findCredentialInfoByID(id);

        // get standard
        if (credsEnitity == null) {
            return credentialInfoForID.get(idsForOwner.get(DEFAULT_OWNER).get(0));
        }


        CredentialInfo credentialInfo = null;
        try {
            credentialInfo = generateCredientialInfoFromKeys(credsEnitity.getDisplayName(), credsEnitity.getPublicCertStr(), credsEnitity.getPrivateKeyStr());
            return credentialInfo;

        } catch (DgwsSecurityException e) {
            throw new RuntimeException();

        }
    }

    @Override
    public String getType(String id) {

        return credentialRepository.findCredentialInfoByID(id).getType();
    }

    @Override
    public String getSerialNumber(String id) {
        return credentialRepository.findCredentialInfoByID(id).getSerialNumber();
    }

    @Override
    public List<CredentialInfoResponse> populateResponses(String owner, CredentialInfoResponse.CredentialTypeEnum type) {
        Collection<String[]> ids = this.getIds(owner);
        List<CredentialInfoResponse> responses = new LinkedList<>();
        if (type == null) {
            for (String[] id : ids) {
                CredentialInfoResponse credentialInfoResponse = new CredentialInfoResponse();
                credentialInfoResponse.setId(id[0]);
                credentialInfoResponse.displayName(id[1]);
                credentialInfoResponse.setSubjectSerialNumber(this.getSerialNumber(id[0]));
                credentialInfoResponse.setCredentialType(
                        CredentialInfoResponse.CredentialTypeEnum.valueOf(this.getType(id[0])));
                responses.add(credentialInfoResponse);
            }
        } else {
            for (String[] id : ids) {
                if (CredentialInfoResponse.CredentialTypeEnum.valueOf(this.getType(id[0])) == type) {
                    {
                        CredentialInfoResponse credentialInfoResponse = new CredentialInfoResponse();
                        credentialInfoResponse.setId(id[0]);
                        credentialInfoResponse.displayName(id[1]);
                        credentialInfoResponse.setSubjectSerialNumber(this.getSerialNumber(id[0]));
                        credentialInfoResponse.setCredentialType(
                                CredentialInfoResponse.CredentialTypeEnum.valueOf(this.getType(id[0])));
                        responses.add(credentialInfoResponse);
                    }
                }

            }
        }

        return responses;
    }


}
