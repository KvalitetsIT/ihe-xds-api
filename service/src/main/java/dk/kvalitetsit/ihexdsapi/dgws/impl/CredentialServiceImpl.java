package dk.kvalitetsit.ihexdsapi.dgws.impl;

import java.util.*;

import dk.kvalitetsit.ihexdsapi.dao.CredentialRepository;
import dk.kvalitetsit.ihexdsapi.dao.entity.CredentialInfoEntity;
import dk.kvalitetsit.ihexdsapi.dgws.CredentialInfo;
import dk.kvalitetsit.ihexdsapi.utility.ValutGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kvalitetsit.ihexdsapi.dgws.CredentialService;
import dk.kvalitetsit.ihexdsapi.dgws.DgwsSecurityException;
import dk.sosi.seal.vault.GenericCredentialVault;


public class CredentialServiceImpl implements CredentialService {


    private CredentialRepository credentialRepository;

    public CredentialServiceImpl(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }


    private static final String DEFAULT_OWNER = " ";
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private Map<String, List<String>> idsForOwner = new HashMap<>();
    private Map<String, CredentialInfo> credentialInfoForID = new HashMap<>();


    @Override
    public synchronized CredentialInfo createAndAddCredentialInfo(String owner, String id, String cvr, String organisation, String publicCertStr, String privateKeyStr) throws DgwsSecurityException {

        if (credentialInfoForID.containsKey(id) || this.credentialRepository.findCredentialInfoByID(id) != null) {
            throw new DgwsSecurityException(3, "A credential vault with id " + id + " is already registered.");
        }

        String ownerKey = DEFAULT_OWNER;
        if (owner != null && owner.trim().length() >= 0) {
            ownerKey = owner.trim();
        }

        if (ownerKey.equals(DEFAULT_OWNER)) {
            List<String> owning = null;
            if (!idsForOwner.containsKey(ownerKey)) {
                owning = new LinkedList<>();
            } else {
                owning = idsForOwner.get(ownerKey);
            }
            owning.add(id);
            idsForOwner.put(ownerKey, owning);
        }

        CredentialInfo credentialInfo = generateCredientialInfoFromKeys(cvr, organisation, publicCertStr, privateKeyStr);


        if (ownerKey.equals(DEFAULT_OWNER)) {
            credentialInfoForID.put(id, credentialInfo);
        }
        credentialRepository.saveCredentialsForID(new CredentialInfoEntity(ownerKey,
                id, cvr, organisation, publicCertStr, privateKeyStr));


        return credentialInfo;
    }

    private CredentialInfo generateCredientialInfoFromKeys(String cvr, String organisation, String publicCertStr,
                                                           String privateKeyStr) throws DgwsSecurityException {
        GenericCredentialVault generic = ValutGenerator.generateGenericCredentialVault(publicCertStr, privateKeyStr);
        CredentialInfo credentialInfo = new CredentialInfo(generic, cvr, organisation);

        return credentialInfo;
    }

    @Override
    public Collection<String> getIds(String owner) {

        List<String> result = new LinkedList<>();
        String ownerKey = null;


        result.addAll(idsForOwner.get(DEFAULT_OWNER));


        if (owner != null) {
            ownerKey = owner.trim();
            if (idsForOwner.containsKey(ownerKey)) {
                result.addAll(credentialRepository.FindListOfIDsForOwner(owner));
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
            credentialInfo = generateCredientialInfoFromKeys(credsEnitity.getCvr(),
                    credsEnitity.getOrganisation(), credsEnitity.getPublicCertStr(), credsEnitity.getPrivateKeyStr());
            return credentialInfo;

        } catch (DgwsSecurityException e) {
            throw new RuntimeException();

        }
    }




}
