package factory.legacy;

import com.fuscard.commons.FileWriter;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:40:21 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Controller extends MVCClass {

    public Controller(String name, MVCClassType type, Component parent, Config config) {
        super(name, type, parent, config);
        String _parentFile = "";
        String _superClass = "";
        switch (configuration.type) {
            case Plain:
                _parentFile = "controller";
                _superClass = "JController";
                break;
            case Admin:
                _parentFile = "controlleradmin";
                _superClass = "JControllerAdmin";
                break;
            case Form:
            case IDE_Details:
                _parentFile = "controllerform";
                _superClass = "JControllerForm";
                break;
            case List:
            case IDE_List:
                if (isSite()) {
                    _parentFile = "controller";
                    _superClass = "JController";
                } else {
                    _parentFile = "controlleradmin";
                    _superClass = "JControllerAdmin";
                }

                break;
            default:
                throw new IllegalStateException("Unsupported controller type");
        }
        parentFile = _parentFile;
        superClass = _superClass;
        String componentName = FileWriter.capitalize(parent.getName());
        className = componentName + "Controller" + FileWriter.capitalize(name);
        folder = "controllers";
    }

    @Override
    public void writeBody() {
        if (isDetails() && isSite()) {
            String s = "public function save(){\n"
                    + "$data = JRequest::getVar(\"jform\");\n"
                    + "$table = $this->getModel()->getTable(\"" + getSingular() + "\");\n"
                    + "$table->bind($data);\n"
                    + "$table->store();\n"
                    + "$this->setRedirect(\"index.php?option=" + parent.getInstallationName() + "&view=" + getPlural().toLowerCase() + "\");\n"
                    + "}\n";
            fw.pushLine(s);
            s = "public function delete(){\n"
                    + "$data = JRequest::getVar(\"jform\");\n"
                    + "$table = $this->getModel()->getTable(\"" + getSingular() + "\");\n"
                    + "$table->bind($data);\n"
                    + "$table->store();\n"
                    + "$this->setRedirect(\"index.php?option=" + parent.getInstallationName() + "&view=" + getPlural().toLowerCase() + "\");\n"
                    + "}\n";
            fw.pushLine(s);
        }
    }
}
