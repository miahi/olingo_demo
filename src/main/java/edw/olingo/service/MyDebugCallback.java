package edw.olingo.service;

import org.apache.olingo.odata2.api.ODataDebugCallback;

public class MyDebugCallback implements ODataDebugCallback {
    @Override
    public boolean isDebugEnabled() {
        boolean isDebug = true; // true|configuration|user role check
        return isDebug;
    }

}