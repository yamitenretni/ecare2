package com.javaschool.ecare.form;

import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.domain.Tariff;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.br.CPF;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/spring-test.xml"})
public class CartContractFormTest {
    private static List<Option> testOptions;
    private static List<Tariff> testTariffs;
    private static List<Contract> testContracts;

    private static Logger logger;


    @BeforeClass
    public static void init() {
        logger = Logger.getLogger(CartContractFormTest.class);

        initTestOptions();
        initTestTariffs();
        initTestContracts();
    }

    @Test
    public void initTest() {
        CartContractForm cartPosition = new CartContractForm(testContracts.get(0));
        assertTrue("11111".equals(cartPosition.getContract().getNumber()));
        assertTrue(testOptions.size() == 10);
        assertTrue(testOptions.get(0).getIncompatibleOptions().size() == 2);
    }

    // Add new option positive tests
    @Test
    public void addSimpleOption() {
        Contract contract = testContracts.get(0);
        Option addingOption = testOptions.get(8);
        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(addingOption);
        assertTrue(cartPosition.isNewOption(addingOption));
    }

    @Test
    public void addDependOptionWithMandatoryInContract() {
        Contract contract = testContracts.get(3);
        Option addingOption = testOptions.get(3);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(addingOption);
        assertTrue(cartPosition.isNewOption(addingOption));
    }

    @Test
    public void addDependOptionWithMandatoryInCart() {
        Contract contract = testContracts.get(0);
        Option mandatoryOption = testOptions.get(0);
        Option addingOption = testOptions.get(3);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(mandatoryOption);
        cartPosition.addNewOption(addingOption);

        assertTrue(cartPosition.isNewOption(mandatoryOption));
        assertTrue(cartPosition.isNewOption(addingOption));
    }

    @Test
    public void addNotAvailableOption() {
        Contract contract = testContracts.get(0);
        Option addingOption = testOptions.get(9);
        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(addingOption);

        assertFalse(cartPosition.isAvailableOption(addingOption));

        assertFalse(cartPosition.isNewOption(addingOption));
    }

    // Add new option negative tests

    @Test
    public void addDependOptionWithoutMandatory() {
        Contract contract = testContracts.get(0);
        Option addingOption = testOptions.get(3);
        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(addingOption);
        assertFalse(cartPosition.isNewOption(addingOption));
    }

    @Test
    public void addDependOptionWithDeactivatedMandatory() {
        Contract contract = testContracts.get(2);
        Option mandatoryOption = testOptions.get(0);
        Option addingOption = testOptions.get(3);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(mandatoryOption);
        cartPosition.addNewOption(addingOption);
        assertTrue(cartPosition.isDeactivatedOption(mandatoryOption));
        assertFalse(cartPosition.isNewOption(addingOption));
    }

    @Test
    public void addCartIncompatibleOption() {
        Contract contract = testContracts.get(0);
        Option addingOption = testOptions.get(2);
        Option incompatibleOption = testOptions.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(incompatibleOption);
        cartPosition.addNewOption(addingOption);

        assertTrue(cartPosition.isNewOption(incompatibleOption));
        assertFalse(cartPosition.isNewOption(addingOption));
    }

    // Delete added option tests
    @Test
    public void deleteNewSimpleOption() {
        Contract contract = testContracts.get(0);
        Option deletingOption = testOptions.get(8);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(deletingOption);

        assertTrue(cartPosition.isNewOption(deletingOption));
        cartPosition.deleteAddedOption(deletingOption);
        assertFalse(cartPosition.isNewOption(deletingOption));
    }

    @Test
    public void deleteNewDependOption() {
        Contract contract = testContracts.get(0);
        Option deletingOption = testOptions.get(3);
        Option mandatoryOption = testOptions.get(0);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(mandatoryOption);
        cartPosition.addNewOption(deletingOption);
        assertTrue(cartPosition.isNewOption(mandatoryOption));
        assertTrue(cartPosition.isNewOption(deletingOption));
        cartPosition.deleteAddedOption(deletingOption);
        assertFalse(cartPosition.isNewOption(deletingOption));
    }

