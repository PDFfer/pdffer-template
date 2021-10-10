package org.nekosoft.pdffer.template;

import static org.nekosoft.pdffer.template.PdfTemplateComponent.GROUP_SEPARATOR;
import static org.nekosoft.pdffer.template.PdfTemplateComponent.ROOT_REGISTRY;

/**
 * <p>This interface is implemented by classes that want to be added to the PDFfer registry as PDF templates.
 * PDF template classes must also be annotated with the {@link PdfTemplateComponent} stereotype, which allows
 * developers to provide the {@link PdfTemplateComponent#name() name} of the template and to indicate the
 * {@link PdfTemplateComponent#scope() scope } that the template should be configured for in the registry context.
 * Unless specified otherwise, the scope will be "prototype".</p>
 * <p>The lifecycle of a template is meant to be as follows
 * <ul>
 *     <li>created as a Spring bean in the PDFfer Registry context when needed to generate a PDF document</li>
 *     <li>the PDF payload is set with the {@link #setPayload(Object)} method</li>
 *     <li>the payload is validated with the {@link #validate()} method</li>
 *     <li>the {@link #generate()} method is called to process the payload and create the actual PDF document</li>
 *     <li>the bytes of the PDF are retrieved with the {@link #getPdfContent()} method by the requester</li>
 * </ul>
 *<p>Mostly, you will probably want to use the {@link AbstractJacksonPdfTemplate} class for your templates, rather than
 * implement this interface from scratch.</p>
 */
public interface PdfTemplate<T> {

    /**
     * Makes a template path string from group and name. It uses the {@link PdfTemplateComponent#GROUP_SEPARATOR} string
     * for that.
     * @param group the name of the template group
     * @param name the name of the template
     * @return the template path for the given group and name
     */
    static String getTemplatePath(String group, String name) {
        if (ROOT_REGISTRY.equals(group)) return name;
        else return group + GROUP_SEPARATOR + name;
    }

    /**
     * Returns the template path string for the given template. It uses the {@link PdfTemplateComponent#GROUP_SEPARATOR}
     * string for that.
     * @param template the template of which the path is required
     * @return the template path for the given template
     */
    static String getTemplatePath(PdfTemplate<?> template) {
        PdfTemplateComponent annotation = template.getClass().getAnnotation(PdfTemplateComponent.class);
        return getTemplatePath(annotation.group(), annotation.name());
    }

    /**
     * Returns the group and name components from a template path string.
     * @param path the template path to be de-composed
     * @return an array of strings with the group at element 0 and the name at element 1
     */
    static String[] splitTemplatePath(String path) {
        int groupSeparatorIdx = path.indexOf(GROUP_SEPARATOR);
        if ( groupSeparatorIdx < 0) {
            return new String[] {ROOT_REGISTRY, path};
        } else {
            String group = path.substring(0, groupSeparatorIdx);
            String name = path.substring(groupSeparatorIdx + GROUP_SEPARATOR.length());
            return new String[] {group, name};
        }
    }

    /**
     * Returns a string representation of a PDF template.
     * @param template the template to represent as a string
     * @return the string representing the template
     */
    static String templateToString(PdfTemplate<?> template) {
        PdfTemplateComponent annotation = template.getClass().getAnnotation(PdfTemplateComponent.class);
        return String.format("%s{from=%s,scope=%s}", getTemplatePath(annotation.group(), annotation.name()), template.getPayloadClass().getName(), annotation.scope());
    }

    /**
     * Returns the payload class that this template is able to handle. It takes instances of the
     * class returned by this method and produces PDFs from them.
     * @return the payload class
     */
    Class<T> getPayloadClass();

    /**
     * Returns the payload used by this instance to generate a PDF document. It must be of the type indicated by
     * {@link #getPayloadClass()}.
     * @return the payload
     */
    T getPayload();
    /**
     * Sets the payload that should be used by this instance to generate a PDF document. It must be of the type indicated
     * by {@link #getPayloadClass()}.
     * @param payload the payload
     */
    void setPayload(T payload);

    /**
     * <p>Checks that the payload is consistent with the requirements of the template.</p>
     * <p>The concept of "valid payload" is entirely up to each {@link PdfTemplate} instance.</p>
     * @return {@code true} if the payload is valid and can be safely used to generate PDFs, {@code false} otherwise
     */
    boolean validate();
    /**
     * Prepares the PDF document. It uses the payload set with {@link #setPayload(Object)} and assumes that the payload
     * has already been checked with {@link #validate()}. The generated PDF document can be accessed
     * with {@link #getPdfContent()} after this method returns.
     */
    void generate();
    /**
     * Returns the PDF document that was generated by {@link #generate()} as an array of bytes
     * @return the bytes of the generated PDF document
     */
    byte[] getPdfContent();
}
