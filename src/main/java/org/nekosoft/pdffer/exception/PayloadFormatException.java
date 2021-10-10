package org.nekosoft.pdffer.exception;

import java.util.Map;

/**
 * An exception indicating that an invalid payload representation has been provided to either
 * {@link org.nekosoft.pdffer.template.AbstractJacksonPdfTemplate#setPayloadJson(String) setPayloadJson} or
 * {@link org.nekosoft.pdffer.template.AbstractJacksonPdfTemplate#setPayloadMap(Map) setPayloadMap} and therefore
 * a payload instance could not be created.
 */
public class PayloadFormatException extends PdfTemplateException {
    private Map map;
    private String json;

    /**
     * Invoke this constructor when a JSON representation failed to create a payload instance.
     * @param json the failing JSON string
     * @param t the exception that was raised when trying to convert JSON to payload class
     */
    public PayloadFormatException(String json, Throwable t) {
        super(t);
        this.json = json;
        this.map = null;
    }


    /**
     * Invoke this constructor when a Java map representation failed to create a payload instance.
     * @param map the failing Java map
     * @param t the exception that was raised when trying to convert the map to payload class
     */
    public PayloadFormatException(Map map, Throwable t) {
        super(t);
        this.json = null;
        this.map = map;
    }

    /**
     * Returns the map that caused the failure, or {@code null} if the failure was caused by a JSON string
     * @return the failing map or {@code null}
     */
    public Map getPayloadMap() {
        return map;
    }

    /**
     * Returns the JSON string that caused the failure, or {@code null} if the failure was caused by a Java map
     * @return the failing JSON string or {@code null}
     */
    public String getPayloadJson() {
        return json;
    }

    /**
     * Returns the representation that caused the failure - it could be a JSON string or a Java map.
     * @return the failing JSON string or Java map
     */
    public Object getPayloadSource() {
        return map != null ? map : json;
    }
}
