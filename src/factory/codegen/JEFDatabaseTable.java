package factory.codegen;

import factory.codegen.app.Project;

/**
 *
 * @author John Mwai
 */
public class JEFDatabaseTable {

    private String name;
    private CodeGeneraror.JEFField[] fields;
    private Project project;
    private String alias;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = CodeGeneraror.normalizeName(name);
    }

    /**
     * @return the fields
     */
    public CodeGeneraror.JEFField[] getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(CodeGeneraror.JEFField[] fields) {
        this.fields = fields;
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * A string to be displayed
     *
     * @return A string that can be used to display this database Table
     */
    @Override
    public String toString() {
        return getAlias() == null || "".equals(getAlias()) ? getName() : getAlias();
    }
}
