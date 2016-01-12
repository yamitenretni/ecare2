package com.javaschool.ecare.controller;

import com.javaschool.ecare.domain.Client;
import com.javaschool.ecare.domain.Contract;
import com.javaschool.ecare.domain.Option;
import com.javaschool.ecare.domain.Role;
import com.javaschool.ecare.domain.Tariff;
import com.javaschool.ecare.domain.User;
import com.javaschool.ecare.form.CartContractForm;
import com.javaschool.ecare.form.CartForm;
import com.javaschool.ecare.form.CompareList;
import com.javaschool.ecare.form.ContractAvailableOptionForm;
import com.javaschool.ecare.service.ClientService;
import com.javaschool.ecare.service.ContractService;
import com.javaschool.ecare.service.TariffService;
import com.javaschool.ecare.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.javaschool.ecare.service.OptionService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class MainController {

    @Autowired
    private OptionService optionService;

    @Autowired
    private TariffService tariffService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartForm cartForm;

    @Autowired
    private CompareList compareList;

    private static final Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    @Qualifier("contractValidator")
    private Validator contractValidator;

    @Autowired
    @Qualifier("tariffValidator")
    private Validator tariffValidator;

    @Autowired
    @Qualifier("optionValidator")
    private Validator optionValidator;

    @Autowired
    @Qualifier("userValidator")
    private Validator userValidator;

    @Autowired
    @Qualifier("clientValidator")
    private Validator clientValidator;

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @PostConstruct
    public void init() {
        return;
    }

    @RequestMapping("/login")
    public ModelAndView loginForm(Model model,
                                  @RequestParam(required = false) String error,
                                  @RequestParam(required = false) String logout,
                                  HttpServletRequest request) {
        if (error != null) {
            model.addAttribute("error", "label.login.error");
        }

        if (logout != null) {
            model.addAttribute("msg", "label.logout.message");
        }

        return new ModelAndView("login");
    }

    @RequestMapping("/staff/option")
    public ModelAndView optionList(Model model) {

        List<Option> options = new ArrayList<>();

        options.addAll(optionService.getActiveOptions());
        model.addAttribute("option", new Option());
        model.addAttribute("optionList", options);

        return new ModelAndView("option");
    }

    @RequestMapping("/staff/tariff")
    public ModelAndView tariffList(Model model) {

        model.addAttribute("tariffList", tariffService.getActiveTariffs());

        return new ModelAndView("tariff");
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping("/staff/option/add")
    public ModelAndView newOption(Model model,
                                  HttpServletRequest request) {
        Option option = (Option) getFlashAttribute(request, "option");
        if (option == null) {
            option = new Option();
        }

        model.addAttribute("option", option);
        model.addAttribute("optionList", optionService.getActiveOptions());

        return new ModelAndView("new-option-page");
    }


    @RequestMapping(value = "/staff/option", method = POST)
    public ModelAndView addOption(@ModelAttribute Option option,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam(required = false) long id,
                                  @RequestHeader() String referer) {

        optionValidator.validate(option, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            redirectAttributes.addFlashAttribute("option", option);

            return new ModelAndView("redirect:" + referer);
        }

        optionService.upsertOption(option);
        logger.info("Option " + option.getName() + " created");

        return new ModelAndView("redirect:/staff/option");
    }

    @RequestMapping(value = "/staff/tariff/add")
    public ModelAndView newTariff(Model model,
                                  HttpServletRequest request) {
        Tariff tariff = (Tariff) getFlashAttribute(request, "tariff");
        if (tariff == null) {
            tariff = new Tariff();
        }
        model.addAttribute("tariff", tariff);
        model.addAttribute("optionList", optionService.getActiveOptions());

        return new ModelAndView("new-tariff-page");
    }

    @RequestMapping(value = "/staff/tariff", method = POST)
    public ModelAndView addTariff(@ModelAttribute Tariff tariff,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  @RequestHeader() String referer) {
        tariffValidator.validate(tariff, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            redirectAttributes.addFlashAttribute("tariff", tariff);

            return new ModelAndView("redirect:" + referer);
        }

        tariffService.upsertTariff(tariff);
        logger.info("Tariff " + tariff.getName() + " created");

        return new ModelAndView("redirect:/staff/tariff");
    }

    @RequestMapping(value = "/staff/tariff/{tariffId}/delete")
    public ModelAndView deleteTariff(@RequestHeader(required = false) final String referer,
                                     @PathVariable() long tariffId) {
        String retPath = "";
        if (referer.isEmpty()) {
            retPath = "/staff/tariff";
        } else {
            retPath = referer;
        }

        tariffService.deleteTariff(tariffId);

        return new ModelAndView("redirect:" + retPath);
    }

    @RequestMapping(value = "/staff/option/{optionId}/delete")
    public ModelAndView deleteOption(@RequestHeader(required = false) final String referer,
                                     @PathVariable() long optionId) {
        String retPath = "";
        if (referer == null) {
            retPath = "/staff/option";
        } else {
            retPath = referer;
        }

        optionService.deleteOption(optionId);

        return new ModelAndView("redirect:" + retPath);
    }

    @RequestMapping(value = "/staff/option/{optionId}/edit")
    public ModelAndView editOption(Model model,
                                   @PathVariable() long optionId,
                                   HttpServletRequest request) {

        String path = "redirect:/staff/option";

        Option option;
        option = (Option) getFlashAttribute(request, "option");
        if (option == null) {
            option = optionService.getById(optionId);
        }
        if (option != null) {
            List<Option> optionList = optionService.getActiveOptions();
            optionList.remove(option);
            model.addAttribute("option", option);
            model.addAttribute("optionList", optionList);
            path = "option-page";
        }

        return new ModelAndView(path);
    }

    @RequestMapping(value = "/staff/tariff/{tariffId}/edit")
    public ModelAndView editTariff(Model model,
                                   @PathVariable() long tariffId,
                                   HttpServletRequest request) {
        Tariff tariff = (Tariff) getFlashAttribute(request, "tariff");
        if (tariff == null) {
            tariff = tariffService.getById(tariffId);
        }

        if (tariff != null) {
            model.addAttribute("tariff", tariff);
            model.addAttribute("optionList", optionService.getActiveOptions());

            return new ModelAndView("tariff-page");
        } else {
            return new ModelAndView("redirect:/staff/tariff");
        }
    }

    @RequestMapping(value = "/staff/client")
    public ModelAndView clientLest(Model model) {
        model.addAttribute("clientList", clientService.getClients());

        return new ModelAndView("client");
    }

    @RequestMapping(value = "/staff/client/add")
    public ModelAndView addClient(Model model,
                                  HttpServletRequest request) {

        Contract contract = (Contract) getFlashAttribute(request, "contract");
        if (contract == null) {
            contract = new Contract();
        }
        model.addAttribute("contract", contract);
        model.addAttribute("tariffList", tariffService.getActiveTariffs());

        return new ModelAndView("client-registration");
    }

    @RequestMapping(value = "/staff/client/{clientId}/contract")
    public ModelAndView addContract(Model model,
                                    @PathVariable() long clientId,
                                    HttpServletRequest request) {

        Client client = clientService.getById(clientId);
        Contract contract = (Contract) getFlashAttribute(request, "contract");
        if (contract == null) {
            contract = new Contract();
            contract.setClient(client);
        }
        model.addAttribute("contract", contract);
        model.addAttribute("tariffList", tariffService.getActiveTariffs());
        model.addAttribute("optionList", optionService.getActiveOptions());

        return new ModelAndView("contract-registration");
    }

    @RequestMapping(value = "/staff/contract", method = POST)
    public ModelAndView saveContract(@ModelAttribute Contract contract,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     @RequestHeader() String referer) {
        if (contract.getActivatedOptions() == null) {
            contract.setActivatedOptions(new HashSet<Option>());
        }
        contractValidator.validate(contract, result);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResult", result);
            redirectAttributes.addFlashAttribute("contract", contract);

            return new ModelAndView("redirect:" + referer);
        }

        Contract newContract = contractService.upsertContract(contract);

        logger.info("Contract " + contract.getNumber() +
                " for client " + contract.getClient().getUser().getFirstName() + " " + contract.getClient().getUser().getLastName() +
                " created");

        return new ModelAndView("redirect:/staff/contract/" + newContract.getId());
    }

    @RequestMapping(value = "/staff/client/new", method = POST)
    public ModelAndView saveClientAndContract(@ModelAttribute Contract contract,
                                              RedirectAttributes redirectAttributes,
                                              BindingResult result) {
        Client client = contract.getClient();
        User user = client.getUser();
        contractValidator.validate(contract, result);

        if (result.hasErrors()) {
            user.setPassword("");
            redirectAttributes.addFlashAttribute("bindingResult", result);
            redirectAttributes.addFlashAttribute("contract", contract);

            return new ModelAndView("redirect:/staff/client/add");
        }

        user.setRole(Role.ROLE_CLIENT);
        User newUser = userService.addUser(user);

        client.setUser(newUser);
        Client newClient = clientService.upsertClient(client);
        contract.setClient(newClient);
        contractService.upsertContract(contract);



        return new ModelAndView("redirect:/staff/client/" + newClient.getId());
    }

    @RequestMapping(value = "/staff/contract/{contractId}")
    public ModelAndView readContract(Model model,
                                     @PathVariable() long contractId) {
        Contract contract = contractService.getById(contractId);
        model.addAttribute("contract", contract);

        model.addAttribute("nonAvailableOptions", cartForm.getContractDeactivatedOptions(contract));
        model.addAttribute("tariffs", cartForm.getAvailableTariffs(contract));

        List<ContractAvailableOptionForm> allAvailableOptions = cartForm.getAllAvailableOptions(contract);
        model.addAttribute("allAvailableOptions", allAvailableOptions);

        return new ModelAndView("contract-page");
    }

    @RequestMapping(value = "/staff/client/{clientId}")
    public ModelAndView readClient(Model model,
                                   @PathVariable() long clientId) {
        Client client = clientService.getById(clientId);
        model.addAttribute("client", client);

        return new ModelAndView("client-page");
    }

    @RequestMapping(value = "/staff/clients/search")
    public ModelAndView searchClient(@RequestParam() String contractNumber,
                                     @RequestHeader() String referer) {
        String path = referer;
        Contract contract = contractService.getByNumber(contractNumber);
        if (contract != null) {
            path = "/staff/client/" + contract.getClient().getId();
        }

        return new ModelAndView("redirect:" + path);
    }

    @RequestMapping(value = "/staff/tariff/{tariffId}/json", produces = "application/json")
    @ResponseBody
    public Tariff getTariffOptions(@PathVariable() long tariffId) {
        Tariff tariff = tariffService.getById(tariffId);
        return tariff;
    }

    @RequestMapping(value = "/cart/{contractId}/deactivate/{optionId}")
    public ModelAndView addDeactivatedOptionToCart(@RequestHeader(required = false) final String referer,
                                                   @PathVariable() long contractId,
                                                   @PathVariable() long optionId) {
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.addDeactivatedOption(optionService.getById(optionId));

        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/cart/{contractId}/deactivate/{optionId}/cancel")
    public ModelAndView deleteDeactivatedOptionFromCart(@RequestHeader(required = false) final String referer,
                                                        @PathVariable() long contractId,
                                                        @PathVariable() long optionId) {
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.deleteDeactivatedOption(optionService.getById(optionId));

        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/cart/{contractId}/add/{optionId}")
    public ModelAndView addNewOptionToCart(@RequestHeader(required = false) final String referer,
                                           @PathVariable() long contractId,
                                           @PathVariable() long optionId) {
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.addNewOption(optionService.getById(optionId));

        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/cart/{contractId}/add/{optionId}/cancel")
    public ModelAndView deleteNewOptionFromCart(@RequestHeader(required = false) final String referer,
                                                @PathVariable() long contractId,
                                                @PathVariable() long optionId) {
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.deleteAddedOption(optionService.getById(optionId));

        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/cart/{contractId}/newtariff")
    public ModelAndView addNewTariffToCart(@RequestHeader(required = false) final String referer,
                                           @PathVariable() long contractId,
                                           @RequestParam() long newTariff) {
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.changeTariff(tariffService.getById(newTariff));

        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/cart/{contract_id}/newtariff/cancel")
    public ModelAndView deleteNewTariffFromCart(@RequestHeader(value = "referer", required = false) final String referer,
                                                @PathVariable("contract_id") long contractId) {
        String retPath = referer;
        if (retPath == null) {
            retPath = "/contract/" + contractId;
        }
        CartContractForm position = cartForm.getCartContractForm(contractService.getById(contractId));
        position.deleteNewTariff();

        return new ModelAndView("redirect:" + retPath);
    }

    @RequestMapping(value = "/cart/{contractId}/save")
    public ModelAndView saveContractInCart(@RequestHeader(required = false) final String referer,
                                           @PathVariable() long contractId) {
        String retPath = referer;
        if (retPath == null) {
            retPath = "/contract/" + contractId;
        }
        Contract contract = contractService.getById(contractId);
        CartContractForm position = cartForm.getCartContractForm(contract);
        Set<Option> deactivatedOptions = position.getDeactivatedOptionsFromList();
//        deactivatedOptions.addAll(position.getUnsupportedOptions());
//        deactivatedOptions.addAll(position.getDependingOptions());
        Set<Option> newOptions = position.getNewOptionsFromList();
        Tariff newTariff = position.getNewTariff();

        List<Option> activeOptions = new ArrayList<>(contract.getActivatedOptions());
        if (!deactivatedOptions.isEmpty()) {
            activeOptions.removeAll(deactivatedOptions);
        }

        if (newTariff != null) {
            contract.setTariff(newTariff);
        }

        if (!newOptions.isEmpty()) {
            activeOptions.addAll(newOptions);
        }
        contract.setActivatedOptions(new HashSet<Option>(activeOptions));

        contractService.upsertContract(contract);

        cartForm.deleteCartContractForm(position);
        return new ModelAndView("redirect:" + retPath);
    }

    @RequestMapping(value = "/cart/{contractId}/clear")
    public ModelAndView clearContractInCart(@RequestHeader(required = false) final String referer,
                                            @PathVariable() long contractId) {
        String retPath = referer;
        if (retPath == null) {
            retPath = "/contract/" + contractId;
        }
        Contract contract = contractService.getById(contractId);
        CartContractForm position = cartForm.getCartContractForm(contract);

        cartForm.deleteCartContractForm(position);
        return new ModelAndView("redirect:" + retPath);
    }

    @RequestMapping(value = "/welcome")
    public ModelAndView getWelcomePage(Model model, HttpSession session) {
        String userPath;
        if (hasRole("ROLE_CLIENT")) {
            userPath = "/my";
        } else if (hasRole("ROLE_EMPLOYEE")) {
            userPath = "/staff";
        } else {
            userPath = "/login";
        }

        return new ModelAndView("redirect:" + userPath);
    }

    @RequestMapping(value = "/my")
    public ModelAndView getClientMainPage() {
        return new ModelAndView("redirect:/my/contract");
    }

    @RequestMapping(value = "/staff")
    public ModelAndView getStaffMainPage() {
        return new ModelAndView("redirect:/staff/client");
    }

    @RequestMapping(value = "/my/contract")
    public ModelAndView getClientContracts(HttpSession session,
                                           Model model) {
        User user = (User) session.getAttribute("currentUser");
        Client client = clientService.getByUser(user);
        model.addAttribute("client", client);
        model.addAttribute("contractList", client.getContracts());

        return new ModelAndView("cabinet-main");
    }

    @RequestMapping(value = "/my/contract/{contractId}")
    public ModelAndView readClientContract(Model model,
                                           @PathVariable() long contractId) {
        Contract contract = contractService.getById(contractId);

        if (getPrincipal().equals(contract.getClient().getUser().getLogin())) {
            return readContract(model, contractId);
        } else {
            return new ModelAndView("redirect:/my/contract");
        }
    }

    @RequestMapping(value = "/my/tariff")
    public ModelAndView readClientContract(Model model) {
        model.addAttribute("tariffList", tariffService.getActiveTariffs());
        return new ModelAndView("tariff");
    }

    private Map<String, Object> addCompare(long tariffId) {
        Map<String, Object> result = new HashMap<>();
        Tariff tariff = tariffService.getById(tariffId);
        if (tariff != null && !tariff.isDeleted()) {
            result.put("success", compareList.addCompare(tariff));
            result.put("tariff", tariff.getName());
        }
        else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "/my/tariff/{tariffId}/compare")
    public ModelAndView addCompareTariff(@PathVariable long tariffId) {
        addCompare(tariffId);

        return new ModelAndView("redirect:/my/tariff");
    }

    @ResponseBody
    @RequestMapping(value = "/my/tariff/{tariffId}/compare/json", produces = "application/json")
    public Map<String, Object> addCompareTariffJson(@PathVariable long tariffId) {
        return addCompare(tariffId);
    }

    private Map<String, Object> removeCompare(long tariffId) {
        Map<String, Object> result = new HashMap<>();
        Tariff tariff = tariffService.getById(tariffId);
        if (tariff != null) {
            result.put("success", compareList.removeCompare(tariff));
            result.put("tariff", tariff.getName());
        }
        else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping(value = "/my/tariff/{tariffId}/cancel")
    public ModelAndView removeCompareTariff(@PathVariable long tariffId) {
        removeCompare(tariffId);

        return new ModelAndView("redirect:/my/tariff");
    }

    @ResponseBody
    @RequestMapping(value = "/my/tariff/{tariffId}/cancel/json", produces = "application/json")
    public Map<String, Object> removeCompareTariffJson(@PathVariable long tariffId) {
        return removeCompare(tariffId);
    }

    @RequestMapping(value = "/my/compare-list")
    public ModelAndView getCompareList(Model model,
                                       HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("optionList", optionService.getActiveOptions());
        model.addAttribute("currentClient", clientService.getByUser(user));
        return new ModelAndView("compare-list");
    }

    @RequestMapping(value = "/staff/contract/{contractId}/block")
    public ModelAndView blockContract(@PathVariable() long contractId,
                                      @RequestHeader() String referer,
                                      HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Contract contract = contractService.getById(contractId);
        contractService.blockContract(contract, currentUser);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/staff/contract/{contractId}/unlock")
    public ModelAndView unlockContract(@PathVariable() long contractId,
                                       @RequestHeader() String referer) {
        Contract contract = contractService.getById(contractId);
        contractService.unlockContract(contract);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/staff/client/{clientId}/block")
    public ModelAndView blockClient(@PathVariable() long clientId,
                                    @RequestHeader() String referer,
                                    HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Client client = clientService.getById(clientId);
//        Contract contract = contractService.getById(contractId);
        clientService.blockClient(client, currentUser);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/staff/client/{clientId}/unlock")
    public ModelAndView unlockClient(@PathVariable() long clientId,
                                     @RequestHeader() String referer) {
        Client client = clientService.getById(clientId);
        clientService.unlockClient(client);
        return new ModelAndView("redirect:" + referer);
    }


    @RequestMapping(value = "/my/contract/{contractId}/block")
    public ModelAndView blockContractByClient(@PathVariable() long contractId,
                                              @RequestHeader() String referer,
                                              HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Contract contract = contractService.getById(contractId);
        contractService.blockContract(contract, currentUser);
        return new ModelAndView("redirect:" + referer);
    }

    @RequestMapping(value = "/my/contract/{contractId}/unlock")
    public ModelAndView unlockContractByClient(@PathVariable() long contractId,
                                               @RequestHeader() String referer,
                                               HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Contract contract = contractService.getById(contractId);
        if (contract.getBlockingUser().equals(currentUser)) {
            contractService.unlockContract(contract);
        }
        return new ModelAndView("redirect:" + referer);
    }

    private String getPrincipal() {
        String username = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession().getAttribute(key);

        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }

        return error;
    }

    private boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }

    private Object getFlashAttribute(HttpServletRequest request, String attributeName) {
        Map<String, ?> flash = RequestContextUtils.getInputFlashMap(request);
        Object result = null;

        if (flash != null) {
            result = flash.get(attributeName);
        }

        return result;
    }
}
