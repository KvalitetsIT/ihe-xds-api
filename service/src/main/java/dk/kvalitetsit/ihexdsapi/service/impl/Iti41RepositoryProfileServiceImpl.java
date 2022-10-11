package dk.kvalitetsit.ihexdsapi.service.impl;

import dk.kvalitetsit.ihexdsapi.configuration.Iti41Configuration;
import dk.kvalitetsit.ihexdsapi.dao.entity.Iti41RepositoryProfile;
import dk.kvalitetsit.ihexdsapi.service.Iti41RepositoryProfileService;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


public class Iti41RepositoryProfileServiceImpl implements Iti41RepositoryProfileService {

    private static Logger LOGGER = LoggerFactory.getLogger(Iti41RepositoryProfileServiceImpl.class);
    @Autowired
    private BeanFactory beanFactory;




    private Iti41Configuration repositoryProfileIti41Configuration;

    private List<Iti41RepositoryProfile> repositoryProfileList;

    public Iti41RepositoryProfileServiceImpl(BeanFactory beanFactory,  Iti41Configuration repositoryProfileIti41Configuration) {
        this.beanFactory = beanFactory;
        this.repositoryProfileIti41Configuration = repositoryProfileIti41Configuration;
        init();

    }
    public Iti41RepositoryProfileServiceImpl(Iti41Configuration repositoryProfileIti41Configuration) {
        this.repositoryProfileIti41Configuration = repositoryProfileIti41Configuration;

    }



    @Override
    @PostConstruct
    public void init() {
        repositoryProfileList = new ArrayList<Iti41RepositoryProfile>();
        StringBuilder endpoints = new StringBuilder();
        StringBuilder names = new StringBuilder();

        for (Iti41Configuration.Repo repo: repositoryProfileIti41Configuration.getRepo()) {
            Iti41RepositoryProfile repositoryProfileDto = new Iti41RepositoryProfile();
            repositoryProfileDto.setDgws(repo.isDgwsenabled());
            repositoryProfileDto.setRepositoryuniqueid(repo.getRepositoryuniqueid());
            repositoryProfileDto.setUpdateallowed(repo.isUpdateallowed());
            repositoryProfileDto.setEndpoint(repo.getEndpoint());
            repositoryProfileDto.setName(repo.getName());
            Iti41PortType iti41PortType = beanFactory.getBean(Iti41PortType.class, repositoryProfileDto.getEndpoint(), repositoryProfileDto.isDgws());
            repositoryProfileDto.setIti41PortType(iti41PortType);

            for (String docType: repo.getValiddoctypes()) {
                if (!docType.isEmpty()) {
                    repositoryProfileDto.addDocType(docType);
                }
            }

            repositoryProfileList.add(repositoryProfileDto);
            LOGGER.info("Configuration loaded is: " + repositoryProfileDto.toString());

            if (endpoints.length() > 0) {
                endpoints.append(";");
                names.append(";");
            }
            endpoints.append(repositoryProfileDto.getEndpoint());
            names.append(repositoryProfileDto.getName());

        }

    }


    @Override
    public boolean isUpdateAllowedForRepositoryUniqueId(String repositoryUniqueId) {
        if (repositoryUniqueId == null) {
            return false;
        }
        Iti41RepositoryProfile repositoryProfileIti41Dto = getRepositoryProfileIti41DtoFromRepositoryUniqueId(repositoryUniqueId);
        if (repositoryProfileIti41Dto != null && repositoryProfileIti41Dto.isUpdateallowed()) {
            LOGGER.debug("Repository has update allowed");
            return true;
        }
        return false;

    }
    @Override
    public boolean isDeprecateAllowedForRepositoryUniqueId(String repositoryUniqueId) {
        if (repositoryUniqueId == null) {
            return false;
        }
        Iti41RepositoryProfile repositoryProfileIti41Dto = getRepositoryProfileIti41DtoFromRepositoryUniqueId(repositoryUniqueId);
        if (repositoryProfileIti41Dto != null && repositoryProfileIti41Dto.isUpdateallowed()) { //if we are allowed to update, then we may deprecate
            LOGGER.debug("Repository has deprecate allowed");
            return true;
        }
        return false;
    }
    @Override
    public Iti41RepositoryProfile getRepositoryFromUniqueId(String repositoryId) {
        return getRepositoryProfileIti41DtoFromRepositoryUniqueId(repositoryId);
    }
    @Override
    public Iti41RepositoryProfile getRepositoryFromEndpoint(String endpoint) {
        return getRepositoryProfileIti41DtoFromRepositoryEndpoint(endpoint);
    }

    private Iti41RepositoryProfile getRepositoryProfileIti41DtoFromRepositoryUniqueId(String id) {
        for (Iti41RepositoryProfile repositoryProfileIti41Dto: repositoryProfileList) {
            if (repositoryProfileIti41Dto.getRepositoryuniqueid().equals(id)) {
                LOGGER.debug("Finding repository by id: " + id + " Found is: " + repositoryProfileIti41Dto.getName());
                return repositoryProfileIti41Dto;
            }
        }
        return null;
    }

    private Iti41RepositoryProfile getRepositoryProfileIti41DtoFromRepositoryEndpoint(String endpoint) {
        for (Iti41RepositoryProfile repositoryProfileIti41Dto: repositoryProfileList) {
            if (repositoryProfileIti41Dto.getEndpoint().equals(endpoint)) {
                LOGGER.debug("Finding repository by id: " + endpoint + " Found is: " + repositoryProfileIti41Dto.getName());
                return repositoryProfileIti41Dto;
            }
        }
        return null;
    }
    @Override
    public List<Iti41RepositoryProfile> getRepositoryProfileList() {
        return repositoryProfileList;
    }


}
