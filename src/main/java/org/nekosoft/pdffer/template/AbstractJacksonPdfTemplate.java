package org.nekosoft.pdffer.template;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nekosoft.pdffer.exception.MissingPayloadException;
import org.nekosoft.pdffer.exception.PayloadFormatException;

import java.util.Map;

import static org.nekosoft.pdffer.template.PdfTemplate.templateToString;

/**
 * <p>An abstract template that works with JSON based payloads and tries to turn them into
 * the payload class that the template can handle. The following {@code PdfferProducerBean}
 * methods require that the template extend this class
 * <ul>
 *     <li>{@code generatePdfDocumentByPathFromJsonMap}</li>
 *     <li>{@code generatePdfDocumentByPathFromJsonString}</li>
 *     <li>{@code generatePdfDocumentFromJsonMap}</li>
 *     <li>{@code generatePdfDocumentFromJsonMap}</li>
 *     <li>{@code generatePdfDocumentFromJsonString}</li>
 *     <li>{@code generatePdfDocumentFromJsonString}</li>
 * </ul>
 * @param <T> the type of the payload that this PDF template can handle
 */
public abstract class AbstractJacksonPdfTemplate<T> implements PdfTemplate<T> {

    private T payload;

    /**
     * <p>The array of bytes of the PDF document.</p>
     * <p>The {@link #generate()} method must create this array and populate it with the bytes of the PDF document that it
     * creates. It must be done before the method returns, so that it is available to clients via the
     * {@link #getPdfContent()} method once generation is complete.</p>
     */
    protected byte[] pdfContent;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public void setPayload(T payload) {
        this.payload = initPayload(payload);
    }

    @Override
    public boolean validate() throws MissingPayloadException {
        if (payload == null) {
            throw new MissingPayloadException();
        }
        return validatePayload();
    }

    /**
     * <p>Override this method to initialize the template and its parts once a payload has been set. For example,
     * you could configure the {@link #getJsonMapper() JSON mapper} or any formatters your template might use
     * based on information in the payload. The method also allows you to completely change the payload if you need to
     * and return a different one to be set into the template.</p>
     * <p>This method is called by {@link #setPayload(Object)}, so there is no need to ever override
     * {@link #setPayload(Object) setPayload} itself.</p>
     * <p>The default implementation does nothing and then returns the same value it receives.</p>
     * @param payload the payload that is being set into the template
     * @return the payload to be set into the template (in most cases you will simply return the same value you receive)
     */
    protected T initPayload(T payload) {
        return payload;
    }

    /**
     * <p>Provide your validation logic for the template. Return {@code true} if the payload is valid and can be used for
     * PDF generation. This method is invoked by {@link #validate()} after it check that the payload is not null. There is
     * therefore no need to override {@link #validate() validate} itself.</p>
     * <p>The default implementation returns true unconditionally.</p>
     * @return {@code true} if the payload is valid and can be safely used to generate PDFs, {@code false} otherwise
     */
    protected boolean validatePayload() {
        return true;
    }

    @Override
    public byte[] getPdfContent() {
        return pdfContent;
    }

    /**
     * Returns the Jackson {@code ObjectMapper} that is used for converting payloads from JSON strings and Java maps to
     * the payload type of the template.
     * @return the Jackson {@code ObjectMapper} used by this template
     */
    protected ObjectMapper getJsonMapper() {
        return jsonMapper;
    }

    /**
     * <p>Takes a Java map and creates the payload for the template from it. The map must contain all the attributes required
     * by the payload class (as specified by the {@link #getPayloadClass()} method), and a Jackson
     * {@link #getJsonMapper() ObjectMapper} will be used for the conversion.</p>
     * <p>This will trigger a call to {@link #initPayload(Object)}</p>.
     * @param map the Java map representation of the payload
     * @throws PayloadFormatException if the map could not successfully be converted to a payload class instance
     */
    public void setPayloadMap(Map<String, Object> map) throws PayloadFormatException {
        try {
            setPayload(getJsonMapper().convertValue(map, getPayloadClass()));
        } catch (IllegalArgumentException e) {
            throw new PayloadFormatException(map, e);
        }
    }

    /**
     * <p>Takes a JSON string and creates the payload for the template from it. The JSON string must contain all the attributes required
     * by the payload class (as specified by the {@link #getPayloadClass()} method), and a Jackson
     * {@link #getJsonMapper() ObjectMapper} will be used for the conversion.</p>
     * <p>This will trigger a call to {@link #initPayload(Object)}</p>.
     * @param json the JSON representation of the payload
     * @throws PayloadFormatException if the JSON string could not successfully be converted to a payload class instance
     */
    public void setPayloadJson(String json) throws PayloadFormatException {
        try {
            setPayload(getJsonMapper().readValue(json, getPayloadClass()));
        } catch (JacksonException e) {
            throw new PayloadFormatException(json, e);
        }
    }

    @Override
    public String toString() {
        return templateToString(this);
    }
}
