package factory.legacy;

import com.fuscard.commons.FileWriter;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 28, 2012 -- 5:04:06 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Helper {

    private final Component parent;

    public Helper(Component component) {
        this.parent = component;
    }

    public void emit() {
        String helper = parent.makeFile("admin.helpers|" + parent.getName().toLowerCase() + ".php");
        FileWriter fw = parent.obtainWriter(helper);
        fw.pushLine("<?php");
        fw.pushLine("defined('_JEXEC') or die;");
        fw.pushLine("/**");
        parent.writeHeader(fw);
        fw.pushLine("*/");
        fw.pushLine("abstract class " + parent.getHelperName() + " {");
        fw.pushLine("public static function addSubmenu($submenu) {");
        fw.pushLine("JSubMenuHelper::addEntry(JText::_('Home'), 'index.php?option=" + parent.getInstallationName() + "', $submenu == 'home');");
        if (parent.getAdminViewIndex() != null) {
            fw.pushLine("JSubMenuHelper::addEntry(JText::_('Index'), 'index.php?option=" + parent.getInstallationName()
                    + "&view=" + parent.getAdminViewIndex().getName().toLowerCase() + "', $submenu == '"
                    + parent.getAdminViewIndex().getName().toLowerCase() + "');");
        }
        if (parent.getRootPanel() != null) {
            fw.pushLine("JSubMenuHelper::addEntry(JText::_('Root'), 'index.php?option=" + parent.getInstallationName()
                    + "&view=" + parent.getRootPanel().getName().toLowerCase() + "', $submenu == '"
                    + parent.getRootPanel().getName().toLowerCase() + "');");
        }
        fw.pushLine("JSubMenuHelper::addEntry(JText::_('Categories'), 'index.php?option=com_categories&view=categories&extension=" + parent.getInstallationName() + "', $submenu == 'categories');");
        fw.pushLine("$document = JFactory::getDocument();");
        fw.pushLine("$document->addStyleDeclaration('.icon-48-" + parent.getName().toLowerCase() + " {background-image: url(components/" + parent.getInstallationName() + "/assets/images/icons/large_icon-48x48.png);}');");
        fw.pushLine("if ($submenu == 'categories') {");
        fw.pushLine("$document->setTitle(JText::_('" + FileWriter.capitalize(parent.getName()) + " Categories'));");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("public static function getActions($view = '', $itemid = 0) {");
        fw.pushLine("$user = JFactory::getUser();");
        fw.pushLine("$result = new JObject;");
        fw.pushLine("if (empty($itemid)) {");
        fw.pushLine("$assetName = '" + parent.getInstallationName() + "';");
        fw.pushLine("} else {");
        fw.pushLine("$assetName = '" + parent.getInstallationName() + ".' . $view . '.' . (int) $itemid;");
        fw.pushLine("}");
        fw.pushLine("$actions = array(");
        fw.pushLine("'core.admin', 'core.manage', 'core.create', 'core.edit', 'core.delete'");
        fw.pushLine(");");
        fw.pushLine("foreach ($actions as $action) {");
        fw.pushLine("$result->set($action, $user->authorise($action, $assetName));");
        fw.pushLine("}");
        fw.pushLine("return $result;");
        fw.pushLine("}");
        fw.pushLine("public static function translateColumns($cols, $suffix = \"\") {");
        fw.pushLine("if ($cols) {");
        fw.pushLine("$colarray = explode(\",\", $cols);");
        fw.pushLine("foreach ($colarray as $key => $val) {");
        fw.pushLine("$val = trim(strtoupper($val));");
        fw.pushLine("$colarray[$key] = JText::_(\"" + parent.getInstallationName().toUpperCase() + "_DATABASE_COLUMN_\" . $suffix . $val);");
        fw.pushLine("}");
        fw.pushLine("return implode(\",\", $colarray);");
        fw.pushLine("}");
        fw.pushLine("return false;");
        fw.pushLine("}");
        fw.pushLine("public static function getMyNameSpacePrefix() {");
        fw.pushLine("return \"" + parent.getInstallationName() + "." + parent.getName().toLowerCase() + "\";");
        fw.pushLine("}");
        fw.pushLine("public static function getParam($name = null) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("$db->setQuery('SELECT params FROM #__extensions WHERE name = \"" + parent.getInstallationName() + "\"');");
        fw.pushLine("$params = json_decode($db->loadResult(), true);");
        fw.pushLine("if ($name !== null)");
        fw.pushLine("return isset($params[$name]) ? $params[$name] : null;");
        fw.pushLine("else");
        fw.pushLine("return $params;");
        fw.pushLine("}");
        fw.pushLine("public static function setParams(");
        fw.pushLine("$param_array");
        fw.pushLine(", $select_query = 'SELECT params FROM #__extensions WHERE name = \"" + parent.getInstallationName() + "\"'");
        fw.pushLine(", $ave_query = 'UPDATE #__extensions SET params = <PARAMS> WHERE name = \"" + parent.getInstallationName() + "\"'");
        fw.pushLine(") {");
        fw.pushLine("if (count($param_array) > 0) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("$db->setQuery($select_query);");
        fw.pushLine("$params = json_decode($db->loadResult(), true);");
        fw.pushLine("foreach ($param_array as $name => $value) {");
        fw.pushLine("$params[(string) $name] = (string) $value;");
        fw.pushLine("}");
        fw.pushLine("$paramsString = json_encode($params);");
        fw.pushLine("$paramsString = $db->quote($paramsString);");
        fw.pushLine("$ave_query = preg_replace(\"/<PARAMS>/i\", $paramsString, $ave_query);");
        fw.pushLine("$db->setQuery($ave_query);");
        fw.pushLine("$db->query();");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("public static function checkToken($sn) {");
        fw.pushLine("$name = $sn['nm'];");
        fw.pushLine("$id = $sn['id'];");
        fw.pushLine("$session = JFactory::getSession();");
        fw.pushLine("$snam = $session->getName();");
        fw.pushLine("$sid = $session->getId();");
        fw.pushLine("if ($name == $session->getName() && $id == $session->getId()) {");
        fw.pushLine("return true;");
        fw.pushLine("}");
        fw.pushLine("return false;");
        fw.pushLine("}");
        fw.pushLine("public static function log($message) {");
        fw.pushLine("$logdir = " + parent.getHelperName() + "::getLogDir();");
        fw.pushLine("if (!is_dir($logdir)) {");
        fw.pushLine(FileWriter.capitalize(parent.getName()) + "Helper::makeLogDir();");
        fw.pushLine("}");
        fw.pushLine("$file = " + parent.getHelperName() + "::getLogFile();");
        fw.pushLine("$log = fopen((string) $file, 'a');");
        fw.pushLine("fwrite($log, date(\"F j, Y, g:i a\") . \" --> \" . $message . \"\n\");");
        fw.pushLine("fclose($log);");
        fw.pushLine("}");
        fw.pushLine("private static function getLogDir() {");
        fw.pushLine("return JPATH_ADMINISTRATOR . DS . \"" + parent.getName().toLowerCase() + "\" . DS . \"logs\";");
        fw.pushLine("}");
        fw.pushLine("private static function makeLogDir() {");
        fw.pushLine("$" + parent.getName().toLowerCase() + "_dir = JPATH_ADMINISTRATOR . DS . \"" + parent.getName().toLowerCase() + "\";");
        fw.pushLine("$logs_dir = $" + parent.getName().toLowerCase() + "_dir . DS . \"logs\";");
        fw.pushLine("if (!is_dir($" + parent.getName().toLowerCase() + "_dir)) {");
        fw.pushLine("mkdir($" + parent.getName().toLowerCase() + "_dir);");
        fw.pushLine("}");
        fw.pushLine("if (!is_dir($logs_dir)) {");
        fw.pushLine("mkdir($logs_dir);");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("public static function getLogFile() {//administrator");
        fw.pushLine("return " + parent.getHelperName() + "::getLogDir() . DS . 'log.txt';");
        fw.pushLine("}");
        fw.pushLine("public static function get($type, $id) {");
        fw.pushLine("$table = \"\";");
        fw.pushLine("$pk = \"\";");
        fw.pushLine("switch ($type) {");
        Table[] tables = parent.getTables();
        for (Table t : tables) {
            fw.pushLine("case \"" + t.getTitleField().getName() + "\" :");
            fw.pushLine("$table = \"" + t.getInstallationName() + "\";");
            fw.pushLine("$pk = \"" + t.getPrimaryKey().getName() + "\";");
            fw.pushLine("break;");
        }
        fw.pushLine("}");
        fw.pushLine("if (empty($table) || empty($pk)) {");
        fw.pushLine("return null;");
        fw.pushLine("}");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("$db->setQuery('select *  from ' . $db->nameQuote($table) . '  where ' . $db->nameQuote($pk) . ' = ' . $db->quote($id));");
        fw.pushLine("$db->query();");
        fw.pushLine("return $db->loadAssoc();");
        fw.pushLine("}");
        fw.pushLine("public static function query($table, $col, $value, $result = null, $query_type = \"select\", $update_col = null, $update_val = null) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("switch ($query_type) {");
        fw.pushLine("case \"select\":");
        fw.pushLine("$db->setQuery('select *  from '");
        fw.pushLine(". $db->nameQuote($table) .");
        fw.pushLine("'  where ' . $db->nameQuote($col) .");
        fw.pushLine("' = ' . $db->quote($value));");
        fw.pushLine("break;");
        fw.pushLine("case \"update\":");
        fw.pushLine("$db->setQuery('update '");
        fw.pushLine(". $db->nameQuote($table) .");
        fw.pushLine("'set ' . $db->nameQuote($update_col) .");
        fw.pushLine("' = ' . $db->quote($update_val) .");
        fw.pushLine("'  where ' . $db->nameQuote($col) .");
        fw.pushLine("' = ' . $db->quote($value));");
        fw.pushLine("break;");
        fw.pushLine("}");
        fw.pushLine("$db->query();");
        fw.pushLine("switch ($result) {");
        fw.pushLine("case 'assoc':");
        fw.pushLine("return $db->loadAssoc();");
        fw.pushLine("case 'assoclist':");
        fw.pushLine("return $db->loadAssocList();");
        fw.pushLine("case 'result':");
        fw.pushLine("return $db->loadResult();");
        fw.pushLine("default: return true;");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("public static function query_preset($query, $result = null) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("$db->setQuery($query);");
        fw.pushLine("$db->query();");
        fw.pushLine("if ($db->getErrorNum() > 0) {");
        fw.pushLine("return $db->getErrorMsg();");
        fw.pushLine("}");
        fw.pushLine("switch ($result) {");
        fw.pushLine("case 'assoc':");
        fw.pushLine("return $db->loadAssoc();");
        fw.pushLine("case 'assoclist':");
        fw.pushLine("return $db->loadAssocList();");
        fw.pushLine("case 'result':");
        fw.pushLine("return $db->loadResult();");
        fw.pushLine("default: return true;");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("}");
    }
}
