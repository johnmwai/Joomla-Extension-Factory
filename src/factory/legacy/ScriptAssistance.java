/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.legacy;

import com.fuscard.commons.FileWriter;
import factory.packager.VersionWrapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 3, 2012 -- 1:33:19 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class ScriptAssistance {

    HashMap<String, LinkedList<Field>> toRemove = new LinkedHashMap<String, LinkedList<Field>>(); //columns to remove against the version
    HashMap<String, LinkedList<Field>> toAdd = new LinkedHashMap<String, LinkedList<Field>>(); //columns to add against the version
    private final Component outer;

    ScriptAssistance(final Component outer) {
        this.outer = outer;
    }

    HashMap<String, LinkedList<Field>> getToAdd() {
        return toAdd;
    }

    HashMap<String, LinkedList<Field>> getToRemove() {
        return toRemove;
    }

    void addFieldToAdd(Field f) {
        if (toAdd.get(f.getVersion()) == null) {
            toAdd.put(f.getVersion(), new LinkedList<Field>());
        }
        toAdd.get(f.getVersion()).add(f);
    }

    public static LinkedList<String> sortVersions(Set<String> toSort) {
        LinkedList<String> res = new LinkedList<String>();
        LinkedList<String> set = new LinkedList<String>(toSort);
        for (int i = 0; i < set.size();) {
            String curr = set.get(i);
            for (int j = 0; j < set.size(); j++) {
                String other = set.get(j);
                if (VersionWrapper.comp(curr, other) == VersionWrapper.LESS_THAN) {
                    curr = other;
                }
            }
            set.remove(curr);
            res.add(curr);
        }
        return res;
    }

    public static LinkedList<String> sortStrings(Set<String> toSort, boolean greater) {
        LinkedList<String> res = new LinkedList<String>();
        LinkedList<String> set = new LinkedList<String>(toSort);
        for (int i = 0; i < set.size();) {
            String curr = set.get(i);
            for (int j = 0; j < set.size(); j++) {
                String other = set.get(j);
                try {
                    if (greater) {
                        if ("".equals(curr) || Integer.parseInt(curr)
                                < Integer.parseInt(other)) {
                            curr = other;
                        }
                    } else {
                        if (Integer.parseInt(curr)
                                > Integer.parseInt(other)) {
                            curr = other;
                        }
                    }

                } catch (Exception e) {
                    if (greater) {
                        if (curr.compareTo(other) < 0) {
                            curr = other;
                        }
                    } else {
                        if (curr.compareTo(other) > 0) {
                            curr = other;
                        }
                    }
                }
            }
            set.remove(curr);
            res.add(curr);
        }
        return res;
    }
    
    public static boolean stringsEqual(String... strings) {
        if (strings.length < 2) {
            return false;
        }
        String s1 = strings[0];
        for (int i = 1; i < strings.length; i++) {
            String s2 = strings[i];
            if (s1 == null ? s2 != null : !s1.equals(s2)) {
                return false;
            }
        }
        return true;
    }
    

    void addFieldToRemove(Field f) {
        if (toRemove.get(f.getTerminationVersion()) == null) {
            toRemove.put(f.getTerminationVersion(), new LinkedList<Field>());
        }
        toRemove.get(f.getTerminationVersion()).add(f);
    }

    void writeScript(FileWriter fw) {
        fw.pushLine("<?php");
        fw.pushLine("/**");
        outer.writeHeader(fw);
        fw.pushLine("*/");
        fw.pushLine("defined('_JEXEC') or die('Restricted access');");
        fw.pushLine("class " + outer.getInstallationName() + "InstallerScript {");
        fw.pushLine("function preflight($type, $parent) {");
        fw.pushLine("$jversion = new JVersion();");
        fw.pushLine("$this->release = $parent->get(\"manifest\")->version;");
        fw.pushLine(
                "$this->minimum_joomla_release = $parent->get(\"manifest\")->attributes()->version;");
        fw.pushLine(
                "echo '<p>Installing component manifest file version = ' . $this->release;");
        fw.pushLine(
                "echo '<br />Current manifest cache commponent version = ' . $this->getParam('version');");
        fw.pushLine(
                "echo '<br />Installing component manifest file minimum Joomla version = ' . $this->minimum_joomla_release;");
        fw.pushLine(
                "echo '<br />Current Joomla version = ' . $jversion->getShortVersion();");
        fw.pushLine("$rel = $this->release;");
        fw.pushLine("if ($type == 'update') {");
        fw.pushLine("} else {");
        fw.pushLine("}");
        fw.pushLine(
                "echo '<p>' . JText::_('BraviaCart is in preflight to ' . $type . ' ' . $rel) . '</p>';");
        fw.pushLine("}");
        fw.pushLine("function install($parent) {");
        fw.pushLine(
                "echo '<p>' . JText::_('" + outer.getInstallationName() + " preinstallation to ' . $this->release) . '</p>';");
        fw.pushLine("}");
        fw.pushLine("function update($parent) {");
        fw.pushLine(
                "echo '<p>' . JText::_('" + outer.getInstallationName() + " preupdate to ' . $this->release) . '</p>';");
        if (!toRemove.isEmpty()) {
            for (String key : sortVersions(toRemove.keySet())) {
                fw.pushLine(
                        "if (version_compare($this->release, \"" + key + "\", 'ge')) {");
                fw.pushLine(
                        "echo \"<p>Changing database schema for versions over " + key + " ...</p> Removing database columns\";");
                LinkedList<Field> fields2 = toRemove.get(key);
                for (Field f : fields2) {
                    fw.pushLine(
                            "$this->removeCol(\"" + f.getTable().getInstallationName() + "\", \"" + f.getName() + "\");");
                }
                fw.pushLine("}");
            }
        }
        if (!toAdd.isEmpty()) {
            for (String key : sortVersions(toAdd.keySet())) {
                fw.pushLine(
                        "if (version_compare($this->release, \"" + key + "\", 'ge')) {");
                fw.pushLine(
                        "echo \"<p>Changing database schema for versions over " + key + " ...</p> Adding database columns.\";");
                LinkedList<Field> fields = toAdd.get(key);
                for (Field f : fields) {
                    fw.pushLine(
                            "$this->addCol(\"" + f.getTable().getInstallationName() + "\", \"" + f.getName() + "\", \"" + f.getSqlDefinition() + "\");");
                }
                fw.pushLine("}");
            }
        }
        //            fw.pushLine("$parent->getParent()->setRedirectURL('index.php?option=" + getInstallationName() + "');");
        fw.pushLine("}");
        fw.pushLine("private function removeCol($table, $col) {");
        fw.pushLine("$db = Jfactory::getDBO();");
        fw.pushLine(
                "$res = $this->execute(\"select * from information_schema.columns");
        fw.pushLine(
                "where table_name = {$db->quote($db->replacePrefix($table))}");
        fw.pushLine("and column_name = \" . $db->quote($col), \"assoc\");");
        fw.pushLine("if (!empty($res)) {");
        fw.pushLine(
                "$this->execute(\"alter table \" . $db->nameQuote($table) . \" drop column \" . $db->nameQuote($col));");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("private function addCol($table, $col, $coldef) {");
        fw.pushLine("$db = Jfactory::getDBO();");
        fw.pushLine(
                "$res = $this->execute(\"select * from information_schema.columns");
        fw.pushLine(
                "where table_name = {$db->quote($db->replacePrefix($table))}");
        fw.pushLine("and column_name = \" . $db->quote($col), \"assoc\");");
        fw.pushLine("if (empty($res)) {");
        fw.pushLine(
                "$this->execute(\"Alter TABLE \" . $db->nameQuote($table) . \" add $coldef\");");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("function postflight($type, $parent) {");
        fw.pushLine(
                "$params['my_param0'] = 'Component version ' . $this->release;");
        fw.pushLine("$params['my_param1'] = 'Another value';");
        fw.pushLine("if ($type == 'install') {");
        fw.pushLine("$params['my_param2'] = '4';");
        fw.pushLine("$params['my_param3'] = 'Star';");
        fw.pushLine("}");
        fw.pushLine("$this->setParams($params);");
        fw.pushLine(
                "echo '<p>' . JText::_('" + outer.getInstallationName() + " is in postflight in ' . $type . ' to ' . $this->release) . '</p>';");
        fw.pushLine("}");
        fw.pushLine("function uninstall($parent) {");
        fw.pushLine("}");
        fw.pushLine("function getParam($name) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine(
                "$db->setQuery('SELECT manifest_cache FROM #__extensions WHERE name = \"" + outer.getInstallationName() + "\"');");
        fw.pushLine("$manifest = json_decode($db->loadResult(), true);");
        fw.pushLine("return $manifest[$name];");
        fw.pushLine("}");
        fw.pushLine("function setParams($param_array) {");
        fw.pushLine("if (count($param_array) > 0) {");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine(
                "$db->setQuery('SELECT params FROM #__extensions WHERE name = \"" + outer.getInstallationName() + "\"');");
        fw.pushLine("$params = json_decode($db->loadResult(), true);");
        fw.pushLine("foreach ($param_array as $name => $value) {");
        fw.pushLine("$params[(string) $name] = (string) $value;");
        fw.pushLine("}");
        fw.pushLine("$paramsString = json_encode($params);");
        fw.pushLine("$db->setQuery('UPDATE #__extensions SET params = ' .");
        fw.pushLine("$db->quote($paramsString) .");
        fw.pushLine("' WHERE name = \"" + outer.getInstallationName() + "\"');");
        fw.pushLine("$db->query();");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("private function bulkExecutor($sql) {");
        fw.pushLine("$queries = explode(\";\", $sql);");
        fw.pushLine("foreach ($queries as $q) {");
        fw.pushLine("$this->execute($q);");
        fw.pushLine("}");
        fw.pushLine("}");
        fw.pushLine("public function execute($query, $result = null) {");
        fw.pushLine("if (empty($query)) {");
        fw.pushLine("return;");
        fw.pushLine("}");
        fw.pushLine("echo \"<h3>excuting query:</h3><p> $query</p><p/>\";");
        fw.pushLine("$db = JFactory::getDbo();");
        fw.pushLine("$db->setQuery($query);");
        fw.pushLine("$db->query();");
        fw.pushLine("if ($db->getErrorNum() > 0) {");
        fw.pushLine(
                "echo '<span style=\"color: red; font-weight:bold;\">',$db->getErrorMsg(),'</span>';");
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
