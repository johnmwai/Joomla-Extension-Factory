package factory.legacy;

import com.fuscard.commons.FileWriter;
import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:40:01 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Model extends MVCClass {

    private String TableName = "";
    private Table table;

    public Model(String name, MVCClassType type, Component parent, Config config) {
        super(name, type, parent, config);
        String _parentFile = "";
        String _superClass = "";
        switch (configuration.type) {
            case Plain:
                _parentFile = "model";
                _superClass = "JModel";
                break;
            case Form:
                _parentFile = "modelform";
                _superClass = "JModelForm";
                break;
            case Admin:
            case IDE_Details:
                _parentFile = "modeladmin";
                _superClass = "JModelAdmin";

                break;
            case List:
            case IDE_List:
                _parentFile = "modellist";
                _superClass = "JModelList";
                break;
            default:
                throw new IllegalStateException("Unsupported model type");
        }
        parentFile = _parentFile;
        superClass = _superClass;
        String componentName = FileWriter.capitalize(parent.getName());
        className = componentName + "Model" + FileWriter.capitalize(name);
        folder = "models";
    }

    @Override
    public void writeBody() {
        if (isDetails()) {
            fw.pushLine(
                    "protected function allowEdit($data = array(), $key = 'id')");
            fw.pushLine("{");
            fw.pushLine(
                    "return JFactory::getUser()->authorise('core.edit', '" + parent.getInstallationName() + "." + getSingular().toLowerCase() + ".'.((int) isset($data[$key]) ? $data[$key] : 0)) or parent::allowEdit($data, $key);");
            fw.pushLine("}");
            fw.pushLine("public function getTable($type = '" + getTableName() + "', $prefix = '" + FileWriter.capitalize(
                    parent.getName()) + "Table', $config = array()) ");
            fw.pushLine("{");
            fw.pushLine("return JTable::getInstance($type, $prefix, $config);");
            fw.pushLine("}");
            fw.pushLine(
                    "public function getForm($data = array(), $loadData = true) ");
            fw.pushLine("{");
            fw.pushLine(
                    "$form = $this->loadForm('" + parent.getInstallationName() + "." + getSingular().toLowerCase() + "', '" + getSingular().toLowerCase() + "', array('control' => 'jform', 'load_data' => $loadData));");
            fw.pushLine("if (empty($form)) ");
            fw.pushLine("{");
            fw.pushLine("return false;");
            fw.pushLine("}");
            fw.pushLine("return $form;");
            fw.pushLine("}");
            fw.pushLine("public function getScript() ");
            fw.pushLine("{");
            fw.pushLine(
                    "return 'administrator/components/" + parent.getInstallationName() + "/models/forms/" + getSingular().toLowerCase() + ".js';");
            fw.pushLine("}");
            fw.pushLine("protected function loadFormData() ");
            fw.pushLine("{");
            fw.pushLine(
                    "$data = JFactory::getApplication()->getUserState('" + parent.getInstallationName() + ".edit." + getSingular().toLowerCase() + ".data', array());");
            fw.pushLine("if (empty($data)) ");
            fw.pushLine("{");
            fw.pushLine("$data = $this->getItem();");
            fw.pushLine("}");
            fw.pushLine("return $data;");
            fw.pushLine("}");
        } else if (isList() && table != null) {
            makeTable();
            String fields = "";
            Field pk = table.getPrimaryKey();
            Field[] fs = table.getRecord().getFields();
            for (Field f : fs) {

                if (f != pk && !(isSite() && (f.type == DataTypes.Params || f.type == DataTypes.Category))) {
                    if (!"".equals(fields) && fs.length > 1) {
                        fields += ",";
                    }
                    fields += f.getName();
                }
            }
            fw.pushLine("private $cols = \"" + fields + "\";");
            fw.pushLine("protected function getListQuery() {");
            fw.pushLine("$db = JFactory::getDBO();");
            fw.pushLine("$query = $db->getQuery(true);");
            String select = "";
            String leftjoin = "";
            int tables = 0;
            LinkedList<Table> tables1 = new LinkedList<Table>();
            for (Field f : fs) {
                if (!"".equals(select) && fs.length > 1) {
                    select += ",\n";
                }
                if (f.type == DataTypes.Sql) {
                    int t;
                    Table tt = f.getReferences();
                    if (tables1.contains(tt)) {
                        t = tables1.indexOf(tt) + 1;
                    } else {
                        t = ++tables;
                        tables1.add(tt);
                    }
                    select += "t" + t + "." + tt.getTitleField().name;
                    leftjoin += "$query->leftJoin('" + tt.getInstallationName() + " t" + t + " on t0." + f.name + " = t" + t + "." + tt.getPrimaryKey().name + "');\n";
                } else {
                    select += "t0." + f.getName();
                }
            }
            fw.pushLine("$query->select('" + select + "');");

            fw.pushLine("$query->from('" + getDatabaseTableName() + " t0');");
            fw.pushLine(leftjoin);
            fw.pushLine("return $query;");
            fw.pushLine("}");
            fw.pushLine("public function getTableHeaders() {");
            fw.pushLine(
                    "$mycols = " + parent.getHelperName() + "::translateColumns($this->cols, '" + TableName.toUpperCase() + "_NAME_');");
            fw.pushLine("if(!$mycols){");
            fw.pushLine("return false;");
            fw.pushLine("}");
            fw.pushLine(
                    "return \"<th>\".implode(\"</th><th>\",explode(\",\",$mycols)).\"</th>\";");
            fw.pushLine("}");
        }
    }

    private String getTableName() {
        return FileWriter.capitalize(singular);
    }

    private String getDatabaseTableName() {
        return table.getInstallationName();
    }

    private void makeTable() {
        if (table == null) {
            return;
        }
        table.setSingular(singular);
        table.setRoot(root);
        table.setPlural(plural);
        table.emitCode(isAdmin());
    }

    public void setTable(Table table) {
        TableName = table.getName();
        this.table = table;
    }

    public void requireTable() {
        if (table != null) {
            parent.addTable(table);
        }
    }
}
