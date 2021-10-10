package org.nekosoft.pdffer.exception;

/**
 * An exception indicating that there is no payload in the template instance. It means that the
 * {@link org.nekosoft.pdffer.template.PdfTemplate#setPayload(Object)} method has not yet been invoked but the
 * operation that was requested needed a payload to proceed.
 */
public class MissingPayloadException extends PdfTemplateException {
}
