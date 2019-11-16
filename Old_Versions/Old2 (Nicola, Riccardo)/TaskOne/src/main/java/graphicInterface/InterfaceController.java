package graphicInterface;


import DataManagement.DataManager;

import java.io.PrintWriter;

abstract class InterfaceController {

    public PrintWriter LOG = new PrintWriter( System.out );
    protected DataManager dataManager = new DataManager();

    abstract void searchValue();
    abstract void undoSearch();
    abstract void showInsertPopup();
    abstract void insertNewElement();
    abstract void closePopups();
    abstract void changeTable( String SECTION );
    abstract void reset();

}
