package org.nekosoft.pdffer.template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A stereotype annotation to mark a class as a PDF template for the PDFfer registry. PDF templates must reside in
 * the {@code org.nekosoft.PDFferTemplates} package and must implement the {@link org.nekosoft.pdffer.template.PdfTemplate}
 * interface.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PdfTemplateComponent {

	/**
	 * The default scope for all PDFfer templates ("prototype")
	 */
	String SCOPE_DEFAULT = "prototype";
	/**
	 * The singleton scope for templates that follow a different paradigm to the standard one. Other scopes are also
	 * possible but not checked by PDFfer, they are just passed on to the Spring framework.
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * The string representing the ROOT registry
	 */
	String ROOT_REGISTRY = "";
	/**
	 * The base package where all PDFfer templates reside.
	 */
	String BASE_PACKAGE = "org.nekosoft.PDFferTemplates";
	/**
	 * The separator used for composing a template path from a name and group. This allows any printable character to be
	 * used as either the name or the group. Composing and de-composing template paths should always only be done with
	 * the methods provided in {@link PdfTemplate}, namely {@link PdfTemplate#getTemplatePath(String, String) getTemplatePath} and
	 * {@link PdfTemplate#splitTemplatePath(String) splitTemplatePath}.
	 */
	String GROUP_SEPARATOR = "\u001D/\u001D";

	/**
	 * The group to which this template belongs.
	 *
	 * @return the group
	 */
	String group() default ROOT_REGISTRY;

	/**
	 * The name of this template.
	 *
	 * @return the name
	 */
	String name();

	/**
	 * The scope of this template (by default, it is "prototype", as set in the {@link #SCOPE_DEFAULT} constant).
	 *
	 * @return the scope
	 */
	String scope() default SCOPE_DEFAULT;

}
