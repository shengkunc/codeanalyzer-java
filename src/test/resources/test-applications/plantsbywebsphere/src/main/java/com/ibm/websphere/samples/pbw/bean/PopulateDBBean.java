package com.ibm.websphere.samples.pbw.bean;

import com.ibm.websphere.samples.pbw.utils.Util;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class PopulateDBBean {

    @Inject
    ResetDBBean dbBean;

    @PostConstruct
    public void initDB() {
        Util.debug("Initializing database...");
        dbBean.populateDB();
    }

}
