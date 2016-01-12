package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Option;

public class CartContractOption {
    private Option option;
    private CartOperation operation;
    private boolean addedByUser;
    private boolean isDepending = false;
    private boolean isUnsupported = false;
    private boolean isIncompatible = false;

    public CartContractOption() {
    }

    public CartContractOption(Option option, CartOperation operation, boolean addedByUser, boolean isDepending, boolean isUnsupported, boolean isIncompatible) {
        this.option = option;
        this.operation = operation;
        this.addedByUser = addedByUser;
        this.isDepending = isDepending;
        this.isUnsupported = isUnsupported;
        this.isIncompatible = isIncompatible;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public CartOperation getOperation() {
        return operation;
    }

    public void setOperation(CartOperation operation) {
        this.operation = operation;
    }

    public boolean isAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(boolean addedByUser) {
        this.addedByUser = addedByUser;
    }

    public boolean isCancelable() {
        return !isDepending() && !isUnsupported();
    }


    public boolean isDepending() {
        return isDepending;
    }

    public void setDepending(boolean depending) {
        isDepending = depending;
    }

    public boolean isUnsupported() {
        return isUnsupported;
    }

    public void setUnsupported(boolean unsupported) {
        isUnsupported = unsupported;
    }

    public boolean isIncompatible() {
        return isIncompatible;
    }

    public void setIncompatible(boolean incompatible) {
        isIncompatible = incompatible;
    }

    public boolean isAdding() {
        return CartOperation.adding.equals(operation);
    }

    public boolean isDeleting() {
        return CartOperation.deleting.equals(operation);
    }

    @Override
    public int hashCode() {
        return option.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (CartContractOption.class.equals(obj.getClass())) {
            return option.equals(((CartContractOption)obj).getOption());
        }
        return false;
    }
}
