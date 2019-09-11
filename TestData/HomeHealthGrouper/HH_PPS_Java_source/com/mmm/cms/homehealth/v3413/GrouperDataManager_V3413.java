/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */
package com.mmm.cms.homehealth.v3413;

import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;
import com.mmm.cms.homehealth.proto.DiagnosisCodeIF;
import com.mmm.cms.homehealth.proto.DiagnosticGroupIF;
import com.mmm.cms.homehealth.proto.HomeHealthGrouperIF;
import com.mmm.cms.homehealth.v3312.GrouperDataManager_V3312;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * This class provides the ICD-9 Data management related to the Diagnosis codes,
 * the NRS Diagnosis codes, the Diagnostic Groups, the Etiology Pairs, and the
 * optional Payment Codes.
 *
 * The information is loaded from text files. Refer to the loading methods from
 * the super class.
 *
 *
 * @author 3M Health Information Systems for CMS Home Health
 * @see GrouperDataManager_V3312
 */
public class GrouperDataManager_V3413 extends GrouperDataManager_V3312 {

    public GrouperDataManager_V3413(HomeHealthGrouperIF homeHealthGrouper) {
        super(homeHealthGrouper);
    }

    /**
     * Finds the diagnostic group by name from list of groups
     *
     * @param diagGroups
     * @param name
     * @return
     */
    protected DiagnosticGroupIF findDiagnosticGroupByName(List<DiagnosticGroupIF> diagGroups, String name) {
        DiagnosticGroupIF diagGroup = null;

        for (DiagnosticGroupIF tmpDxGroup : diagGroups) {
            if (name.equalsIgnoreCase(tmpDxGroup.getDescription())) {
                diagGroup = tmpDxGroup;
                break;
            }
        }

        return diagGroup;
    }

    /**
     * This looks at the case mix description to determine if the item pertains
     * to primary only, and then collects the Diagnostic Group name
     *
     * @param casemixItems
     * @return
     */
    protected List<String> getPrimaryGroupNames(List<CaseMixAdjustmentItemIF> casemixItems) {
        final List<String> diagGroupNames = new ArrayList<String>();
        final Pattern pattern = Pattern.compile("=");
        String tokens[];
        int idx;

        for (CaseMixAdjustmentItemIF casemixItem : casemixItems) {
            tokens = pattern.split(casemixItem.getName());
            if ("Primary Diagnosis".equalsIgnoreCase(tokens[0].trim())) {
                // get rid of any [NOTES...] if any
                idx = tokens[1].indexOf('[');
                if (idx > -1) {
                    tokens[1] = tokens[1].substring(0, idx);
                }
                diagGroupNames.add(tokens[1].trim());
            }
        }
        return diagGroupNames;
    }

    /**
     * This calls the super init() and afterwards sets the Diagnostic Groups
     * alternate Primary scoring flags for version V3413 or newer.
     *
     * @param props
     * @throws Exception
     * @throws RemoteException
     */
    @Override
    public void init(Properties props) throws Exception, RemoteException {
        HomeHealthGrouperIF grouper;
        List<CaseMixAdjustmentItemIF> casemixItems;
        List<DiagnosticGroupIF> diagGroups;
        DiagnosticGroupIF diagGroup;
        List<String> diagGroupNames;

        super.init(props);

        grouper = super.getHomeHealthGrouper();

        // Clinical/Functional Diagnostic Groups 
        casemixItems = grouper.getCaseMixAdjustments();
        diagGroups = grouper.getDiagnosticGroups();
        diagGroupNames = getPrimaryGroupNames(casemixItems);
        for (String str : diagGroupNames) {
            diagGroup = findDiagnosticGroupByName(diagGroups, str);
            if (diagGroup != null) {
                diagGroup.setAlternatePrimaryScorable(true);
                /*
		System.out.println("Grouper Version: " + grouper.getVersion() + 
                        ", DG: " + diagGroup.getId() + " - is AlternatePrimaryScorable");
                 */
            }
        }

        // NRS Diagnostic Groups 
        casemixItems = grouper.getNRSCaseMixAdjustments();
        diagGroups = grouper.getDiagnosticGroupsNRS();
        diagGroupNames = getPrimaryGroupNames(casemixItems);
        for (String str : diagGroupNames) {
            diagGroup = findDiagnosticGroupByName(diagGroups, str);
            if (diagGroup != null) {
                diagGroup.setAlternatePrimaryScorable(true);
                /*
                System.out.println("Grouper Version: " + grouper.getVersion()
                        + ", NS DG: " + diagGroup.getId() + " - is AlternatePrimaryScorable");
                 */
            }
        }
    }

    /**
     * Parses the Diagnosis Code Type for primary awarding code. If the str is
     * "A" then the PrimaryAwardableVCode is set to true. This may through an
     * Exception * If the str is not "A", then is calls the
     * super.onParseDiagnosisCodeType()
     *
     * @param diag
     * @param str
     */
    @Override
    protected void onParseDiagnosisCodeAttributes(DiagnosisCodeIF diag, String str) {
        if ("A".equals(str)) {
            diag.setPrimaryAwardableCode(true);
        } else {
            super.onParseDiagnosisCodeAttributes(diag, str);
        }
    }
}
