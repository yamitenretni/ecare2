package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Tariff;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CompareList {
    private Set<Tariff> compareTariffs;

    public CompareList() {
        compareTariffs = new LinkedHashSet<>();
    }

    public Set<Tariff> getCompareTariffs() {
        return compareTariffs;
    }

    public boolean removeCompare(Tariff tariff) {
        return compareTariffs.remove(tariff);
    }

    public boolean addCompare(Tariff tariff) {
        return compareTariffs.add(tariff);
    }
}
