package view.panels;

import javax.swing.*;

import business.BaseManager;
import core.Helper;
import entity.BaseEntity;
import view.BaseLayout;

public abstract class BaseUpdateView<
        E extends BaseEntity
        > extends BaseLayout {

    protected final BaseManager<E> manager;
    protected JButton button_save;
    protected JButton button_cancel;
    protected E currentEntity;

    protected BaseUpdateView(BaseManager<E> manager) {
        this.manager = manager;
    }

    public void setButton_Save(JButton btn_save) {
        this.button_save = btn_save;
    }

    public void setButton_Cancel(JButton button_cancel) {
        this.button_cancel = button_cancel;
    }

    public abstract void initializeUIComponents(E entity);

    protected abstract boolean validateFields();

    protected abstract E setFields(E entity);

    protected void save() {
        if (validateFields()) {
            currentEntity = setFields(currentEntity);
            if (currentEntity.getId() == 0) {
                Helper.showMessage(this.manager.save(currentEntity) ? "Save Successful" : "Save Error: Erroneous entry");
            } else {
                Helper.showMessage(this.manager.update(currentEntity) ? "Update Successful" : "Update Error: Erroneous entry");
            }
            dispose();
        } else {
            Helper.showMessage("fill");
        }
    }

    protected void initializeEventListeners() {
        button_cancel.addActionListener(e -> dispose());
        button_save.addActionListener(e -> save());
    }
}
