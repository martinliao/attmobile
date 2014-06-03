package net.mindeos.attmobile.webservice;

import org.json.JSONArray;

/**
 * Class to call the attcontrol webservice methods
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AttHomeCaller extends AttCaller {
    private static AttHomeCaller instance = new AttHomeCaller();

    private static final String FUNCTIONPREFIX = "mod_atthome_";

    private AttHomeCaller() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AttHomeCaller getInstance() {
        return AttHomeCaller.instance;
    }


    /**
     * Calls a service returning a JSON array.
     *
     * @param wsfunction the function
     * @param args the arguments for this function
     * @return the result array
     */
    public static JSONArray call(String wsfunction, String... args)
            throws Exception {
        return serviceCall(Att.getSite(), Att.getAttHomeToken(), FUNCTIONPREFIX + wsfunction, args);
    }


    /**
     * Calls a service with no results.
     *
     * @param wsfunction the function
     * @param args the arguments for this function
     */
    public static void callNoResults(String wsfunction, String... args)
            throws Exception {

        serviceCallNoResults(Att.getSite(), Att.getAttHomeToken(), FUNCTIONPREFIX + wsfunction, args);
    }


}