    @Test
    public void deleteNewMandatoryOptionForCart() {
        Contract contract = testContracts.get(0);
        Option deletingOption = testOptions.get(0);
        Option dependOption = testOptions.get(3);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(deletingOption);
        cartPosition.addNewOption(dependOption);
        assertTrue(cartPosition.isNewOption(deletingOption));
        assertTrue(cartPosition.isNewOption(dependOption));
        assertFalse(cartPosition.isNotCancelable(dependOption));
        assertFalse(cartPosition.isNotCancelable(deletingOption));
        cartPosition.deleteAddedOption(deletingOption);
        assertFalse(cartPosition.isNewOption(deletingOption));
        assertFalse(cartPosition.isNewOption(dependOption));
    }

    @Test
    public void deleteNewMandatoryOptionForContract() {
        Contract contract = testContracts.get(3);
        Option deletingOption = testOptions.get(6);
        Option mandatoryOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(deletingOption);
        cartPosition.addDeactivatedOption(mandatoryOption);
        assertTrue(cartPosition.isNewOption(deletingOption));
        assertTrue(cartPosition.isDeactivatedOption(mandatoryOption));

        cartPosition.deleteAddedOption(deletingOption);
        assertFalse(cartPosition.isNewOption(deletingOption));
        assertTrue(cartPosition.isDeactivatedOption(mandatoryOption));
        assertFalse(cartPosition.isNotCancelable(mandatoryOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
    }

    // Add deactivated option positive tests
    @Test
    public void deactivateSimpleOption() {
        Contract contract = testContracts.get(1);
        Option deactivatingOption = testOptions.get(8);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertTrue(cartPosition.isDeactivatedOption(deactivatingOption));
    }

    @Test
    public void deactivateDependOption() {
        Contract contract = testContracts.get(3);
        Option deactivatingOption = testOptions.get(4);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertTrue(cartPosition.isDeactivatedOption(deactivatingOption));
    }

    @Test
    public void deactivateMandatoryForContractOption() {
        Contract contract = testContracts.get(3);
        Option deactivatingOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertTrue(cartPosition.isDeactivatedOption(deactivatingOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
    }

    @Test
    public void deactivateMandatoryForCartOption() {
        Contract contract = testContracts.get(2);
        Option deactivatingOption = testOptions.get(0);
        Option dependOption = testOptions.get(3);
        contract.getActivatedOptions().add(deactivatingOption);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(dependOption);
        assertTrue(cartPosition.isNewOption(dependOption));
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertTrue(cartPosition.isDeactivatedOption(deactivatingOption));
        assertFalse(cartPosition.isDeactivatedOption(dependOption));
    }

    @Test
    public void deactivateIncompatibleOption() {
        Contract contract = testContracts.get(3);
        Option deactivatingOption = testOptions.get(1);
        Option incompatibleOption = testOptions.get(2);

        CartContractForm cartPosition = new CartContractForm(contract);

        assertFalse(cartPosition.isAvailableOption(incompatibleOption));
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertTrue(cartPosition.isDeactivatedOption(deactivatingOption));
        assertTrue(cartPosition.isAvailableOption(incompatibleOption));
    }

    // Add deactivated option negative test
    @Test
    public void deactivateNotActiveOption() {
        Contract contract = testContracts.get(3);
        Option deactivatingOption = testOptions.get(3);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(deactivatingOption);
        assertFalse(cartPosition.isDeactivatedOption(deactivatingOption));
    }

    @Test
    public void addContractIncompatibleOption() {
        Contract contract = testContracts.get(3);
        Option addingOption = testOptions.get(2);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(addingOption);

        assertFalse(cartPosition.isNewOption(addingOption));
    }

    // Change tariff positive tests

    @Test
    public void setTariffWithoutSimpleContractOption() {
        Contract contract = testContracts.get(1);
        Option unsupportedOption = testOptions.get(8);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertFalse(cartPosition.isAvailableOption(unsupportedOption));
    }

    @Test
    public void setTariffWithoutSimpleDeactivatedContractOption() {
        Contract contract = testContracts.get(1);
        Option unsupportedOption = testOptions.get(8);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(unsupportedOption);
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));

        cartPosition.changeTariff(cartTariff);
        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertFalse(cartPosition.isAvailableOption(unsupportedOption));
    }

    @Test
    public void setTariffWithoutMandatoryContractOption() {
        Contract contract = testContracts.get(3);
        Tariff cartTariff = testTariffs.get(2);
        Option unsupportedOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);
        contract.getActivatedOptions().add(unsupportedOption);
        contract.getActivatedOptions().add(dependOption);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
    }

    @Test
    public void setTariffWithoutDeactivatedMandatoryContractOption() {
        Contract contract = testContracts.get(3);
        Tariff cartTariff = testTariffs.get(2);
        Option unsupportedOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);
        contract.getActivatedOptions().add(unsupportedOption);
        contract.getActivatedOptions().add(dependOption);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(unsupportedOption);
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));

        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
    }

    @Test
    public void setTariffWithoutSimpleCartOption() {
        Contract contract = testContracts.get(0);
        Option unsupportedOption = testOptions.get(8);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(unsupportedOption);
        assertTrue(cartPosition.isNewOption(unsupportedOption));
        cartPosition.changeTariff(cartTariff);
        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertFalse(cartPosition.isNewOption(unsupportedOption));
    }

    @Test
    public void setTariffWithoutNewMandatoryOptionForContract() {
        Contract contract = testContracts.get(3);
        Option unsupportedOption = testOptions.get(6);
        Option dependOption = testOptions.get(4);
        Option mandatoryOption = testOptions.get(1);
        Tariff cartTariff = testTariffs.get(0);

        contract.getActivatedOptions().add(mandatoryOption);
        contract.getActivatedOptions().add(dependOption);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(unsupportedOption);
        cartPosition.addDeactivatedOption(mandatoryOption);
        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertFalse(cartPosition.isNewOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isDeactivatedOption(mandatoryOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
        assertFalse(cartPosition.isAvailableOption(unsupportedOption));
        assertFalse(cartPosition.isNotCancelable(mandatoryOption));
    }

    @Test
    public void setTariffWithoutNewMandatoryOptionForCart() {
        Contract contract = testContracts.get(0);
        Option unsupportedOption = testOptions.get(5);
        Option dependOption = testOptions.get(3);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(unsupportedOption);
        cartPosition.addNewOption(dependOption);

        assertTrue(cartPosition.isNewOption(unsupportedOption));
        assertTrue(cartPosition.isNewOption(dependOption));

        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertFalse(cartPosition.isNewOption(unsupportedOption));
        assertTrue(cartPosition.isDependOption(dependOption));
        assertFalse(cartPosition.isNewOption(dependOption));
    }

    // Change tariff negative tests
    @Test
    public void setCurrentTariff() {
        Contract contract = testContracts.get(0);
        Tariff cartTariff = testTariffs.get(0);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.changeTariff(cartTariff);
        assertNull(cartPosition.getNewTariff());
    }

    // Cancel new tariff tests
    @Test
    public void cancelEmptyTariff() {
        Contract contract = testContracts.get(0);

        CartContractForm cartPosition = new CartContractForm(contract);
        assertNull(cartPosition.getNewTariff());
        cartPosition.deleteNewTariff();
        assertNull(cartPosition.getNewTariff());
    }

    @Test
    public void cancelTariffWithoutSimpleContractOption() {
        Contract contract = testContracts.get(1);
        Option unsupportedOption = testOptions.get(8);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.changeTariff(cartTariff);
        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        cartPosition.deleteNewTariff();
        assertNull(cartPosition.getNewTariff());
        assertFalse(cartPosition.isDeactivatedOption(unsupportedOption));
    }

    @Test
    public void cancelTariffWithoutSimpleDeactivatedContractOption() {
        Contract contract = testContracts.get(1);
        Option unsupportedOption = testOptions.get(8);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addDeactivatedOption(unsupportedOption);
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertFalse(cartPosition.isNotCancelable(unsupportedOption));

        cartPosition.changeTariff(cartTariff);
        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));

        cartPosition.deleteNewTariff();
        assertNull(cartPosition.getNewTariff());
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertFalse(cartPosition.isNewOption(unsupportedOption));
    }

    @Test
    public void cancelTariffWithoutMandatoryForContractOption() {
        Contract contract = testContracts.get(3);
        Option unsupportedOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);
        Tariff cartTariff = testTariffs.get(2);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));

        cartPosition.deleteNewTariff();

        assertNull(cartPosition.getNewTariff());
        assertFalse(cartPosition.isDeactivatedOption(unsupportedOption));
        assertFalse(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isAvailableOption(unsupportedOption));
    }

    @Test
    public void cancelTariffWithoutDeactivatedMandatoryOption() {
        Contract contract = testContracts.get(3);
        Option unsupportedOption = testOptions.get(1);
        Option dependOption = testOptions.get(4);
        Tariff cartTariff = testTariffs.get(2);

        CartContractForm cartPosition = new CartContractForm(contract);

        cartPosition.addDeactivatedOption(unsupportedOption);
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertFalse(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));

        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));

        cartPosition.deleteNewTariff();

        assertNull(cartPosition.getNewTariff());
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isDeactivatedOption(dependOption));
        assertFalse(cartPosition.isNotCancelable(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(dependOption));
    }

    @Test
    public void cancelTariffWithoutMandatoryForCartOption() {
        Contract contract = testContracts.get(2);
        Option unsupportedOption = testOptions.get(0);
        Option dependOption = testOptions.get(3);
        Tariff cartTariff = testTariffs.get(1);

        CartContractForm cartPosition = new CartContractForm(contract);
        cartPosition.addNewOption(dependOption);
        cartPosition.changeTariff(cartTariff);

        assertTrue(cartTariff.equals(cartPosition.getNewTariff()));
        assertTrue(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isNotCancelable(unsupportedOption));
        assertFalse(cartPosition.isNewOption(dependOption));

        cartPosition.deleteNewTariff();

        assertNull(cartPosition.getNewTariff());
        assertFalse(cartPosition.isDeactivatedOption(unsupportedOption));
        assertTrue(cartPosition.isInCart(dependOption));
        assertTrue(cartPosition.isNewOption(dependOption));
    }

    private static void setMandatoryOptions(long manOption, long depOption) {
        Option mandatoryOption = testOptions.get((int) manOption);
        Option dependOption = testOptions.get((int) depOption);

        mandatoryOption.getDependOptions().add(dependOption);
        dependOption.getMandatoryOptions().add(mandatoryOption);
    }

    private static void setIncompatibleOptions(long fOption, long sOption) {
        Option firstOption = testOptions.get((int) fOption);
        Option secondOption = testOptions.get((int) sOption);

        firstOption.getIncompatibleOptions().add(secondOption);
        secondOption.getIncompatibleOptions().add(firstOption);
    }

    private static void setTariffOptions(long tariff, List<Long> options) {
        for (long optId : options) {
            Option option = testOptions.get((int) optId);
            testTariffs.get((int) tariff).getAvailableOptions().add(option);
        }
    }

    private static void initTestOptions() {
        testOptions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Option option = new Option();
            option.setId(i);
            option.setName("Test option" + i);
            option.setConnectionCost(i * 3);
            option.setRegularCost(i * 1.5);
            testOptions.add(option);
        }

        setIncompatibleOptions(0, 1);
        setIncompatibleOptions(1, 2);
        setIncompatibleOptions(2, 0);

        setMandatoryOptions(0, 3);
        setMandatoryOptions(1, 3);
        setMandatoryOptions(2, 3);
        setMandatoryOptions(5, 3);
        setMandatoryOptions(6, 3);

        setMandatoryOptions(0, 4);
        setMandatoryOptions(1, 4);
        setMandatoryOptions(2, 4);
        setMandatoryOptions(5, 4);
        setMandatoryOptions(6, 4);

        StringBuilder sb = new StringBuilder();
        sb.append("================================= Testing options =================================");
        sb.append(System.getProperty("line.separator"));
        for (Option option : testOptions) {
            sb.append("======== Name: " + option.getName() + " (id: " + option.getId() + ") ========");
            sb.append(System.getProperty("line.separator"));
            sb.append("Connection cost: " + option.getConnectionCost());
            sb.append(System.getProperty("line.separator"));
            sb.append("Regular cost: " + option.getRegularCost());
            sb.append(System.getProperty("line.separator"));
            sb.append("Incompatible options (" + option.getIncompatibleOptions().size() + " options):");
            sb.append(System.getProperty("line.separator"));
            for (Option incOption : option.getIncompatibleOptions()) {
                sb.append("   " + incOption.getName() + " (id: " + incOption.getId() + ")");
                sb.append(System.getProperty("line.separator"));
            }
            sb.append("Mandatory options (" + option.getMandatoryOptions().size() + " options):");
            sb.append(System.getProperty("line.separator"));
            for (Option manOption : option.getMandatoryOptions()) {
                sb.append("   " + manOption.getName() + " (id: " + manOption.getId() + ")");
                sb.append(System.getProperty("line.separator"));
            }
            sb.append("Depend options (" + option.getDependOptions().size() + " options):");
            sb.append(System.getProperty("line.separator"));
            for (Option depOption : option.getDependOptions()) {
                sb.append("   " + depOption.getName() + " (id: " + depOption.getId() + ")");
                sb.append(System.getProperty("line.separator"));
            }
        }
        logger.info(sb.toString());
    }

    private static void initTestTariffs() {
        testTariffs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tariff tariff = new Tariff();
            tariff.setName("Tariff" + i);
            tariff.setId(i);
            tariff.setRegularCost(i * 5);
            testTariffs.add(tariff);
        }

        List<Long> options = new ArrayList<>(Arrays.asList(0l, 1l, 3l, 5l, 8l));
        setTariffOptions(0, options);
        options = new ArrayList<>(Arrays.asList(1l, 2l, 3l, 4l, 6l, 7l));
        setTariffOptions(1, options);
        options = new ArrayList<>(Arrays.asList(3l, 4l, 5l, 6l, 7l, 8l, 9l));
        setTariffOptions(2, options);

        StringBuilder sb = new StringBuilder();
        sb.append("================================= Testing tariffs =================================");
        sb.append(System.getProperty("line.separator"));
        for (Tariff tariff : testTariffs) {
            sb.append("======== Name: " + tariff.getName() + " (id: " + tariff.getId() + ") ========");
            sb.append(System.getProperty("line.separator"));
            sb.append("Regular cost: " + tariff.getRegularCost());
            sb.append(System.getProperty("line.separator"));
            sb.append("Available options (" + tariff.getAvailableOptions().size() + " options):");
            sb.append(System.getProperty("line.separator"));
            for (Option avOption : tariff.getAvailableOptions()) {
                sb.append("   " + avOption.getName() + " (id: " + avOption.getId() + ")");
                sb.append(System.getProperty("line.separator"));
            }
        }
        logger.info(sb.toString());
    }

    private static void initTestContracts() {
        testContracts = new ArrayList<>();
        // create contract without options
        Contract contract = new Contract();
        contract.setNumber("11111");
        contract.setTariff(testTariffs.get(0));
        Set<Option> options = new HashSet<>();
        contract.setActivatedOptions(options);
        testContracts.add(contract);

        // create contract with simple option
        contract = new Contract();
        contract.setNumber("22222");
        contract.setTariff(testTariffs.get(0));
        options = new HashSet<>();
        options.add(testOptions.get(8));
        contract.setActivatedOptions(options);
        testContracts.add(contract);

        // create contract with mandatory option
        contract = new Contract();
        contract.setNumber("33333");
        contract.setTariff(testTariffs.get(0));
        options = new HashSet<>();
        options.add(testOptions.get(0));
        contract.setActivatedOptions(options);
        testContracts.add(contract);

        // create contract with mandatory and depend option
        contract = new Contract();
        contract.setNumber("98765");
        contract.setTariff(testTariffs.get(1));
        options = new HashSet<>();
        options.add(testOptions.get(1));
        options.add(testOptions.get(4));
        contract.setActivatedOptions(options);
        testContracts.add(contract);


    }
}
