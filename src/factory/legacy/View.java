package factory.legacy;

import com.fuscard.commons.FileWriter;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:39:48 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class View extends MVCClass {

    public final LinkedList<Capability> capabilities = new LinkedList<Capability>();
    public final LinkedList<MVCClass> partners = new LinkedList<MVCClass>();
    private Table table = null;
    private String newTitle = "";
    private String editTitle = "";
    private String listTitle = "";
    private String displayTitle = null;
    private boolean controlPanel = false;

    public View(String name, MVCClassType type, Component parent, Config config) {
        super(name, type, parent, config);
        String _parentFile = "";
        String _superClass = "";
        switch (configuration.type) {
            case Plain:
            case Form:
            case List:
            case Admin:
            case IDE_Details:
            case IDE_List:
                _parentFile = "view";
                _superClass = "JView";
                break;
            default:
                throw new IllegalStateException("Unsupported view type");
        }
        parentFile = _parentFile;
        superClass = _superClass;
        String componentName = FileWriter.capitalize(parent.getName());
        className = componentName + "View" + FileWriter.capitalize(name);
        folder = "views";
    }

    public void addCapability(Capability c) {
        capabilities.add(c);
    }

    @Override
    protected void finalizePreparations() {
        switch (type) {
            case ADMIN:
                root = "admin";
                break;
            case SITE:
                root = "site";
                break;
            default:
                throw new IllegalStateException();
        }
        file = root + "." + folder + "." + name + "|view.html.php";
        String index = root + "." + folder + "." + name + "|index.html";
        file = parent.makeFile(file.toLowerCase());
        parent.makeFile(index);
        fw = parent.obtainWriter(file);
        makeLayout();
    }

    private void makeLayout() {
        if (isDetails()) {
            makeEditLayout(root + "." + folder + "." + name + ".tmpl|edit.php");
        } else if (isList()) {
            makeDefaultLayout(
                    root + "." + folder + "." + name + ".tmpl|default.php");
            makeDefaultHeadLayout(
                    root + "." + folder + "." + name + ".tmpl|default_head.php");
            makeDefaultBodyLayout(
                    root + "." + folder + "." + name + ".tmpl|default_body.php");
            makeDefaultFootLayout(
                    root + "." + folder + "." + name + ".tmpl|default_foot.php");
        } else if (parent.viewIndex == this) {
            makeAdminViewIndexLayout(
                    root + "." + folder + "." + name + ".tmpl|default.php");
        } else if (parent.controlPanel == this) {
            makeControlPanelLayout(
                    root + "." + folder + "." + name + ".tmpl|default.php",
                    "Extended Control Panel");
        } else if (parent.getRootPanel() == this || isControlPanel()) {
            makeSimpleControlPanelLayout(
                    root + "." + folder + "." + name + ".tmpl|default.php");
        } else {
            makePlainLayout(
                    root + "." + folder + "." + name + ".tmpl|default.php");
        }
        String index = root + "." + folder + "." + name + ".tmpl|index.html";
        parent.makeFile(index);
        if (type == MVCClassType.SITE) {
            makeTemplateXml();
        }
    }

    private void makeEditLayout(String path) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        if (isSite()) {
//            fwr.pushLine("</form>");
            String s = "<?php\n"
                    + "/**\n";
            fwr.pushLine(s);
            parent.writeHeader(fwr);
            s = " */\n"
                    + "defined('_JEXEC') or die('Restricted access');\n"
                    + "JHtml::_('behavior.tooltip');\n"
                    + "JHtml::_('behavior.formvalidation');\n"
                    + "$params = $this->form->getFieldsets('params');\n"
                    + "?>\n"
                    + "<style type=\"text/css\">\n"
                    + "    <!--\n"
                    + "\n"
                    + "    table tr td{\n"
                    + "        padding: 5px;\n"
                    + "    }\n"
                    + "    table tr{\n"
                    + "        text-align: left;\n"
                    + "        background-color: #F5F5F5;\n"
                    + "    }\n"
                    + "    table tr:hover {\n"
                    + "        color: #999999;\n"
                    + "        background-color: #FFFFFF;	\n"
                    + "    }\n"
                    + "    -->\n"
                    + "</style>\n"
                    + "\n"
                    + "\n"
                    + "<h3><?php echo JText::_( '" + getDisplayNameJText() + "' ).' :: " + editTitle + "'; ?></h3>\n"
                    + "<form action=\"<?php echo JRoute::_('index.php?option=" + parent.getInstallationName() + "&layout=edit&" + table.getPrimaryKey().getName() + "=' . (int) $this->item->" + table.getPrimaryKey().getName() + "); ?>\" method=\"post\" name=\"adminForm\" id=\"" + getSingular().toLowerCase() + "-form\" class=\"form-validate\">\n"
                    + "    <div class=\"width-60 fltlft\">\n"
                    + "        <fieldset class=\"adminform\">\n"
                    + "\n"
                    + "            <table width=\"100%\" border=\"1\" bordercolor=\"#CCCCCC\">\n"
                    + "                <?php\n"
                    + "                foreach ($this->form->getFieldset('details') as $field):\n"
                    + "                    if (!$field->hidden):\n"
                    + "                        ?>\n"
                    + "                        <tr><td><?php echo $field->label; ?></td><td>\n"
                    + "                                <?php echo $field->input; ?></td></tr>\n"
                    + "                    <?php\n"
                    + "                    else:\n"
                    + "                        echo $field->input;\n"
                    + "                    endif;\n"
                    + "                endforeach;\n"
                    + "                ?>\n"
                    + "            </table>\n"
                    + "        </fieldset>\n"
                    + "    </div>\n"
                    + "    <div class=\"width-40 fltrt\">\n"
                    + "\n"
                    + "    </div>\n"
                    + "    <div>\n"
                    + "        <input type=\"hidden\" name=\"task\" value=\"" + getSingular().toLowerCase() + ".save\" />\n"
                    + "<?php echo JHtml::_('form.token'); ?></\n"
                    + "    </div><input type=\"submit\" value=\"submit\"/>"
                    + "<input type=\"reset\" value=\"Reset\"/>"
                    + "<input type=\"button\" value=\"Cancel\" onclick=\"document.location='index.php?option=" + parent.getInstallationName() + "&view=" + getPlural().toLowerCase() + "'\"/>\n"
                    + "</form>\n"
                    + "";
            fwr.pushLine(s);
        } else {
            fwr.pushLine("<?php");
            fwr.pushLine("/**");
            parent.writeHeader(fwr);
            fwr.pushLine("*/");
            fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
            fwr.pushLine("JHtml::_('behavior.tooltip');");
            fwr.pushLine("JHtml::_('behavior.formvalidation');");
            fwr.pushLine("$params = $this->form->getFieldsets('params');");
            fwr.pushLine("?>");
            fwr.pushLine(
                    "<form action=\"<?php echo JRoute::_('index.php?option=" + parent.getInstallationName() + "&layout=edit&id='.(int) $this->item->" + table.getPrimaryKey().getName() + "); ?>\" method=\"post\" name=\"adminForm\" id=\"" + getSingular().toLowerCase() + "-form\" class=\"form-validate\">");
            fwr.pushLine("<div class=\"width-60 fltlft\">");
            fwr.pushLine("<fieldset class=\"adminform\">");
            fwr.pushLine(
                    "<legend><?php echo JText::_( '" + getDisplayNameJText() + "' ).' :: " + editTitle + "'; ?></legend>");
            fwr.pushLine("<ul class=\"adminformlist\">");
            fwr.pushLine(
                    "<?php foreach($this->form->getFieldset('details') as $field): ?>");
            fwr.pushLine(
                    "<li><?php echo $field->label;echo $field->input;?></li>");
            fwr.pushLine("<?php endforeach; ?>");
            fwr.pushLine("</ul>");
            fwr.pushLine("</div>");
            fwr.pushLine("<div class=\"width-40 fltrt\">");
            fwr.pushLine(
                    "<?php echo JHtml::_('sliders.start', '" + getSingular().toLowerCase() + "-slider'); ?>");
            fwr.pushLine("<?php foreach ($params as $name => $fieldset): ?>");
            fwr.pushLine(
                    "<?php echo JHtml::_('sliders.panel', JText::_($fieldset->label), $name.'-params');?>");
            fwr.pushLine(
                    "<?php if (isset($fieldset->description) && trim($fieldset->description)): ?>");
            fwr.pushLine(
                    "<p class=\"tip\"><?php echo $this->escape(JText::_($fieldset->description));?></p>");
            fwr.pushLine("<?php endif;?>");
            fwr.pushLine("<fieldset class=\"panelform\" >");
            fwr.pushLine("<ul class=\"adminformlist\">");
            fwr.pushLine(
                    "<?php foreach ($this->form->getFieldset($name) as $field) : ?>");
            fwr.pushLine(
                    "<li><?php echo $field->label; ?><?php echo $field->input; ?></li>");
            fwr.pushLine("<?php endforeach; ?>");
            fwr.pushLine("</ul>");
            fwr.pushLine("</fieldset>");
            fwr.pushLine("<?php endforeach; ?>");
            fwr.pushLine("<?php echo JHtml::_('sliders.end'); ?>");
            fwr.pushLine("</div>");
            fwr.pushLine("<div>");
            fwr.pushLine(
                    "<input type=\"hidden\" name=\"task\" value=\"" + getSingular().toLowerCase() + ".submitdetails\" />");
            fwr.pushLine("<?php echo JHtml::_('form.token'); ?>");
            fwr.pushLine("</div>");
            fwr.pushLine("</form>");
        }

    }

    private void makePlainLayout(String path) {
        if (isDetails()) {
            return;
        }
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("JHtml::_('behavior.tooltip');");
        fwr.pushLine("?>");
        fwr.pushLine("This is the " + name + " layout");
    }

    private void makeAdminViewIndexLayout(String path) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("JHtml::_('behavior.tooltip');");
        fwr.pushLine("?>");
        fwr.pushLine(
                "<h3><?php echo JText::_( '" + getDisplayNameJText() + "' ).' :: Index of views'; ?></h3><p/>");
        for (View v : sortViews(parent.getViews())) {
            if (v.type == MVCClassType.ADMIN) {
                String url;
                if (!v.isDetails()) {
                    url = "index.php?option=" + parent.getInstallationName() + "&view=" + v.getName();
                } else {
                    url = "index.php?option=" + parent.getInstallationName() + "&view=" + v.getName() + "&layout=edit";
                }
                String namee = v.getName();
                if (v.isDetails()) {
                    namee = v.getNewTitle();
                } else if (v.isList()) {
                    namee = v.getListTitle();
                }
                fwr.pushLine(
                        "<span style=\"font-weight: bold;\"><a href=\"" + url.toLowerCase() + "\">" + v.getName() + "</a></span>--" + namee + "<br/>");
            }
        }
    }

    private LinkedList<View> sortViews(View[] views) {
        LinkedList<View> src = new LinkedList<View>(Arrays.asList(views));
        return sortViews(src);
    }

    private LinkedList<View> sortViews(LinkedList<View> src) {
        LinkedList<View> res = new LinkedList<View>();
        for (int i = 0; i < src.size();) {
            View curr = src.get(i);
            for (int j = 0; j < src.size(); j++) {
                View other = src.get(j);
                if (other.getName().compareToIgnoreCase(curr.getName()) < 0) {
                    curr = other;
                }
            }
            src.remove(curr);
            res.add(curr);
        }
        return res;
    }

    private void makeDefaultLayout(String path) {
        if (isDetails()) {
            return;
        }
        String tfile = parent.makeFile(path);
        String s;
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("JHtml::_('behavior.tooltip');");
        String filter = "$mainframe = JFactory::getApplication();\n"
                + "$namespace_prefix = " + parent.getHelperName() + "::getMyNameSpacePrefix();\n"
                + "$filter = $mainframe->getUserState(\"$namespace_prefix." + getName().toLowerCase() + ".filter\");\n"
                + "if ($filter == null) {\n"
                + "$filter = array();\n"
                + "}";
        fwr.pushLine(filter);
        fwr.pushLine("?>");
        fwr.pushLine("<style type=\"text/css\">");
        fwr.pushLine("<!--");
        s = ".bravia_cart_list_container {\n"
                + "overflow: auto;\n"
                + "}\n"
                + "::-webkit-scrollbar {\n"
                + "width: 5px;\n"
                + "height: 10px;\n"
                + "}\n"
                + "::-webkit-scrollbar-track {\n"
                + "-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);\n"
                + "border-radius: 4px;\n"
                + "}\n"
                + "::-webkit-scrollbar-thumb {\n"
                + "border-radius: 4px;\n"
                + "-webkit-box-shadow: inset 0 0 6px rgba(0,50,200,0.9);\n"
                + "}";
        fwr.pushLine(s);
        if (isSite()) {
            s = "table tr th {\n"
                    + "background-color: #CCCCCC;\n"
                    + "text-align: center;\n"
                    + "padding: 5px;\n"
                    + "}\n"
                    + "table tr th:hover {\n"
                    + "color: #999999;\n"
                    + "background-color: #FFFFFF;\n"
                    + "}";
            fwr.pushLine(s);

        }
        fwr.pushLine("-->");
        fwr.pushLine("</style>");
        if (isSite()) {
            fwr.pushLine(
                    "<h3><?php echo JText::_( '" + getDisplayNameJText() + "' ).' :: " + listTitle + "'; ?></h3>");
        }
        if (isSite()) {
            fwr.pushLine(
                    "<p><input type=\"button\" value=\"Edit Selected\" onclick=\"MyEdit.edit()\"/>"
                    + "<input type=\"button\" value=\"add new\" onclick=\"MyEdit.add()\"/>"
                    + "<input type=\"button\" value=\"Back to Client Area\" onclick=\"MyEdit.clientArea()\"/></p>");
        }
        fwr.pushLine(
                "<div class=\"bravia_cart_list_container\"><form action=\"<?php echo JRoute::_('index.php?option=" + parent.getInstallationName() + "&view=" + getPlural().toLowerCase() + "'); ?>\" method=\"post\" name=\"adminForm\">");

        s = "width=\"100%\" border=\"1\" bordercolor=\"#CCCCCC\"";
        fwr.pushLine("<table class=\"adminlist\" " + (isSite() ? s : "") + ">");
        fwr.pushLine("<thead><?php echo $this->loadTemplate('head');?></thead>");
        fwr.pushLine("<tfoot><?php echo $this->loadTemplate('foot');?></tfoot>");
        fwr.pushLine("<tbody><?php echo $this->loadTemplate('body');?></tbody>");
        fwr.pushLine("</table>");
        fwr.pushLine("<div>");
        fwr.pushLine("<input type=\"hidden\" name=\"task\" value=\"\" />");
        fwr.pushLine("<input type=\"hidden\" name=\"boxchecked\" value=\"0\" />");
        fwr.pushLine("<?php echo JHtml::_('form.token'); ?>");
        fwr.pushLine("</div>");
        fwr.pushLine("</form></div><?php echo $this->pagination->getListFooter(); ?>");
        if (isSite()) {
            s = "<script type=\"text/javascript\">\n"
                    + "var MyEdit = {};\n"
                    + "MyEdit.edit = function(){\n"
                    + "var sel = document.adminForm.boxchecked.get(\"value\");\n"
                    + "if(sel == \"0\"){\n"
                    + "alert(\"Please make a selection\");\n"
                    + "return;\n"
                    + "}\n"
                    + "var j = 0;\n"
                    + "var s = 0;\n"
                    + "var c = document.getElementsByName(\"cid[]\");\n"
                    + "for(var i = 0 ; i < c.length; i ++ ){\n"
                    + "var cc = c[i];\n"
                    + "if(cc.checked){\n"
                    + "j ++ ;\n"
                    + "s = cc.get(\"value\");\n"
                    + "}\n"
                    + "}\n"
                    + "if(j != 1){\n"
                    + "alert(\"Please select just one row\");\n"
                    + "return;\n"
                    + "}\n"
                    + "document.location = 'index.php?option=" + parent.getInstallationName() + "&view=" + getSingular() + "&layout=edit&"+getTable().getPrimaryKey().name+"=' + s;\n"
                    + "};\n"
                    + "MyEdit.add = function(){\n"
                    + "document.location = 'index.php?option=" + parent.getInstallationName() + "&view=" + getSingular() + "&layout=edit&"+getTable().getPrimaryKey().name+"=' + 0;\n"
                    + "};\n"
                    + "MyEdit.clientArea = function(){\n"
                    + "document.location = 'index.php?option=" + parent.getInstallationName() + "';\n"
                    + "};\n"
                    + "</script>";
            fwr.pushLine(s);
        }
    }

    private void makeTemplateXml() {
        String snme = isDetails() ? "edit" : "default";
        String props = root + "." + folder + "." + name + ".tmpl|" + snme + ".xml";
        FileWriter pfw = parent.obtainWriter(parent.makeFile(props));
        pfw.pushLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        pfw.pushLine("<metadata>");
        pfw.pushLine(
                "<layout title=\"" + FileWriter.capitalize(name) + " Layout\">");
        pfw.pushLine("<message>");
        pfw.pushLine("<![CDATA[" + FileWriter.capitalize(name) + " Layout]]>");
        pfw.pushLine("</message>");
        pfw.pushLine("</layout>");
        pfw.pushLine("<state>");
        pfw.pushLine("<name>" + FileWriter.capitalize(name) + " Layout</name>");
        pfw.pushLine(
                "<description>" + FileWriter.capitalize(name) + " Layout</description>");
        pfw.pushLine("<params>");
        pfw.pushLine("</params>");
        pfw.pushLine("</state>");
        pfw.pushLine("</metadata>");
    }

    @Override
    public void writeBody() {
        if (isDetails()) {
            fw.pushLine("public function display($tpl = null) {");
            fw.pushLine("$form = $this->get('Form');");
            fw.pushLine("$item = $this->get('item');");
            fw.pushLine("$script = $this->get('Script');");
            fw.pushLine("if (count($errors = $this->get('Errors'))) {");
            fw.pushLine("JError::raiseError(500, implode('<br />', $errors));");
            fw.pushLine("return false;");
            fw.pushLine("}");
            fw.pushLine("$this->form = $form;");
            fw.pushLine("$this->item = $item;");
            fw.pushLine("$this->script = $script;");
            if (isAdmin()) {
                fw.pushLine("$this->addToolBar();");
            }
            fw.pushLine("parent::display($tpl);");
            fw.pushLine("$this->setDocument();");
            fw.pushLine("}");
            if (isAdmin()) {
                fw.pushLine("protected function addToolBar() {");
                fw.pushLine("JRequest::setVar('hidemainmenu', true);");
                fw.pushLine("$user = JFactory::getUser();");
                fw.pushLine("$userId = $user->id;");
                fw.pushLine(
                        "$isNew = $this->item->" + table.getPrimaryKey().getName() + " == 0;");
                fw.pushLine(
                        "$canDo = " + parent.getHelperName() + "::getActions('" + getSingular().toLowerCase() + "', $this->item->" + table.getPrimaryKey().getName() + ");");

                fw.pushLine(
                        "JToolBarHelper::title($isNew ? JText::_('" + getAdminTitle() + "').' :: " + newTitle + "' : JText::_('" + getAdminTitle() + "').' :: " + editTitle + "', '" + parent.getName().toLowerCase() + "');");

                fw.pushLine("if ($isNew) {");
                fw.pushLine("if ($canDo->get('core.create')) {");

                fw.pushLine(
                        "JToolBarHelper::apply('" + getSingular().toLowerCase() + ".apply', 'JTOOLBAR_APPLY');");
                fw.pushLine(
                        "JToolBarHelper::save('" + getSingular().toLowerCase() + ".save', 'JTOOLBAR_SAVE');");
                fw.pushLine(
                        "JToolBarHelper::custom('" + getSingular().toLowerCase() + ".save2new', 'save-new.png', 'save-new_f2.png', 'JTOOLBAR_SAVE_AND_NEW', false);");

                fw.pushLine("}");

                fw.pushLine(
                        "JToolBarHelper::cancel('" + getSingular().toLowerCase() + ".cancel', 'JTOOLBAR_CANCEL');");

                fw.pushLine("} else {");
                fw.pushLine("if ($canDo->get('core.edit')) {");

                fw.pushLine(
                        "JToolBarHelper::apply('" + getSingular().toLowerCase() + ".apply', 'JTOOLBAR_APPLY');");
                fw.pushLine(
                        "JToolBarHelper::save('" + getSingular().toLowerCase() + ".save', 'JTOOLBAR_SAVE');");

                fw.pushLine("if ($canDo->get('core.create')) {");

                fw.pushLine(
                        "JToolBarHelper::custom('" + getSingular().toLowerCase() + ".save2new', 'save-new.png', 'save-new_f2.png', 'JTOOLBAR_SAVE_AND_NEW', false);");

                fw.pushLine("}");
                fw.pushLine("}");
                fw.pushLine("if ($canDo->get('core.create')) {");

                fw.pushLine(
                        "JToolBarHelper::custom('" + getSingular().toLowerCase() + ".save2copy', 'save-copy.png', 'save-copy_f2.png', 'JTOOLBAR_SAVE_AS_COPY', false);");

                fw.pushLine("}");

                fw.pushLine(
                        "JToolBarHelper::cancel('" + getSingular().toLowerCase() + ".cancel', 'JTOOLBAR_CLOSE');");

                fw.pushLine("}");
                fw.pushLine("}");
            }
            fw.pushLine("protected function setDocument() {");
            fw.pushLine(
                    "$isNew = ($this->item->" + table.getPrimaryKey().getName() + " < 1);");
            fw.pushLine("$document = JFactory::getDocument();");
            fw.pushLine(
                    "$document->setTitle($isNew ? JText::_('" + getAdminTitle() + "').' :: " + newTitle + "' : JText::_('" + getAdminTitle() + "').' :: " + editTitle + "');");
            fw.pushLine("$document->addScript(JURI::root() . $this->script);");
            fw.pushLine(
                    "$document->addScript(JURI::root() . \"/administrator/components/" + parent.getInstallationName() + "/views/" + getSingular().toLowerCase() + "/submitbutton.js\");");
            fw.pushLine("JText::script('Some values are unacceptable');");
            fw.pushLine("}");
        } else if (isList()) {
            makeScripts();
            fw.pushLine("function display($tpl = null) {");
            fw.pushLine("$items = $this->get('Items');");
            fw.pushLine("$pagination = $this->get('Pagination');");
            fw.pushLine("if (count($errors = $this->get('Errors'))) {");
            fw.pushLine("JError::raiseError(500, implode('<br />', $errors));");
            fw.pushLine("return false;");
            fw.pushLine("}");
            fw.pushLine("$this->items = $items;");
            fw.pushLine("$this->pagination = $pagination;");
            fw.pushLine("$table_headers = $this->get('TableHeaders');");
            fw.pushLine("$this->table_headers = $table_headers;");
            if (isAdmin()) {
                fw.pushLine("$this->addToolBar();");
            }
            fw.pushLine("parent::display($tpl);");
            fw.pushLine("$this->setDocument();");
            fw.pushLine("}");
            if (isAdmin()) {
                fw.pushLine("protected function addToolBar() {");
                fw.pushLine(
                        "$canDo = " + parent.getHelperName() + "::getActions();");
                fw.pushLine(
                        "JToolBarHelper::title(JText::_('" + getAdminTitle() + "').' :: " + listTitle + "', '" + parent.getName().toLowerCase() + "');");
                fw.pushLine("if ($canDo->get('core.create')) {");
                fw.pushLine(
                        "JToolBarHelper::addNew('" + getSingular().toLowerCase() + ".add', 'JTOOLBAR_NEW');");
                fw.pushLine("}");
                fw.pushLine("if ($canDo->get('core.edit')) {");
                fw.pushLine(
                        "JToolBarHelper::editList('" + getSingular().toLowerCase() + ".edit', 'JTOOLBAR_EDIT');");
                fw.pushLine("}");
                fw.pushLine("if ($canDo->get('core.delete')) {");
                fw.pushLine(
                        "JToolBarHelper::deleteList('', '" + getPlural().toLowerCase() + ".delete', 'JTOOLBAR_DELETE');");
                fw.pushLine("}");
                fw.pushLine("if ($canDo->get('core.admin')) {");
                fw.pushLine("JToolBarHelper::divider();");
                fw.pushLine(
                        "JToolBarHelper::preferences('" + parent.getInstallationName() + "');");

                fw.pushLine("}");
                fw.pushLine("}");
            }
            fw.pushLine("protected function setDocument() {");
            fw.pushLine("$document = JFactory::getDocument();");
            fw.pushLine(
                    "$document->setTitle(JText::_('" + getAdminTitle() + "').' :: " + listTitle + "');");
            fw.pushLine("}");
        } else if (type == MVCClassType.ADMIN) {
            fw.pushLine("function display($tpl = null) {");
            String title = displayTitle == null ? "Admin" : displayTitle;
            if (isAdmin()) {
                fw.pushLine(
                        "JToolBarHelper::title(JText::_('" + getAdminTitle() + "').' :: " + title + "', '" + parent.getName().toLowerCase() + "');");
            }
            fw.pushLine("$document = JFactory::getDocument();");
            fw.pushLine(
                    "$document->setTitle(JText::_('" + getAdminTitle() + "').' :: " + title + "');");
            if (isAdmin()) {
                fw.pushLine(
                        "JToolBarHelper::preferences('" + parent.getInstallationName() + "');");
            }
            fw.pushLine("parent::display($tpl);");
            fw.pushLine("}");
        } else {
            fw.pushLine("function display($tpl = null) {");
            fw.pushLine("parent::display($tpl);");
            fw.pushLine("}");
        }
    }

    private String getAdminTitle() {
        return parent.getInstallationName().toUpperCase() + "_ADMIN_TITLE";
    }

    private String getDisplayNameJText() {
        return parent.getInstallationName().toUpperCase() + "_DISPLAY_NAME";
    }

    public void setTable(Table table, boolean injectToAll) {
        this.table = table;
        if (injectToAll) {
            if (isDetails()) {
                _setTable(getPlural());
            }
            if (isList()) {
                _setTable(getSingular());
            }
            Model m = (Model) parent.getMVCClass(getName(), type, "model");
            if (m != null) {
                m.setTable(table);
            }
        }
    }

    private void _setTable(String name) {
        View v;
        v = (View) parent.getMVCClass(name, type, "view");
        if (v != null) {
            v.setTable(table);
        }
    }

    private void makeScripts() {
        parent.makeFile(
                "admin.models.forms|" + getSingular().toLowerCase() + ".js");
        makeSubmitButton();
        makeForm();
    }

    private void makeSubmitButton() {
        String props = "admin.views." + getSingular().toLowerCase() + "|submitbutton.js";
        FileWriter fws = parent.obtainWriter(parent.makeFile(props));
        fws.pushLine("");
        fws.pushLine("Joomla.submitbutton = function(task)");
        fws.pushLine("{");
        fws.pushLine("if (task == '')");
        fws.pushLine("{");
        fws.pushLine("return false;");
        fws.pushLine("}");
        fws.pushLine("else");
        fws.pushLine("{");
        fws.pushLine("var isValid=true;");
        fws.pushLine("var action = task.split('.');");
        fws.pushLine("if (action[1] != 'cancel' && action[1] != 'close')");
        fws.pushLine("{");
        fws.pushLine("var forms = $$('form.form-validate');");
        fws.pushLine("for (var i=0;i<forms.length;i++)");
        fws.pushLine("{");
        fws.pushLine("if (!document.formvalidator.isValid(forms[i]))");
        fws.pushLine("{");
        fws.pushLine("isValid = false;");
        fws.pushLine("break;");
        fws.pushLine("}");
        fws.pushLine("}");
        fws.pushLine("}");
        fws.pushLine("if (isValid)");
        fws.pushLine("{");
        fws.pushLine("Joomla.submitform(task);");
        fws.pushLine("return true;");
        fws.pushLine("}");
        fws.pushLine("else");
        fws.pushLine("{");
        fws.pushLine("alert(Joomla.JText._('some values are unacceptable'));");
        fws.pushLine("return false;");
        fws.pushLine("}");
        fws.pushLine("}");
        fws.pushLine("}");
    }

    private void makeForm() {
        String path = root + ".models.forms|" + getSingular().toLowerCase() + ".xml";
        FileWriter fwr = parent.obtainWriter(parent.makeFile(path));
        fwr.pushLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        fwr.pushLine("<form>");
        fwr.pushLine("<fieldset name=\"details\">");
        for (Field f : table.getFields()) {
            if (f.getType() != DataTypes.Params && !(f.getType() == DataTypes.Category && type == MVCClassType.SITE)) {
                f.writeFormField(fwr, type == MVCClassType.ADMIN);
            }
        }
        fwr.pushLine("</fieldset>");
        if (isAdmin()) {

            fwr.pushLine("<fields name=\"params\">");
            fwr.pushLine("<fieldset");
            fwr.pushLine("name=\"params\"");
            fwr.pushLine("label=\"JGLOBAL_FIELDSET_DISPLAY_OPTIONS\"");
            fwr.pushLine(">");
            Field f = table.getRecord().getParams();
            if (f != null) {
                f.writeFormField(fwr, true);
            }
            fwr.pushLine("</fieldset>");
            fwr.pushLine("</fields>");
        }

        fwr.pushLine("</form>");
    }

    public void setTitles(String newTtl, String editTtl, String listTtl, MVCClassType location) {
        _setTitles(newTtl, editTtl, listTtl, getPlural(), location);
        _setTitles(newTtl, editTtl, listTtl, getSingular(), location);
    }

    private void _setTitles(String newTtl, String editTtl, String listTtl, String view, MVCClassType location) {
        View v = (View) parent.getMVCClass(view, location, "view");
        if (v != null) {
            v.newTitle = newTtl;
            v.editTitle = editTtl;
            v.listTitle = listTtl;
        }
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setTable(Table table) {
        setTable(table, false);
    }

    public String getNewTitle() {
        return newTitle;
    }

    public String getEditTitle() {
        return editTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public void setEditTitle(String editTitle) {
        this.editTitle = editTitle;
    }

    public Table getTable() {
        return table;
    }

    private void makeDefaultHeadLayout(String path) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("?>");
        fwr.pushLine("<tr>");
        fwr.pushLine("<th width=\"5\">");
        fwr.pushLine("<?php echo JText::_('id'); ?>");
        fwr.pushLine("</th>");
        fwr.pushLine("<th width=\"20\">");
        fwr.pushLine(
                "<input type=\"checkbox\" name=\"toggle\" value=\"\" onclick=\"checkAll(<?php echo count($this->items); ?>);\" />");
        fwr.pushLine("</th>			");
        fwr.pushLine("<?php echo $this->table_headers; ?>");
        fwr.pushLine("</tr>");
    }

    private void makeDefaultBodyLayout(String path) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("?>");
        fwr.pushLine("<?php foreach ($this->items as $i => $item): ?>");
        fwr.pushLine("<tr class=\"row<?php echo $i % 2; ?>\">");
        fwr.pushLine("<td>");
        fwr.pushLine(
                "<?php echo $item->" + table.getPrimaryKey().getName() + "; ?>");
        fwr.pushLine("</td>");
        fwr.pushLine("<td>");
        fwr.pushLine(
                "<?php echo JHtml::_('grid.id', $i, $item->" + table.getPrimaryKey().getName() + "); ?>");
        fwr.pushLine("</td>");
        fwr.pushLine("<?php");
        fwr.pushLine("foreach ($item as $itm => $val) {");
        fwr.pushLine("if ($itm != '" + table.getPrimaryKey().getName() + "') {");
        fwr.pushLine("echo '<td>';");
        fwr.pushLine("echo $val;");
        fwr.pushLine("echo '</td>';");
        fwr.pushLine("}");
        fwr.pushLine("}");
        fwr.pushLine("?>");
        fwr.pushLine("</tr>");
        fwr.pushLine("<?php endforeach; ?>");
    }

    private void makeDefaultFootLayout(String path) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("?>");
        fwr.pushLine("<tr >");
        fwr.pushLine(
                "<td colspan=\"100%\"></td>");
        fwr.pushLine("</tr>");
    }
    private String myicon = "<style type=\"text/css\">\n"
            + "<!--\n"
            + ".MyIcons {\n"
            + "background-position: left top;\n"
            + "background-repeat: no-repeat;\n"
            + "float: left;\n"
            + "height: 100px;\n"
            + "position: relative;\n"
            + "width: 100px;\n"
            + "text-align: center;\n"
            + "overflow: hidden;\n"
            + "clear: none;\n"
            + "border: 1px solid #757575;\n"
            + "color: #666666;\n"
            + "margin: 1px;\n"
            + "background-color: #FFFFFF;\n"
            + "border-radius: 3px;\n"
            + "padding-top: 20px;\n"
            + "padding-right: 10px;\n"
            + "padding-bottom: 10px;\n"
            + "padding-left: 10px;\n"
            + "}\n"
            + ".MyIcons:hover {\n"
            + "background-color: #dddddd;\n"
            + "}\n"
            + "-->\n"
            + "</style>";

    private void makeControlPanelLayout(String path, String displayName) {
        makeAControlPanel(parent.getViews(), path, displayName);
    }

    private void makeAControlPanel(LinkedList<View> children, String path, String displayName) {
        makeAControlPanel(children.toArray(new View[0]), path, displayName);
    }

    private void makeAControlPanel(View[] children, String path, String displayName) {
        String tfile = parent.makeFile(path);
        FileWriter fwr = parent.obtainWriter(tfile);
        fwr.pushLine("<?php");
        fwr.pushLine("/**");
        parent.writeHeader(fwr);
        fwr.pushLine("*/");
        fwr.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwr.pushLine("JHtml::_('behavior.tooltip');");
        fwr.pushLine("?>");
        fwr.pushLine(myicon);
        fwr.pushLine(
                "<h3><?php echo JText::_( '" + getDisplayNameJText() + "' ).' :: " + displayName + "'; ?></h3><p></p>");
        for (View v : sortViews(children)) {

            if (v.type == type) {
                String url;
                if (!v.isDetails()) {
                    url = "index.php?option=" + parent.getInstallationName() + "&view=" + v.getName();
                } else {
                    url = "index.php?option=" + parent.getInstallationName() + "&view=" + v.getName() + "&layout=edit";
                }
                String namee = v.getName();
                if (v.isDetails()) {
                    namee = v.getNewTitle();
                } else if (v.isList()) {
                    namee = v.getListTitle();
                }
                String image = parent.getImagesFolder() + "\\" + v.getName() + ".gif";
                String _img;
                if (!(new File(image)).isFile()) {
                    _img = "/administrator/components/" + parent.getInstallationName() + "/assets/images/icons/large_icon-feint.png";
                } else {
                    _img = "/administrator/components/" + parent.getInstallationName() + "/assets/images/" + v.getName() + ".gif";
                }
                String code = v.getName();
                if (code.length() > 17) {
                    code = code.substring(0, 16) + "...";
                }
                if (namee.length() > 30) {
                    namee = namee.substring(0, 30) + "...";
                }
                String icon = "<a href=\"" + url.toLowerCase() + "\">\n"
                        + "<div class=\"MyIcons\" style=\"line-height: 1.0em; color: #ffaaaa;\">"
                        + "<img src=\"<?php echo JURI::root() .'" + _img + "' ?>" + "\"/>\n"
                        + "<br/>\n"
                        + "<span style=\"font-weight: bold; size: 10px;\">" + namee
                        + "</span>";
                if (isAdmin()) {
                    icon += "<br/><span style=\"font-style: italic; size: 8px; color: #444444;\">" + code + "</span>\n";
                }
                icon += "</div>\n"
                        + "</a>";
                fwr.pushLine(icon);

            }

        }
    }

    public void setControlPanel(boolean controlPanel) {
        this.controlPanel = controlPanel;
    }

    public boolean isControlPanel() {
        return controlPanel;
    }

    private void makeSimpleControlPanelLayout(String path) {
        makeAControlPanel(children, path, getDisplayTitle());
    }

    public void setDisplayTitle(String title) {
        this.displayTitle = title;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }
    LinkedList<View> children = new LinkedList<View>();

    public void addChild(View view) {
        if (view == null) {
            throw new IllegalArgumentException("the child may not be null");
        }

        for (View v : children) {
            if (v.getName() == null ? view.getName() == null : v.getName().equals(
                    view.getName())) {
                throw new IllegalArgumentException(
                        "Same name: " + v.getName() + " v/s " + view.getName());
            }
        }
        setControlPanel(true);
        children.add(view);
    }
}
