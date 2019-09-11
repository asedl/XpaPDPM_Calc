/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CommonMessageText;
import com.mmm.cms.homehealth.proto.record.HomeHealthRecordIF;
import com.mmm.cms.homehealth.proto.RecordItemValidatorIF;
import com.mmm.cms.homehealth.proto.RecordValidatorFactoryIF;
import com.mmm.cms.homehealth.vut.oasis.OasisValidatorItems;
import com.mmm.cms.util.Initializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 3M Health Information Systems for CMS Home Health
 */
public class RecordValidatorFactory implements Initializable, RecordValidatorFactoryIF {

    public final static String PROPERTY_RECORD_FACTORY_CLASS = "record.validator.factory.class";
    private static RecordValidatorFactory myInstance;

    private RecordValidatorFactoryIF validatorFactory;
    private String factoryClassName;

    private RecordValidatorFactory() {
    }

    /**
     * One first call to this method, the properties must not be null because it
     * is used to initialize the instance. After the first call, then properties
     * can be null, since only one factory can be created per run of the
     * software
     *
     * @param properties - non-null on first call, all subsequent calls can be
     * null
     *
     * @return record validator factory
     *
     * @throws Exception
     */
    public static RecordValidatorFactoryIF getInstance(Properties properties) throws Exception {
        if (myInstance == null) {
            myInstance = new RecordValidatorFactory();
            myInstance.init(properties);
        }

        return myInstance;
    }

    public void init(Properties properties) throws Exception {
        if (this.validatorFactory == null) {
            try {
                Class clazz;

                this.factoryClassName = properties.getProperty(PROPERTY_RECORD_FACTORY_CLASS, OasisValidatorItems.class.getName());

                clazz = Class.forName(factoryClassName);
                this.validatorFactory = (RecordValidatorFactoryIF) clazz.newInstance();
                Logger.getLogger(getClass().getName()).log(Level.INFO,
                        CommonMessageText.EVENT_MSG_HEADER + " - RecordValidatorFactory set to class: {0}", this.factoryClassName);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                        CommonMessageText.EVENT_MSG_HEADER + " - RecordValidatorFactory class NOT FOUND: {0}", this.factoryClassName);
                throw ex;
            }
        }
    }

    /**
     * this gets the validation items based on the validator factory loaded during
     * the initial call to the getInstance().  If no class was loaded, this will
     * throw a Null Pointer.
     * 
     * @param record
     * @return non-null array of Record Item Validators
     */
    public RecordItemValidatorIF[] getValidationItems(HomeHealthRecordIF record) {
        return this.validatorFactory.getValidationItems(record);
    }

}
